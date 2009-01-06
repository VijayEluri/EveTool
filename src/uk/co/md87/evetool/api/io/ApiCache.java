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

    public static enum ApiCacheStatus {
        MISS,
        EXPIRED,
        CACHED;
    }

    void setCache(final String method, final Map<String, String> args,
            final String data, final long cacheUntil);

    ApiCacheStatus getCacheStatus(final String method, final Map<String, String> args);

    String getCache(final String method, final Map<String, String> args);

}
