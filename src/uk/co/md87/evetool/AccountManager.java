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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages accounts that have been added to the tool.
 *
 * @author chris
 */
public class AccountManager {

    /** Logger to use for this class. */
    private static final Logger LOGGER = Logger.getLogger(AccountManager.class.getName());

    /** A list of known accounts. */
    private final List<Account> accounts = new ArrayList<Account>();

    /** A list of listeners. */
    private final List<AccountListener> listeners = new ArrayList<AccountListener>();

    /** The SQL connection to use. */
    private final Connection conn;

    /** The SQL statement to use to insert new accounts. */
    private PreparedStatement insertStm;

    /**
     * Creates a new account manager which will use the specified DB connection.
     *
     * @param conn The database connection to use
     */
    public AccountManager(final Connection conn) {
        this.conn = conn;

        try {
            this.insertStm = conn.prepareStatement("INSERT INTO accounts " +
                    "(account_userid, account_key) VALUES (?, ?)");
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Unable to prepare account statement", ex);
        }

        loadAccounts();
    }

    /**
     * Retrieves a list of all known accounts.
     * 
     * @return A list of known accounts
     */
    public List<Account> getAccounts() {
        return new ArrayList<Account>(accounts);
    }

    /**
     * Retrieves a list of all known accounts, and registers the specified
     * {@link AccountListener}. Any account added before, during or after this
     * method call is guaranteed to either be present in the returned list, or
     * have an accountAdded listener fired for it.
     *
     * @param listener The listener to be added
     * @return A list of known accounts
     */
    public synchronized List<Account> getAccounts(final AccountListener listener) {
        addListener(listener);
        
        return new ArrayList<Account>(accounts);
    }

    /**
     * Adds a new account with the specified details.
     * 
     * @param id The user ID of the account owner
     * @param key The API key for the account
     */
    public void addAccount(final int id, final String key) {
        try {
            insertStm.setInt(1, id);
            insertStm.setString(2, key);
            insertStm.execute();

            addAccount(new Account(id, key));
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Unable to add account", ex);
        }
    }

    /**
     * Registers the specified listener with this account manager.
     *
     * @param listener The listener to be registered
     */
    public void addListener(final AccountListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /**
     * Adds an account to the list of known accounts and fires the
     * appropriate listeners.
     *
     * @param account The account to be added
     */
    protected synchronized void addAccount(final Account account) {
        LOGGER.log(Level.FINE, "Adding account: " + account);

        accounts.add(account);

        synchronized (listeners) {
            for (AccountListener listener : listeners) {
                listener.accountAdded(account);
            }
        }
    }

    /**
     * Loads all accounts in the database.
     */
    protected void loadAccounts() {
        try {
            final Statement statement = conn.createStatement();
            final ResultSet set = statement.executeQuery("SELECT account_userid, "
                    + "account_key FROM accounts");

            while (set.next()) {
                final int id = set.getInt("ACCOUNT_USERID");
                final String key = set.getString("ACCOUNT_KEY");
                addAccount(new Account(id, key));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error loading accounts", ex);
        }
    }

    /**
     * Interfaces implemented by objects who wish to be notified when accounts
     * are modified.
     */
    public static interface AccountListener {

        /**
         * Called when a new account has been added.
         *
         * @param account The account that was added.
         */
        void accountAdded(final Account account);

    }
}
