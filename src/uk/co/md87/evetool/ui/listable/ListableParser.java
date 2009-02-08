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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private final Map<String, List<Method>> methods = new HashMap<String, List<Method>>();

    public ListableParser(final Class<? extends Listable> target) {
        parse(target, new ArrayList<Method>(), "");
    }

    protected void parse(final Class<?> target, final List<Method> methodChain,
            final String prefix) {
        for (Method method : target.getMethods()) {
            final Retrievable ann = method.getAnnotation(Retrievable.class);

            if (ann != null) {
                final List<Method> newMethodChain = new ArrayList<Method>(methodChain);
                newMethodChain.add(method);
                
                if (ann.deferred()) {
                    if (ann.name().isEmpty()) {
                        parse(method.getReturnType(), newMethodChain, prefix);
                    } else if (prefix.isEmpty()) {
                        parse(method.getReturnType(), newMethodChain, ann.name());
                    } else {
                        parse(method.getReturnType(), newMethodChain, prefix + " "
                                + ann.name());
                    }
                } else {
                    final String name = (prefix.isEmpty() ? prefix : prefix + " ") +
                            (ann.name().isEmpty() ? getName(method.getName()) : ann.name());

                    methods.put(name.toLowerCase(), newMethodChain);
                }
            }
        }
    }

    protected String getName(final String methodName) {
        final StringBuilder builder = new StringBuilder();
        boolean lastCap = false;

        final char[] chrs = (methodName.startsWith("get") ? methodName.substring(3)
                : methodName).toCharArray();

        for (int i = 0; i < chrs.length; i++) {
            final char ch = chrs[i];
            
            if (Character.isUpperCase(ch) && builder.length() > 0 &&
                    (!lastCap || (i < chrs.length - 1 && !Character.isUpperCase(chrs[i + 1])))) {
                builder.append(' ');
            }

            builder.append(Character.toLowerCase(ch));

            lastCap = Character.isUpperCase(ch);
        }

        System.out.println(methodName + " ==> " + builder.toString());
        return builder.toString();
    }

    public Set<String> getRetrievableNames() {
        return methods.keySet();
    }

    public String getValue(final Object target, final String name) {
        if (methods.containsKey(name)) {
            Object result = target;

            try {
                for (Method method : methods.get(name)) {
                    result = method.invoke(result);
                }

                final Class<?> formatter = methods.get(name)
                        .get(methods.get(name).size() - 1)
                        .getAnnotation(Retrievable.class).formatWith();

                return (String) formatter.getMethod("getValue", Object.class)
                        .invoke(null, result);
            } catch (IllegalAccessException ex) {
                LOGGER.log(Level.SEVERE, "Unable to retrieve value", ex);
            } catch (IllegalArgumentException ex) {
                LOGGER.log(Level.SEVERE, "Unable to retrieve value", ex);
            } catch (InvocationTargetException ex) {
                LOGGER.log(Level.SEVERE, "Unable to retrieve value", ex);
            } catch (NoSuchMethodException ex) {
                LOGGER.log(Level.SEVERE, "Unable to retrieve value", ex);
            }
        }

        return "Unknown";
    }

}
