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

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 * @see http://www.php.net/manual/en/phar.fileformat.stub.php
 */
public final class PharStub implements PharWritable {

    private final String stubCode;

    public PharStub(final String stubCode) {
        this.stubCode = stubCode;
    }

    @Override
    public void write(final PharOutputStream out) throws IOException {
        out.writeString("<?php ");
        if (this.stubCode != null) {
            out.writeString(this.stubCode);
        }
        out.writeString("__HALT_COMPILER(); ?>");
    }


}
