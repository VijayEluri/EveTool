/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.md87.evetool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import uk.co.md87.evetool.api.EveApi;

/**
 *
 * TODO: Document
 * @author chris
 */
public class ApiFactory {

    private static String dbURL = "jdbc:derby:db/eveApi;create=true";

    private static Connection createConnection() {
        try {
            return DriverManager.getConnection(dbURL);
        } catch (SQLException ex) {
            Logger.getLogger(ApiFactory.class.getName()).log(Level.SEVERE,
                    "Exception when creating DB connection", ex);
            return null;
        }
    }

    public static EveApi getApi() {
        return new EveApi(createConnection());
    }

}
