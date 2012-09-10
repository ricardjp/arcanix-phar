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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import com.arcanix.php.phar.util.PharVersion;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public final class PharManifest implements PharWritable {

    private static final int BITMAP_SIGNATURE_FLAG = 0x00010000;

    private final File pharFile;
    private final byte[] pharFiles;
    private final List<PharEntry> pharEntries;
    private final PharMetadata pharMetadata;

    public PharManifest(
            final File pharFile,
            final byte[] pharFiles,
            final List<PharEntry> pharEntries,
            final PharMetadata pharMetadata) {

        if (pharFile == null) {
            throw new IllegalArgumentException("Phar file cannot be null");
        }
        if (pharFiles == null) {
            throw new IllegalArgumentException("Phar files bytes cannot be null");
        }
        if (pharEntries == null) {
            throw new IllegalArgumentException("Phar entries cannot be null");
        }
        if (pharMetadata == null) {
            throw new IllegalArgumentException("Phar metadata cannot be null");
        }
        this.pharFile = pharFile;
        this.pharFiles = pharFiles;
        this.pharEntries = pharEntries;
        this.pharMetadata = pharMetadata;
    }

    public void write(final PharOutputStream out) throws IOException {
        byte[] pharAlias = this.pharFile.getName().getBytes("UTF-8");


        ByteArrayOutputStream metadataOutputStream = new ByteArrayOutputStream();
        PharOutputStream pharOutputStream = new PharOutputStream(metadataOutputStream);
        pharOutputStream.write(this.pharMetadata);
        pharOutputStream.flush();
        pharOutputStream.close();

        byte[] metadataBytes = metadataOutputStream.toByteArray();

        int manifestLength = metadataBytes.length + this.pharFiles.length + pharAlias.length + 14;
        out.writeInt(manifestLength);
        out.writeInt(this.pharEntries.size());

        // version
        out.write(PharVersion.getVersionNibbles(Phar.PHAR_VERSION));

        // global bitmapped flags
        out.writeInt(BITMAP_SIGNATURE_FLAG);

        // write alias
        out.writeInt(pharAlias.length);
        out.write(pharAlias);

        // write metadata
        out.write(metadataBytes);
    }

}
