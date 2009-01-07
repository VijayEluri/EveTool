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

package uk.co.md87.evetool.api.wrappers;

import java.util.ArrayList;
import java.util.Map;

import uk.co.md87.evetool.api.parser.ApiElement;
import uk.co.md87.evetool.api.wrappers.data.BasicCharInfo;
import uk.co.md87.evetool.api.wrappers.data.BasicCorpInfo;

/**
 *
 * TODO: Document CharacterList
 * @author chris
 */
public class CharacterList extends ArrayList<BasicCharInfo> {

    public CharacterList(final ApiElement resultElement) {
        super();

        for (ApiElement child : resultElement.getChild("rowset").getChildren()) {
            add(getCharInfo(child));
        }
    }

    protected BasicCharInfo getCharInfo(final ApiElement row) {
        final Map<String, String> attr = row.getAttributes();
        final BasicCorpInfo corpInfo = new BasicCorpInfo(attr.get("corporationName"),
                Integer.parseInt(attr.get("corporationID")));
        final BasicCharInfo charInfo = new BasicCharInfo(attr.get("name"),
                Integer.parseInt(attr.get("characterID")), corpInfo);

        return charInfo;
    }

}
