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

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class PharOutputStream extends FilterOutputStream {

    private static final String STRING_ENCODING = "UTF-8";

    public PharOutputStream(final OutputStream outputStream) {
        super(outputStream);
    }

    public void writeInt(final int i) throws IOException {
        this.out.write((i >>> 0) & 0xFF);
        this.out.write((i >>> 8) & 0xFF);
        this.out.write((i >>> 16) & 0xFF);
        this.out.write((i >>> 24) & 0xFF);
    }

    public void writeString(final String s) throws IOException {
        if (s == null) {
            throw new NullPointerException("String cannot be null");
        }
        this.out.write(s.getBytes(STRING_ENCODING));
    }

    public void write(final PharWritable writable) throws IOException {
        if (writable == null) {
            throw new NullPointerException("Writable cannot be null");
        }
        writable.write(this);
    }

}
