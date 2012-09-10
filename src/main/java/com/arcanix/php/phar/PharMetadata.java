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

import java.io.IOException;
import java.util.Map;

import com.arcanix.php.phar.util.PHPSerializer;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public final class PharMetadata implements PharWritable {

    private final Map<String, String> metadata;

    public PharMetadata(final Map<String, String> metadata) {
        if (metadata == null) {
            throw new IllegalArgumentException("Metadata cannot be null");
        }
        this.metadata = metadata;
    }

    @Override
    public void write(PharOutputStream out) throws IOException {
        byte[] metadataBytes = new byte[0];
        if (!this.metadata.isEmpty()) {
            metadataBytes = PHPSerializer.serialize(this.metadata).getBytes("UTF-8");
        }
        out.writeInt(metadataBytes.length);
        out.write(metadataBytes);
    }

}
