/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
