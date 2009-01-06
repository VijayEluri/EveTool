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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.md87.evetool.api.EveApi;

/**
 *
 * TODO: Document
 * @author chris
 */
public class ApiResult extends ApiElement {

    private static final Logger LOGGER = Logger.getLogger(ApiResult.class.getName());

    protected static final long ERROR_OFFSET = 600000;

    public boolean wasSuccessful() {
        return getChild("result") != null && getChild("error") == null;
    }

    public ApiElement getResultElement() {
        return getChild("result");
    }

    public String getError() {
        return getChild("error").getContent();
    }

    public Date getCachedSince() {
        final NamedApiElement ctElement = getChild("currentTime");

        if (ctElement == null) {
            return new Date(System.currentTimeMillis());
        } else {
            try {
                return new SimpleDateFormat(EveApi.DATE_FORMAT).parse(ctElement.getContent());
            } catch (ParseException ex) {
                LOGGER.log(Level.SEVERE, "Error parsing cached from date", ex);
                return new Date(System.currentTimeMillis());
            }
        }
    }

    public Date getCachedUntil() {
        final NamedApiElement cuElement = getChild("cachedUntil");

        if (cuElement == null) {
            return new Date(System.currentTimeMillis() + ERROR_OFFSET);
        } else {
            try {
                return new SimpleDateFormat(EveApi.DATE_FORMAT).parse(cuElement.getContent());
            } catch (ParseException ex) {
                LOGGER.log(Level.SEVERE, "Error parsing cached until date", ex);
                return new Date(System.currentTimeMillis() + ERROR_OFFSET);
            }
        }
    }

}
