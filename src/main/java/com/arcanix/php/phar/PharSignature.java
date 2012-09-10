/**
 * Copyright (C) 2012 Arcanix.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.arcanix.php.phar;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 * @see <a href="http://www.php.net/manual/en/phar.fileformat.signature.php">Phar Signature format</a>
 */
public final class PharSignature implements PharWritable {

    private static final byte[] MAGIC_GBMB = "GBMB".getBytes();

    private final File pharFile;
    private final PharSignatureType pharSignatureType;

    public PharSignature(final File pharFile, final PharSignatureType pharSignatureType) {
        if (pharFile == null) {
            throw new IllegalArgumentException("Phar file cannot be null");
        }
        if (pharSignatureType == null) {
            throw new IllegalArgumentException("Phar signature type cannot be null");
        }
        this.pharFile = pharFile;;
        this.pharSignatureType = pharSignatureType;
    }

    public void write(final PharOutputStream out) throws IOException {
        MessageDigest signature = null;
        try {
            signature = MessageDigest.getInstance(this.pharSignatureType.getAlgorithm());

            // create signature
            signature.update(Files.readAllBytes(this.pharFile.toPath()));

            // write signature
            out.write(signature.digest());

            // write signature flag
            out.writeInt(this.pharSignatureType.getFlag());

            // write magic GBMB
            out.write(MAGIC_GBMB);
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("Could not write signature to phar", e);
        }
    }

}
