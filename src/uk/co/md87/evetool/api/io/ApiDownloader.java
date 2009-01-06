/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.md87.evetool.api.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author chris
 */
public class ApiDownloader {
    
    private String userID = null;
    private String charID = null;
    private String apiKey = null;

    public ApiDownloader() {
    }

    public ApiDownloader(final String userID, final String apiKey) {
        this.userID = userID;
        this.apiKey = apiKey;
    }

    public ApiDownloader(final String userID, final String apiKey, final String charID) {
        this.userID = userID;
        this.apiKey = apiKey;
        this.charID = charID;
    }

    public String getPage(final String method, final Map<String, String> args)
            throws IOException {
        final Map<String, String> ourArgs = new HashMap<String, String>(args);
        // TODO: Caching

        if (userID != null) {
            ourArgs.put("userID", userID);
        }

        if (apiKey != null) {
            ourArgs.put("apiKey", apiKey);
        }

        if (charID != null) {
            ourArgs.put("characterID", charID);
        }

        final StringBuilder builder = new StringBuilder();
        for (String line : Downloader.getPage(method, ourArgs)) {
            builder.append(line);
        }

        return builder.toString();
    }

}
