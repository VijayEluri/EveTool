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

package uk.co.md87.evetool.api.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom.JDOMException;

import uk.co.md87.evetool.api.io.ApiCache.CacheStatus;
import uk.co.md87.evetool.api.parser.ApiParser;
import uk.co.md87.evetool.api.parser.ApiResult;
import uk.co.md87.evetool.api.parser.ParserException;

/**
 *
 * TODO: Document ApiDownloader
 * @author chris
 */
public class ApiDownloader {

    private static final Logger LOGGER = Logger.getLogger(ApiDownloader.class.getName());

    private static final String API_HOST = "http://api.eve-online.com";

    private final ApiCache cache;
    private final ApiParser parser;

    public ApiDownloader(final ApiCache cache, final ApiParser parser) {
        this.cache = cache;
        this.parser = parser;
    }

    public synchronized ApiResult getPage(final String method, final Map<String, String> args) {
        final Map<String, String> ourArgs = new HashMap<String, String>();

        if (args != null) {
            ourArgs.putAll(args);
        }

        final CacheStatus cacheStatus = cache.getCacheStatus(method, ourArgs);

        if (cacheStatus == CacheStatus.MISS || cacheStatus == CacheStatus.EXPIRED) {
            try {
                final String page = Downloader.getPage(getUrl(method), ourArgs);
                final ApiResult res = parser.parseResult(page);
                
                cache.setCache(method, ourArgs, page, res.getCachedUntil().getTime());
                // TODO: Time should be converted from GMT

                return res;
            } catch (IOException ex) {
                LOGGER.log(Level.WARNING, "API request failed", ex);
            } catch (JDOMException ex) {
                LOGGER.log(Level.WARNING, "Error parsing API result", ex);
            } catch (ParserException ex) {
                LOGGER.log(Level.WARNING, "Error parsing API result", ex);
            }
        }

        try {
            return parser.parseResult(cache.getCache(method, ourArgs).getData());
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, "Error processing cached API result", ex);
        } catch (JDOMException ex) {
            LOGGER.log(Level.WARNING, "Error parsing cached API result", ex);
        } catch (ParserException ex) {
            LOGGER.log(Level.WARNING, "Error parsing cached API result", ex);
        }

        return null;
    }

    protected static String getUrl(final String method) {
        return API_HOST + method;
    }

}
