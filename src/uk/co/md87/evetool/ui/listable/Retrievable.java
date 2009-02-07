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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Designates that the return type of the method is retrievable for use in a
 * {@link Listable}.
 * 
 * @author chris
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Retrievable {

    /**
     * Indicates that the return type of the method should be scanned for
     * other retrievable methods, instead of just using the result directly.
     * If the {@link #name()} property is specified as well, the name is used
     * as a prefix for the methods found in the returned retrievable.
     * 
     * @return True if the return type should be scanned for 'deferred'
     * retrievables, false if the result should be used verbatim
     */
    boolean deferred() default false;

    /**
     * A custom name for the retrievable method. If no name is specified, it is
     * inferred from the method name by stripping prefixes such as 'get' and
     * inserting spaces before CamelCase words.
     * 
     * @return The custom name for the method
     */
    String name() default "";
    
}
