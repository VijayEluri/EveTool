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

package uk.co.md87.evetool.ui.listable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TODO: Document ListableConfigManager
 *
 * @author chris
 */
public class ListableConfigManager {

    /** A logger for this class. */
    private static final Logger LOGGER = Logger.getLogger(ListableConfigManager.class.getName());

    private final Connection conn;

    private PreparedStatement prepSelect = null;
    private PreparedStatement prepSelpid = null;
    private PreparedStatement prepInsert = null;
    private PreparedStatement prepDelete = null;

    public ListableConfigManager(final Connection conn) {
        this.conn = conn;

        try {
            prepSelect = conn.prepareStatement("SELECT pce_location, pce_type, "
                    + "pce_value FROM PageConfigs NATURAL JOIN PageConfigElements "
                    + "WHERE pc_page = ? ORDER BY pce_location, pce_order");
            prepSelpid = conn.prepareStatement("SELECT pc_id FROM PageConfigs "
                    + "WHERE pc_page = ?");
            prepDelete = conn.prepareStatement("DELETE FROM PageConfigElements "
                   + "WHERE pc_id = ?");
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error preparing statements", ex);
        }
    }

    public ListableConfig getConfig(final String page) {
        return null;
    }

    public void setConfig(final String page, final ListableConfig config) {
        
    }

}
