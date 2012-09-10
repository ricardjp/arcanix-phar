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
 */
public enum PharCompression {

    /** File is compressed with zlib compression - 0x00001000. */
    GZIP(new byte[] { 0, 0x10, 0, 0 }),

    /** File is compressed with bzip compression - 0x00002000. */
    BZIP2(new byte[] { 0, 0x20, 0, 0 });

    private final byte[] bitmapFlag;

    private PharCompression(final byte[] bitmapFlag) {
        this.bitmapFlag = bitmapFlag;
    }

    public byte[] getBitmapFlag() {
        return this.bitmapFlag;
    }

}
