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

package uk.co.md87.evetool.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import uk.co.md87.evetool.api.io.ApiDownloader;
import uk.co.md87.evetool.api.io.DBCache;
import uk.co.md87.evetool.api.parser.ApiElement;
import uk.co.md87.evetool.api.parser.ApiParser;
import uk.co.md87.evetool.api.parser.ApiResult;
import uk.co.md87.evetool.api.wrappers.CharacterList;
import uk.co.md87.evetool.api.wrappers.SkillInTraining;

/**
 *
 * TODO: Document
 * @author chris
 */
public class EveApi {

    private static final String[] TABLES = {"PageCache"};

    private static final Logger LOGGER = Logger.getLogger(EveApi.class.getName());

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final Connection conn;
    private final ApiDownloader downloader;
    private String userID;
    private String charID;
    private String apiKey;

    public EveApi(final Connection sqlConnection) {
        this.conn = sqlConnection;
        checkTables();

        this.downloader = new ApiDownloader(new DBCache(conn), new ApiParser());
    }

    public void setApiKey(final String apiKey) {
        this.apiKey = apiKey;
        downloader.setApiKey(apiKey);
    }

    public void setCharID(final String charID) {
        this.charID = charID;
        downloader.setCharID(charID);
    }

    public void setUserID(final String userID) {
        this.userID = userID;
        downloader.setUserID(userID);
    }

    public ApiResponse<CharacterList> getCharacterList() {
        return getResponse("/account/Characters.xml.aspx", CharacterList.class, true, false);
    }

    public ApiResponse<SkillInTraining> getSkillInTraining() {
        return getResponse("/char/SkillInTraining.xml.aspx", SkillInTraining.class, true, true);
    }

    protected <T> ApiResponse<T> getResponse(final String method, final Class<T> type,
            final boolean needKey, final boolean needChar) {
        // TODO: Require userid + apikey
        final ApiResult result = downloader.getPage(method, null);

        if (result.wasSuccessful()) {
            try {
                return new ApiResponse<T>(type.getConstructor(ApiElement.class)
                        .newInstance(result.getResultElement()), result);
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Unable to create response object", ex);
            }

            return null;
        } else {
            return new ApiResponse<T>(result.getError(), result);
        }
    }

    // TODO: Abstract db maintenance
    // TODO: Version tables somehow
    protected void createTable(final String table) {
        LOGGER.log(Level.FINE, "Creating table " + table);
        final StringBuilder sql = new StringBuilder();
        final BufferedReader stream = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream("db/" + table.toLowerCase() + ".sql")));
        String line;

        try {
            do {
                line = stream.readLine();

                if (line != null) {
                    sql.append(line);
                }
            } while (line != null);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error when reading SQL for table: " + table, ex);
            return;
        }
        
        try {
            conn.createStatement().execute(sql.toString());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error creating table: " + table, ex);
        }
    }

    protected void checkTables() {
        LOGGER.log(Level.FINEST, "Checking that tables exist");

        final List<String> tables = getTables();

        for (String table : TABLES) {
            if (!tables.contains(table.toUpperCase())) {
                createTable(table);
            }
        }

        LOGGER.log(Level.FINEST, "Done checking tables");
    }

    protected List<String> getTables() {
        final List<String> tables = new ArrayList<String>();

        try {
            final ResultSet rs = conn.getMetaData().getTables(null, null, null, null);

            while (rs.next()) {
                tables.add(rs.getString("TABLE_NAME"));
            }

            rs.close();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQL Error when checking for tables", ex);
        }

        return tables;
    }

}
