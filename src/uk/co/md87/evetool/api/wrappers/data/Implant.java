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
 * TODO: Document Implant
 * @author chris
 */
public class Implant {

    private final String name;
    private final Attribute attribute;
    private final int bonus;

    public Implant(final String name, final Attribute attribute, final int bonus) {
        this.name = name;
        this.attribute = attribute;
        this.bonus = bonus;
    }

    @Retrievable
    public Attribute getAttribute() {
        return attribute;
    }

    @Retrievable
    public int getBonus() {
        return bonus;
    }

    @Retrievable
    public String getName() {
        return name;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "[" + name + " (+" + bonus + " " + attribute + ")]";
    }

}
