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

package uk.co.md87.evetool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import uk.co.md87.evetool.api.EveApi;

/**
 * Factory class to create instances of the {@link EveApi} class. Uses an
 * embedded Derby database server by default.
 *
 * @author chris
 */
public class ApiFactory {

    /** The URL of the database. */
    private static String dbURL = "jdbc:derby:db/eveApi;create=true";

    private static Connection dbConn;

    /**
     * Creates a database connection for use by the API.
     *
     * @return A Database Connection
     */
    public static synchronized Connection getConnection() {
        try {
            return dbConn == null ? dbConn = DriverManager.getConnection(dbURL) : dbConn;
        } catch (SQLException ex) {
            Logger.getLogger(ApiFactory.class.getName()).log(Level.SEVERE,
                    "Exception when creating DB connection", ex);
            return null;
        }
    }

    /**
     * Retrieves a new instance of the EVE API.
     *
     * @return An instance of the EVE API.
     */
    public static EveApi getApi() {
        return new EveApi(getConnection());
    }

}
