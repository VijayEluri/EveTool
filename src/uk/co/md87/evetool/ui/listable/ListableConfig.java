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

package uk.co.md87.evetool.ui.listable;

/**
 *
 * @author chris
 */
public class ListableConfig {

    public ConfigElement topLeft, topRight, bottomLeft, bottomRight;
    public String[] sortOrder;
    public ConfigElement group;

    public static interface ConfigElement {

        String getValue(final Object target, final ListableParser parser);

    }

    public static class BasicConfigElement implements ConfigElement {

        private final String name;

        public BasicConfigElement(String name) {
            this.name = name;
        }

        public String getValue(final Object target, ListableParser parser) {
            return parser.getValue(target, name);
        }

        public String getName() {
            return name;
        }
    }

    public static class LiteralConfigElement implements ConfigElement {

        private final String text;

        public LiteralConfigElement(String text) {
            this.text = text;
        }

        public String getValue(Object target, ListableParser parser) {
            return text;
        }

        public String getText() {
            return text;
        }
    }

    public static class CompoundConfigElement implements ConfigElement {

        private final ConfigElement[] elements;

        public CompoundConfigElement(ConfigElement ... elements) {
            this.elements = elements;
        }

        public String getValue(Object target, ListableParser parser) {
            final StringBuilder builder = new StringBuilder();

            for (ConfigElement element : elements) {
                builder.append(element.getValue(target, parser));
            }

            return builder.toString();
        }

        public ConfigElement[] getElements() {
            return elements;
        }
    }

}
