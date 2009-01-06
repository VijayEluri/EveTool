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

import uk.co.md87.evetool.api.io.ApiCache.CacheStatus;
import uk.co.md87.evetool.api.parser.ApiResult;

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

    protected void addArgs(final Map<String, String> args) {
        if (userID != null) {
            args.put("userID", userID);
        }

        if (apiKey != null) {
            args.put("apiKey", apiKey);
        }

        if (charID != null) {
            args.put("characterID", charID);
        }
    }

    public ApiCache.CacheResult getPage(final String method, final Map<String, String> args) {
        final Map<String, String> ourArgs = new HashMap<String, String>(args);
        addArgs(ourArgs);

        // TODO: Refactor to avoid duplicate gets

        final CacheStatus cacheStatus = cache.getCacheStatus(method, args);

        if (cacheStatus == CacheStatus.MISS || cacheStatus == CacheStatus.EXPIRED) {
            try {
                cache.setCache(method, args, Downloader.getPage(getUrl(method), ourArgs),
                        System.currentTimeMillis() + 20000); // TODO: Proper time
            } catch (IOException ex) {
                LOGGER.log(Level.WARNING, "API request failed", ex);
            }
        }

        return cache.getCache(method, args);
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setCharID(String charID) {
        this.charID = charID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    protected static String getUrl(final String method) {
        return API_HOST + method;
    }

}
