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
import uk.co.md87.evetool.api.parser.ApiElement;
import uk.co.md87.evetool.api.wrappers.data.Order;

/**
 * A list of market orders.
 *
 * @author chris
 */
public class MarketOrders extends ArrayList<Order> {

    /**
     * A version number for this class. It should be changed whenever the class
     * structure is changed (or anything else that would prevent serialized
     * objects being unserialized with the new class).
     */
    private static final long serialVersionUID = 10;

    public MarketOrders(final ApiElement resultElement) {
        super();

        double total = 0;

        for (ApiElement row : resultElement.getRowset("orders")) {
            if (row.getNumericAttribute("bid") == 0) {
                // Sell order
                final double value = row.getNumericAttribute("volRemaining")
                        * Double.parseDouble(row.getStringAttribute("price"));
                total += value;
            }
        }

        System.out.println(total);
    }

}
