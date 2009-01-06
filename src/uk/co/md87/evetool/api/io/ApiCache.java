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

import java.util.Map;

/**
 *
 * TODO: Document
 * @author chris
 */
public interface ApiCache {

    public static enum CacheStatus {
        MISS,
        EXPIRED,
        HIT;
    }

    public static class CacheResult {

        private final String data;
        private final long cachedAt;
        private final long cachedUntil;

        public CacheResult(String data, long cachedAt, long cachedUntil) {
            this.data = data;
            this.cachedAt = cachedAt;
            this.cachedUntil = cachedUntil;
        }

        public long getCachedAt() {
            return cachedAt;
        }

        public long getCachedUntil() {
            return cachedUntil;
        }

        public String getData() {
            return data;
        }
        
    }

    void setCache(final String method, final Map<String, String> args,
            final String data, final long cacheUntil);

    CacheStatus getCacheStatus(final String method, final Map<String, String> args);

    CacheResult getCache(final String method, final Map<String, String> args);

}
