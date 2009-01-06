/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.md87.evetool.api.io;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import uk.co.md87.evetool.api.EveApi;
import static org.junit.Assert.*;

/**
 *
 * @author chris
 */
public class DBCacheTest {

    private static String dbURL = "jdbc:derby:build/test-db/eveApi;create=true";

    @Test
    public void testBasicInsert() throws SQLException {
        final Map<String, String> args = new HashMap<String, String>();
        final Connection conn = DriverManager.getConnection(dbURL);
        new EveApi(conn); // To create tables
        final DBCache cache = new DBCache(conn);

        args.put("foo", "bar");
        args.put("rand", String.valueOf(System.currentTimeMillis()));

        assertEquals(ApiCache.CacheStatus.MISS, cache.getCacheStatus("/unittest", args));
        cache.setCache("/unittest", args, "testing 123", System.currentTimeMillis() - 100000);
        assertEquals(ApiCache.CacheStatus.EXPIRED, cache.getCacheStatus("/unittest", args));
        assertEquals("testing 123", cache.getCache("/unittest", args).getData());
        cache.setCache("/unittest", args, "testing 456", System.currentTimeMillis() + 100000);
        assertEquals(ApiCache.CacheStatus.HIT, cache.getCacheStatus("/unittest", args));
        assertEquals("testing 456", cache.getCache("/unittest", args).getData());
        conn.close();
    }

}