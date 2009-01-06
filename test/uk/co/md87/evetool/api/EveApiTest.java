/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.md87.evetool.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author chris
 */
public class EveApiTest {

    private static String dbURL = "jdbc:derby:build/test-db/eveApi;create=true";

    /**
     * Test of createTable method, of class EveApi.
     */
    @Test
    public void testCreateTables() throws SQLException {
        final Connection conn = DriverManager.getConnection(dbURL);

        if (conn.getMetaData().getTables(null, null, "PAGECACHE", null).next()) {
            conn.createStatement().execute("DROP TABLE PAGECACHE");
        }
        
        final EveApi api = new EveApi(conn);
        assertTrue(conn.getMetaData().getTables(null, null, "PAGECACHE", null).next());
    }

}