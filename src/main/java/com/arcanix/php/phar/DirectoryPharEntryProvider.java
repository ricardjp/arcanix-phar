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
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public class DirectoryPharEntryProvider implements PharEntryProvider {

    private final Path rootPath;
    private final String localPath;
    private final PharCompression pharCompression;

    public DirectoryPharEntryProvider(final File directory, final String localPath, final PharCompression pharCompression) {
        if (directory == null) {
            throw new IllegalArgumentException("Directory cannot be null");
        }
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Directory must be a valid directory");
        }
        if (StringUtils.isEmpty(localPath)) {
            throw new IllegalArgumentException("Local path cannot be empty");
        }
        if (pharCompression == null) {
            throw new IllegalArgumentException("Phar compression cannot be null");
        }
        this.rootPath = directory.toPath();
        this.localPath = localPath;
        this.pharCompression = pharCompression;
    }

    @Override
    public List<PharEntry> getPharEntries() throws IOException {
        List<PharEntry> pharEntries = new ArrayList<PharEntry>();
        addPharEntriesRecursively(pharEntries, this.rootPath);
        return pharEntries;

    }

    private void addPharEntriesRecursively(final List<PharEntry> pharEntries, final Path directory) throws IOException {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory)) {
            for (Path element : directoryStream) {
                File file = element.toFile();
                if (file.isDirectory()) {
                    addPharEntriesRecursively(pharEntries, element);
                } else {
                    String relativePath = this.rootPath.relativize(element).toString();
                    pharEntries.add(new PharEntry(file, this.localPath + "/" + relativePath, this.pharCompression));
                }
            }
        }
    }

}
