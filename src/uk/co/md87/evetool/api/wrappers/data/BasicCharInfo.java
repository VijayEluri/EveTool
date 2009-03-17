/*
 * Copyright (c) 2009 Chris Smith
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package uk.co.md87.evetool.api.wrappers.data;

import uk.co.md87.evetool.api.listable.Retrievable;

/**
 *
 * TODO: Document BasicCharInfo
 * @author chris
 */
public class BasicCharInfo {

    private final String name;
    private final int id;
    private final BasicCorpInfo corp;

    public BasicCharInfo(final String name, final int id, final BasicCorpInfo corp) {
        this.name = name;
        this.id = id;
        this.corp = corp;
    }

    @Retrievable(deferred=true, name="Corporation")
    public BasicCorpInfo getCorp() {
        return corp;
    }

    @Retrievable
    public int getId() {
        return id;
    }

    @Retrievable
    public String getName() {
        return name;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "[" + name + " (" + id + ") in " + corp + "]";
    }

}
