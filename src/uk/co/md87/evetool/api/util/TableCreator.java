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

package uk.co.md87.evetool.api.util;

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

/**
 * Checks for the existance of, and if neccessary creates, tables in a SQL
 * database.
 *
 * @author chris
 */
public class TableCreator {

    /** A logger for this class. */
    private static final Logger LOGGER = Logger.getLogger(TableCreator.class.getName());

    /** The SQL connection to use. */
    private final Connection conn;

    /** The names of the tables to check for. */
    private final String[] tables;

    /** The location to look for SQL files to create tables. */
    private final String base;

    /**
     * Creates a new TableCreator which will check for the existance of the
     * specified tables using the specified connection. If the tables do not
     * exist, the TableCreator will attempt to load a .sql file from the
     * specified base directory with the same name as the table.
     *
     * @param conn The SQL connection to use
     * @param base The base directory for SQL files
     * @param tables The tables to create
     */
    public TableCreator(final Connection conn, final String base, final String[] tables) {
        this.conn = conn;
        this.tables = tables;
        this.base = base;
    }

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
                getClass().getResourceAsStream(base + table.toLowerCase() + ".sql")));
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
    public void checkTables() {
        LOGGER.log(Level.FINEST, "Checking that tables exist");

        final List<String> availTables = getTables();

        for (String table : tables) {
            if (!availTables.contains(table.toUpperCase())) {
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
        final List<String> availTables = new ArrayList<String>();

        try {
            final ResultSet rs = conn.getMetaData().getTables(null, null, null, null);

            while (rs.next()) {
                availTables.add(rs.getString("TABLE_NAME"));
            }

            rs.close();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQL Error when checking for tables", ex);
        }

        return availTables;
    }

}
