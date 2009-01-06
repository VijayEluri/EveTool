/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.md87.evetool.api.io;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * TODO: Document
 * @author chris
 */
public class DBCache implements ApiCache {

    private static final Logger LOGGER = Logger.getLogger(DBCache.class.getName());

    private final Connection conn;

    private PreparedStatement prepInsert = null;
    private PreparedStatement prepUpdate = null;
    // TODO: Statement for retrieving cache time
    // TODO: Statement for retrieving cache

    public DBCache(final Connection conn) {
        this.conn = conn;
        
        try {
            prepInsert = conn.prepareStatement("INSERT INTO PageCache (pc_method, "
                    + "pc_args, pc_cachedat, pc_cacheduntil, pc_data) VALUES ("
                    + "?, ?, ?, ?, ?)");
            prepUpdate = conn.prepareStatement("UPDATE PageCache SET "
                    + "pc_cachedat = ?, pc_cacheduntil = ?, pc_data = ? WHERE "
                    + "pc_method = ? AND pc_args = ?");
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error preparing statements", ex);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setCache(final String method, final Map<String, String> args,
            final String data, final long cacheUntil) {
        try {
            if (getCacheStatus(method, args) == ApiCacheStatus.MISS) {
                // The page has never been requested before.
                
                prepInsert.setString(1, method);
                prepInsert.setString(2, Downloader.encodeArguments(args));
                prepInsert.setDate(3, new Date(System.currentTimeMillis()));
                prepInsert.setDate(4, new Date(cacheUntil));
                prepInsert.setString(5, data);
                prepInsert.executeUpdate();
            } else {
                // The page has been requested before, but has been updated.

                prepUpdate.setString(4, method);
                prepUpdate.setString(5, Downloader.encodeArguments(args));
                prepUpdate.setDate(1, new Date(System.currentTimeMillis()));
                prepUpdate.setDate(2, new Date(cacheUntil));
                prepUpdate.setString(3, data);
                prepUpdate.executeUpdate();
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error adding to cache", ex);
        }
    }

    /** {@inheritDoc} */
    @Override
    public ApiCacheStatus getCacheStatus(final String method, final Map<String, String> args) {
        // TODO: Implement getCacheStatus
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String getCache(final String method, final Map<String, String> args) {
        // TODO: Implement getCache
        return null;
    }

}
