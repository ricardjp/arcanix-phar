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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public final class FilePharEntryProvider implements PharEntryProvider {

    private final File file;
    private final String localPath;
    private final PharCompression pharCompression;

    public FilePharEntryProvider(final File file, final String localPath, final PharCompression pharCompression) throws FileNotFoundException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        if (!file.isFile()) {
            throw new IllegalArgumentException("File must be a valid file and cannot be a directory");
        }
        if (StringUtils.isEmpty(localPath)) {
            throw new IllegalArgumentException("Local path cannot be empty");
        }
        if (pharCompression == null) {
            throw new IllegalArgumentException("Phar compression cannot be null");
        }
        this.file = file;
        this.localPath = localPath;
        this.pharCompression = pharCompression;
    }

    @Override
    public List<PharEntry> getPharEntries() throws IOException {
        List<PharEntry> pharEntries = new LinkedList<>();
        pharEntries.add(new PharEntry(this.file, this.localPath, this.pharCompression));
        return pharEntries;
    }

}
