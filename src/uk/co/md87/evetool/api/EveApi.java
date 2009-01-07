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
import uk.co.md87.evetool.api.wrappers.CharacterSheet;
import uk.co.md87.evetool.api.wrappers.SkillInTraining;
import uk.co.md87.evetool.api.wrappers.SkillList;

/**
 * Allows access to the EVE Api (see http://api.eve-online.com/).
 *
 * @author chris
 */
public class EveApi {

    /** SQL tables required by the API. */
    private static final String[] TABLES = {"PageCache"};

    /** Logger to use for this class. */
    private static final Logger LOGGER = Logger.getLogger(EveApi.class.getName());

    /** Date format for dates returned by the API. */
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /** The database connection to use. */
    private final Connection conn;

    /** The downloader to use. */
    private final ApiDownloader downloader;
    
    /** The client's user ID, if specified. */
    private String userID;

    /** The client's character ID, if specified. */
    private String charID;

    /** The client's API key, if specified. */
    private String apiKey;

    /**
     * Creates a new instance of the EVE API client using the specified database
     * connection.
     *
     * @param sqlConnection A connection to a database to use
     */
    public EveApi(final Connection sqlConnection) {
        this.conn = sqlConnection;
        checkTables();

        this.downloader = new ApiDownloader(new DBCache(conn), new ApiParser());
    }

    /**
     * Sets the API key used by this client.
     *
     * @param apiKey The user's API key
     */
    public void setApiKey(final String apiKey) {
        this.apiKey = apiKey;
        downloader.setApiKey(apiKey);
    }

    /**
     * Sets the character ID used by this client.
     *
     * @param charID The user's chosen character ID
     */
    public void setCharID(final String charID) {
        this.charID = charID;
        downloader.setCharID(charID);
    }

    /**
     * Sets the user ID used by this client.
     *
     * @param userID The user's user ID
     */
    public void setUserID(final String userID) {
        this.userID = userID;
        downloader.setUserID(userID);
    }

    /**
     * Retrieves the character list for the user's account.
     * Requires a limited API key and user ID.
     *
     * @return The user's character list
     */
    public ApiResponse<CharacterList> getCharacterList() {
        return getResponse("/account/Characters.xml.aspx", CharacterList.class, true, false);
    }

    /**
     * Retrieves the character sheet for the specified character.
     * Requires a limited API key, user ID and character ID.
     *
     * @return The character's extended information
     */
    public ApiResponse<CharacterSheet> getCharacterSheet() {
        return getResponse("/char/CharacterSheet.xml.aspx", CharacterSheet.class, true, true);
    }

    /**
     * Retrieves the skill that's currently in training.
     * Requires a limited API key, user ID and character ID.
     *
     * @return The character's currently training skill
     */
    public ApiResponse<SkillInTraining> getSkillInTraining() {
        return getResponse("/char/SkillInTraining.xml.aspx", SkillInTraining.class, true, true);
    }

    /**
     * Retrieves the full skill tree for EVE.
     * Does not require an API key.
     *
     * @return The complete EVE skill tree
     */
    public ApiResponse<SkillList> getSkillTree() {
        return getResponse("/eve/SkillTree.xml.aspx ", SkillList.class, false, true);
    }

    /**
     * Utility method to send a request to the API and manufacture the
     * appropaite response objects.
     *
     * @param <T> The type of object that will be returned on success
     * @param method The method (path) of the API being used
     * @param type The class of object to return on success
     * @param needKey Whether or not an API key is needed
     * @param needChar Whether or not a character ID is needed
     * @return An appropriate ApiResponse encapsulating the request
     */
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
    /**
     * Creates the table with the specified name. SQL is read from the
     * <code>db</code> package.
     *
     * @param table The table to be created
     */
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

    /**
     * Checks to ensure that all required tables exist. If any table is missing,
     * it will be created.
     *
     * @see #createTable(java.lang.String)
     */
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

    /**
     * Retrieves a list of tables that exist in the database.
     *
     * @return A list of table names that exist
     */
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
