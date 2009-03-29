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
        final DBCache cache = new DBCache(conn);
        new EveApi(cache); // To create tables

        args.put("foo", "bar");
        args.put("rand", String.valueOf(System.currentTimeMillis()));

        assertEquals(ApiCache.CacheStatus.MISS, cache.getCacheStatus("/unittest", args));
        cache.setCache("/unittest", args, "testing 123", System.currentTimeMillis() - 100000);
        assertEquals(ApiCache.CacheStatus.EXPIRED, cache.getCacheStatus("/unittest", args));
        assertEquals("testing 123", cache.getCache("/unittest", args).getData());
        cache.setCache("/unittest", args, "testing 456", System.currentTimeMillis() + 100000);
        assertEquals(ApiCache.CacheStatus.HIT, cache.getCacheStatus("/unittest", args));
        assertEquals("testing 456", cache.getCache("/unittest", args).getData());
    }

}