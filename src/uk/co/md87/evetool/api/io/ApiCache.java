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
 * Provides methods of accessing and updating a cache for API requests.
 *
 * @author chris
 */
public interface ApiCache {

    /**
     * Describes the status of a cache request.
     */
    public static enum CacheStatus {
        /** The request missed (no cached result). */
        MISS,
        /** The request hit but the cached entry has expired. */
        EXPIRED,
        /** The request hit successfully. */
        HIT;
    }

    /**
     * Represents the result of a cache request.
     */
    public static class CacheResult {

        /** The actual cached data. */
        private final String data;

        /** The time the data was cached at (milliseconds since the epoch). */
        private final long cachedAt;

        /** The time the data was cached until (milliseconds since the epoch). */
        private final long cachedUntil;

        /**
         * Creates a new CacheResult object.
         *
         * @param data The cache data being returned
         * @param cachedAt The time at which the data was cached
         * @param cachedUntil The time at which the data will expire
         */
        public CacheResult(final String data, final long cachedAt, final long cachedUntil) {
            this.data = data;
            this.cachedAt = cachedAt;
            this.cachedUntil = cachedUntil;
        }

        /**
         * Retrieves the time that the data was cached at.
         *
         * @return This result's cached-at time
         */
        public long getCachedAt() {
            return cachedAt;
        }

        /**
         * Retrieves the time that the data is cached until.
         *
         * @return This result's cached-until time
         */
        public long getCachedUntil() {
            return cachedUntil;
        }

        /**
         * Retrieves the response to this request.
         *
         * @return The data previously stored in the cache
         */
        public String getData() {
            return data;
        }
        
    }

    /**
     * Updates the cache for the specified request to contain the specified
     * data until the specified time.
     * 
     * @param method The API method being requested
     * @param args The arguments passed to that method
     * @param data The data that has been returned
     * @param cacheUntil The time (milliseconds since the epoch) to cache until
     */
    void setCache(final String method, final Map<String, String> args,
            final String data, final long cacheUntil);

    /**
     * Checks the cache status of the specified request.
     *
     * @param method The API method that is being checked
     * @param args The arguments that will be passed to that method
     * @return A {@link CacheStatus} object describing the status of the cache
     */
    CacheStatus getCacheStatus(final String method, final Map<String, String> args);

    /**
     * Retrieves a cached API query. This method assumes that the cache has
     * previously been verified to contain the specified entry; if the cache
     * does not contain the entry, the behaviour of this method is undefined.
     *
     * @param method The API method requested
     * @param args Any arguments passed to that method
     * @return A {@link CacheResult} object describing the resulting data
     */
    CacheResult getCache(final String method, final Map<String, String> args);

}
