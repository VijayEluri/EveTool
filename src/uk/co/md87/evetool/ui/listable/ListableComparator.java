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

import java.util.Comparator;
import java.util.List;
import uk.co.md87.evetool.ui.listable.ListableConfig.ConfigElement;

/**
 *
 * TODO: Document ListableComparator
 * @author chris
 */
public class ListableComparator implements Comparator<Object> {

    private final ListableConfig.ConfigElement[] order;
    private final ListableParser parser;

    public ListableComparator(ConfigElement[] order, ListableParser parser) {
        this.order = order;
        this.parser = parser;
    }

    public int compare(final Object o1, final Object o2) {
        for (ConfigElement element : order) {
            final Object op1 = element.getUnformattedValue(o1, parser);
            final Object op2 = element.getUnformattedValue(o2, parser);

            int res = doCompare(op1, op2);

            if (res != 0) {
                return res;
            }
        }

        return 0;
    }

    @SuppressWarnings("unchecked")
    protected int doCompare(Object op1, Object op2) {
        int res = 0;

        if (op1 instanceof Comparable) {
            res = ((Comparable) op1).compareTo(op2);
        } else if (op1 instanceof List) {
            final List<?> objs1 = (List<?>) op1;
            final List<?> objs2 = (List<?>) op2;

            for (int i = 0; i < objs1.size(); i++) {
                res = doCompare(objs1.get(i), objs2.get(i));

                if (res != 0) {
                    break;
                }
            }
        } else {
            res = String.valueOf(op1).compareTo(String.valueOf(op2));
        }

        return res;
    }

}
