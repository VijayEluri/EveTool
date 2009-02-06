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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * TODO: Document ListableParser
 * @author chris
 */
public class ListableParser {

    private static final Logger LOGGER = Logger.getLogger(ListableParser.class.getName());

    private final Map<String, Method> methods = new HashMap<String, Method>();

    public ListableParser(final Class<? extends Listable> target) {
        for (Method method : target.getMethods()) {
            final Retrievable ann = method.getAnnotation(Retrievable.class);

            if (ann != null) {
                final String name = ann.name() == null ? getName(method.getName()) : ann.name();

                methods.put(name, method);
            }
        }
    }

    protected String getName(final String methodName) {
        return methodName;
    }

    public Set<String> getRetrievableNames() {
        return methods.keySet();
    }

    public String getValue(final Listable target, final String name) {
        if (methods.containsKey(name)) {
            try {
                final Object result = methods.get(name).invoke(target);

                return String.valueOf(result);
            } catch (IllegalAccessException ex) {
                LOGGER.log(Level.SEVERE, "Unable to retrieve value", ex);
            } catch (IllegalArgumentException ex) {
                LOGGER.log(Level.SEVERE, "Unable to retrieve value", ex);
            } catch (InvocationTargetException ex) {
                LOGGER.log(Level.SEVERE, "Unable to retrieve value", ex);
            }
        }

        return "Unknown";
    }

}
