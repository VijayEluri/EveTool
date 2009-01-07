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

package uk.co.md87.evetool.api.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * TODO: Document
 * @author chris
 */
public class ApiElement {

    private String content = null;
    private List<ApiElement> children = new ArrayList<ApiElement>();
    private Map<String, String> attributes = new HashMap<String, String>();

    public void addChild(final ApiElement e) {
        children.add(e);
    }

    public void addAttribute(final String key, final String value) {
        attributes.put(key, value);
    }

    public List<ApiElement> getChildren() {
        return children;
    }
    
    public Map<String, String> getAttributes() {
        return attributes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public NamedApiElement getChild(final String name) {
        for (ApiElement child : getChildren()) {
            if (child instanceof NamedApiElement
                    && name.equals(((NamedApiElement) child).getName())) {
                return (NamedApiElement) child;
            }
        }

        return null;
    }

    public ApiElement getRowset(final String name) {
        for (ApiElement child : getChildren()) {
            if (child instanceof NamedApiElement
                    && "rowset".equals(((NamedApiElement) child).getName())
                    && name.equals(child.getAttributes().get("name"))) {
                return child;
            }
        }

        return null;
    }

}
