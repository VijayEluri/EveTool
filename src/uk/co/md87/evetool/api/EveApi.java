/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

/**
 *
 * @author chris
 */
public class EveApi {

    private static final String[] TABLES = {"PageCache"};

    private static final Logger LOGGER = Logger.getLogger(EveApi.class.getName());

    private final Connection conn;
    private final ApiDownloader downloader;
    private String userID;
    private String charID;
    private String apiKey;

    public EveApi(final Connection sqlConnection) {
        this.conn = sqlConnection;
        checkTables();

        this.downloader = new ApiDownloader(new DBCache(conn));
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
        downloader.setApiKey(apiKey);
    }

    public void setCharID(String charID) {
        this.charID = charID;
        downloader.setCharID(charID);
    }

    public void setUserID(String userID) {
        this.userID = userID;
        downloader.setUserID(userID);
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
