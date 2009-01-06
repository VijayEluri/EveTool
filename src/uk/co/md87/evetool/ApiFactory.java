/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.md87.evetool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import uk.co.md87.evetool.api.EveApi;

/**
 *
 * @author chris
 */
public class ApiFactory {

    private static String dbURL = "jdbc:derby:db/eveApi;create=true;user=eveApi;password=api881";

    private static Connection createConnection() {
        try {
            return DriverManager.getConnection(dbURL);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static EveApi getApi() {
        return new EveApi(createConnection());
    }

}
