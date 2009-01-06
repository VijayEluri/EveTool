/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.md87.evetool.api.io;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.serial.SerialClob;

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
    private PreparedStatement prepCheck = null;
    private PreparedStatement prepRetrieve = null;

    public DBCache(final Connection conn) {
        this.conn = conn;
        
        try {
            prepInsert = conn.prepareStatement("INSERT INTO PageCache (pc_method, "
                    + "pc_args, pc_cachedat, pc_cacheduntil, pc_data) VALUES ("
                    + "?, ?, ?, ?, ?)");
            prepUpdate = conn.prepareStatement("UPDATE PageCache SET "
                    + "pc_cachedat = ?, pc_cacheduntil = ?, pc_data = ? WHERE "
                    + "pc_method = ? AND pc_args = ?");
            prepCheck = conn.prepareStatement("SELECT pc_cacheduntil FROM "
                    + "PageCache WHERE pc_method = ? AND pc_args = ?");
            prepRetrieve = conn.prepareStatement("SELECT pc_cachedat, "
                    + "pc_cacheduntil, pc_data FROM PageCache WHERE "
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
            if (getCacheStatus(method, args) == CacheStatus.MISS) {
                // The page has never been requested before.
                
                prepInsert.setString(1, method);
                prepInsert.setString(2, Downloader.encodeArguments(args));
                prepInsert.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                prepInsert.setTimestamp(4, new Timestamp(cacheUntil));
                prepInsert.setClob(5, new SerialClob(data.toCharArray()));
                prepInsert.executeUpdate();
            } else {
                // The page has been requested before, but has been updated.

                prepUpdate.setString(4, method);
                prepUpdate.setString(5, Downloader.encodeArguments(args));
                prepUpdate.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                prepUpdate.setTimestamp(2, new Timestamp(cacheUntil));
                prepUpdate.setClob(3, new SerialClob(data.toCharArray()));
                prepUpdate.executeUpdate();
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error adding to cache", ex);
        }
    }

    /** {@inheritDoc} */
    @Override
    public CacheStatus getCacheStatus(final String method, final Map<String, String> args) {
        try {
            prepCheck.setString(1, method);
            prepCheck.setString(2, Downloader.encodeArguments(args));

            final ResultSet rs = prepCheck.executeQuery();

            if (rs.next()) {
                return rs.getTimestamp("PC_CACHEDUNTIL")
                        .before(new Date(System.currentTimeMillis()))
                        ? CacheStatus.EXPIRED : CacheStatus.HIT;
            } else {
                return CacheStatus.MISS;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error checking cache status", ex);
            return CacheStatus.MISS;
        }
    }

    /** {@inheritDoc} */
    @Override
    public CacheResult getCache(final String method, final Map<String, String> args) {
        try {
            prepRetrieve.setString(1, method);
            prepRetrieve.setString(2, Downloader.encodeArguments(args));

            final ResultSet rs = prepRetrieve.executeQuery();

            if (rs.next()) {
                final Clob clob = rs.getClob("PC_DATA");
                final long at = rs.getTimestamp("PC_CACHEDAT").getTime();
                final long until = rs.getTimestamp("PC_CACHEDUNTIL").getTime();
                return new CacheResult(clob.getSubString(1, (int) clob.length()), at, until);
            } else {
                LOGGER.log(Level.WARNING, "No cache result for " + method);
                return null;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error checking cache status", ex);
            return null;
        }
    }

}
