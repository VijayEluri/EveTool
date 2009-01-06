/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.md87.evetool.api.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import uk.co.md87.evetool.api.io.ApiCache.ApiCacheStatus;

/**
 *
 * TODO: Document
 * @author chris
 */
public class ApiDownloader {

    private static final Logger LOGGER = Logger.getLogger(ApiDownloader.class.getName());

    private static final String API_HOST = "http://api.eve-online.com";

    private final ApiCache cache;

    private String userID = null;
    private String charID = null;
    private String apiKey = null;

    public ApiDownloader(final ApiCache cache) {
        this.cache = cache;
    }

    public ApiDownloader(final ApiCache cache, final String userID, final String apiKey) {
        this(cache);
        this.userID = userID;
        this.apiKey = apiKey;
    }

    public ApiDownloader(final ApiCache cache, final String userID,
            final String apiKey, final String charID) {
        this(cache, userID, apiKey);
        this.charID = charID;
    }

    public String getPage(final String method, final Map<String, String> args) {
        final Map<String, String> ourArgs = new HashMap<String, String>(args);

        // TODO: Abstract
        if (userID != null) {
            ourArgs.put("userID", userID);
        }

        if (apiKey != null) {
            ourArgs.put("apiKey", apiKey);
        }

        if (charID != null) {
            ourArgs.put("characterID", charID);
        }

        final ApiCacheStatus cacheStatus = cache.getCacheStatus(method, args);

        if (cacheStatus == ApiCacheStatus.CACHED) {
            return cache.getCache(method, args);
        }

        try {
            final StringBuilder builder = new StringBuilder();
            for (String line : Downloader.getPage(getUrl(method), ourArgs)) {
                builder.append(line);
            }

            cache.setCache(method, args, builder.toString(),
                    System.currentTimeMillis() + 20000); // TODO: Proper time
            return builder.toString();
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, "API request failed", ex);
            if (cacheStatus == ApiCacheStatus.EXPIRED) {
                return cache.getCache(method, args);
            } else {
                return null;
            }
        }
    }

    protected static String getUrl(final String method) {
        return API_HOST + method;
    }

}
