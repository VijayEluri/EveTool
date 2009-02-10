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

package uk.co.md87.evetool.ui.data;

import uk.co.md87.evetool.api.wrappers.CharacterSheet;
import uk.co.md87.evetool.api.wrappers.data.BasicShipInfo;
import uk.co.md87.evetool.ui.listable.ListableImpl;
import uk.co.md87.evetool.ui.listable.Retrievable;

/**
 *
 * TODO: Document BasicShipInfoSurrogate
 * @author chris
 */
public class BasicShipInfoSurrogate extends ListableImpl {

    private final BasicShipInfo info;
    private final CharacterSheet sheet;

    public BasicShipInfoSurrogate(final BasicShipInfo info, final CharacterSheet sheet) {
        this.info = info;
        this.sheet = sheet;
    }

    @Retrievable(deferred=true)
    public BasicShipInfo getInfo() {
        return info;
    }

    @Retrievable
    public boolean canFly() {
        return sheet.hasSkills(info.getRequirements());
    }

}
