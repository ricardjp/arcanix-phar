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
package com.arcanix.php.phar.util;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public final class PharVersion {

    /**
     * Utility class. Can't instantiate.
     */
    private PharVersion() {
        throw new AssertionError();
    }

    public static byte[] getVersionNibbles(final String version) throws NumberFormatException {
        String[] splitted = version.split("\\.");
        if (splitted.length != 3) {
            throw new IllegalArgumentException("Version must contains 3 parts");
        }

        String[] hex = new String[4];

        for (int i = 0; i < splitted.length; i++) {
            int versionPartInt = Integer.parseInt(splitted[i]);
            if (versionPartInt < 0) {
                throw new NumberFormatException("Version cannot contains negative numbers");
            }
            if (versionPartInt > 15) {
                throw new NumberFormatException("Version cannot contains part over 15");
            }

            hex[i] = Integer.toHexString(versionPartInt);
        }

        hex[3] = "0"; // last nibble is not used

        HexBinaryAdapter adapter = new HexBinaryAdapter();

        byte[] nibbles = new byte[2];
        nibbles[0] = adapter.unmarshal(hex[0] + hex[1])[0];
        nibbles[1] = adapter.unmarshal(hex[2] + hex[3])[0];

        return nibbles;
    }

}
