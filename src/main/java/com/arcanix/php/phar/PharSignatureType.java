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

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 * @see <a href="http://www.php.net/manual/en/phar.fileformat.signature.php">Phar Signature format</a>
 */
public enum PharSignatureType {

    MD5(1, "MD5", 16),
    SHA1(2, "SHA-1", 20),
    SHA256(3, "SHA-256", 32),
    SHA512(4, "SHA-512", 64);

    private final int flag;
    private final String algorithm;
    private final int numberOfBytes;

    private PharSignatureType(final int flag, final String algorithm, final int numberOfBytes) {
        this.flag = flag;
        this.algorithm = algorithm;
        this.numberOfBytes = numberOfBytes;
    }

    public int getFlag() {
        return this.flag;
    }

    public String getAlgorithm() {
        return this.algorithm;
    }

    public int getNumberOfBytes() {
        return this.numberOfBytes;
    }

}
