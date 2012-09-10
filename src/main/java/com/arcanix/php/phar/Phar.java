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
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public final class Phar {

    public static final String PHAR_VERSION = "1.1.0";

    private final File file;
    private final PharCompression pharCompression;

    private String stubCode;

    private final List<PharEntry> entries = new ArrayList<>();

    private final Map<String, String> metadata = new HashMap<>();

    public Phar(final File file) {
        this(file, PharCompression.GZIP);
    }

    public Phar(final File file, final PharCompression pharCompression) {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        if (pharCompression == null) {
            throw new IllegalArgumentException("Phar compression cannot be null");
        }
        this.file = file;
        this.pharCompression = pharCompression;
    }

    /**
     * Adds an entry to be stored in the phar archive.
     */
    public void add(final File file) throws IOException {
        add(file, file.getName());
    }

    /**
     * Adds an entry to be stored in the phar archive.
     */
    public void add(final File file, final String localPath) throws IOException {
        if (file.isDirectory()) {
            add(new DirectoryPharEntryProvider(file, localPath, this.pharCompression));
        } else {
            add(new FilePharEntryProvider(file, localPath, this.pharCompression));
        }
    }

    private void add(final PharEntryProvider pharEntryProvider) throws IOException {
        for (PharEntry pharEntry : pharEntryProvider.getPharEntries()) {
            this.entries.add(pharEntry);
        }
    }

    public void setMetadata(final String key, final String value) {
        this.metadata.put(key, value);
    }

    public void setStub(final String stubCode) {
        this.stubCode = stubCode;
    }

    public void write() throws IOException, NoSuchAlgorithmException {
        PharOutputStream out = null;
        try {
            out = new PharOutputStream(new FileOutputStream(this.file));

            // write stub
            out.write(new PharStub(this.stubCode));

            byte[] pharFiles = writePharEntriesManifest();

            PharManifest pharManifest = new PharManifest(
                    this.file, pharFiles, this.entries, new PharMetadata(this.metadata));

            out.write(pharManifest);
            out.write(pharFiles);

            for (PharEntry pharEntry : this.entries) {
                out.write(pharEntry.getPharEntryData());
            }

            // flush to file before creating signature
            out.flush();

            // writing signature
            out.write(new PharSignature(this.file, PharSignatureType.SHA512));

            out.flush();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private byte[] writePharEntriesManifest() throws IOException {
        PharOutputStream out = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            out = new PharOutputStream(byteArrayOutputStream);
            for (PharEntry pharEntry : this.entries) {
                out.write(pharEntry.getPharEntryManifest());
            }
            out.flush();
            return byteArrayOutputStream.toByteArray();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }


}
