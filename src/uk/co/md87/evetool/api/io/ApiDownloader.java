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

import org.jdom.JDOMException;
import uk.co.md87.evetool.api.io.ApiCache.CacheStatus;
import uk.co.md87.evetool.api.parser.ApiParser;
import uk.co.md87.evetool.api.parser.ApiResult;
import uk.co.md87.evetool.api.parser.ParserException;

/**
 *
 * TODO: Document
 * @author chris
 */
public class ApiDownloader {

    private static final Logger LOGGER = Logger.getLogger(ApiDownloader.class.getName());

    private static final String API_HOST = "http://api.eve-online.com";

    private final ApiCache cache;
    private final ApiParser parser;

    private String userID = null;
    private String charID = null;
    private String apiKey = null;

    public ApiDownloader(final ApiCache cache, final ApiParser parser) {
        this.cache = cache;
        this.parser = parser;
    }

    public ApiDownloader(final ApiCache cache, final ApiParser parser,
            final String userID, final String apiKey) {
        this(cache, parser);
        this.userID = userID;
        this.apiKey = apiKey;
    }

    public ApiDownloader(final ApiCache cache, final ApiParser parser,
            final String userID, final String apiKey, final String charID) {
        this(cache, parser, userID, apiKey);
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

    public ApiResult getPage(final String method, final Map<String, String> args) {
        final Map<String, String> ourArgs = new HashMap<String, String>(args);
        addArgs(ourArgs);

        final CacheStatus cacheStatus = cache.getCacheStatus(method, args);

        if (cacheStatus == CacheStatus.MISS || cacheStatus == CacheStatus.EXPIRED) {
            try {
                final String page = Downloader.getPage(getUrl(method), ourArgs);
                final ApiResult res = parser.parseResult(page);
                cache.setCache(method, args, page, res.getCachedUntil().getTime());

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
            return parser.parseResult(cache.getCache(method, args).getData());
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, "Error processing cached API result", ex);
        } catch (JDOMException ex) {
            LOGGER.log(Level.WARNING, "Error parsing cached API result", ex);
        } catch (ParserException ex) {
            LOGGER.log(Level.WARNING, "Error parsing cached API result", ex);
        }

        return null;
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
