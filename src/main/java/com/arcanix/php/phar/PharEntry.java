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
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.zip.CRC32;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public final class PharEntry {

    private final File file;
    private final String localPath;
    private final PharCompression pharCompression;

    private final CRC32 checksum;
    private final byte[] compressedBytes;

    public PharEntry(final File file, final String localPath, final PharCompression pharCompression) throws IOException {
        this.file = file;
        this.localPath = localPath;
        this.pharCompression = pharCompression;

        byte[] uncompressedBytes = Files.readAllBytes(this.file.toPath());
        this.checksum = new CRC32();
        this.checksum.update(uncompressedBytes);

        ByteArrayOutputStream compressed = new ByteArrayOutputStream();

        OutputStream compressor = null;
        try {
            compressor = getCompressorOutputStream(compressed);
            Files.copy(this.file.toPath(), compressor);
            compressor.flush();
        } finally {
            compressor.close();
        }

        this.compressedBytes = compressed.toByteArray();
    }

    private OutputStream getCompressorOutputStream(final OutputStream outputStream) throws IOException {
        switch (this.pharCompression) {
            case GZIP:
                return new GzipCompressorOutputStream(outputStream);
            case BZIP2:
                return new BZip2CompressorOutputStream(outputStream);
            default:
                throw new IllegalArgumentException("Compression is not supported: " + this.pharCompression.name());
        }
    }

    public PharWritable getPharEntryManifest() {
        return new PharWritable() {

            @Override
            public void write(PharOutputStream out) throws IOException {
                byte[] filenameBytes = PharEntry.this.localPath.getBytes("UTF-8");

                // write filename length
                out.writeInt(filenameBytes.length);

                // write filename
                out.write(filenameBytes);

                // write file size
                out.writeInt((int) PharEntry.this.file.length());

                // write file last modified date
                out.writeInt((int) (PharEntry.this.file.lastModified() / 1000));

                // write file compressed length
                out.writeInt((int) PharEntry.this.compressedBytes.length);

                // write file checksum value
                out.writeInt((int) PharEntry.this.checksum.getValue());

                // write bit-mapped file-specific flags
                out.write(PharEntry.this.pharCompression.getBitmapFlag());

                // write meta-data length
                out.writeInt(0);
            }
        };
    }

    public PharWritable getPharEntryData() {
        return new PharWritable() {

            @Override
            public void write(final PharOutputStream out) throws IOException {
                out.write(PharEntry.this.compressedBytes);
            }
        };
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("file", this.file.getAbsolutePath())
            .append("localPath", this.localPath)
            .toString();
    }

}
