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

import java.util.Map;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public final class PHPSerializer {

    /**
     * Utility class, can't instantiate.
     */
    private PHPSerializer() {
        throw new AssertionError();
    }

    public static String serialize(final Map<String, String> map) {
        final StringBuilder serialized = new StringBuilder();

        // map is serialized as a PHP associative array
        serialized.append("a:").append(map.size()).append(":{");
        for (Map.Entry<String, String> attribute : map.entrySet()) {

            // serialize key as string
            serialized.append(serialize(attribute.getKey()));

            // serialize value as string
            serialized.append(serialize(attribute.getValue()));
        }
        serialized.append("}");
        return serialized.toString();
    }

    public static String serialize(final String s) {
        StringBuilder serialized = new StringBuilder();
        serialized.append("s:").append(s.length()).append(":\"").append(s).append("\";");
        return serialized.toString();
    }

}
