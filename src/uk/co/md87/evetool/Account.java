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

import uk.co.md87.evetool.api.EveApi;

/**
 * Represents one user account, with a user ID and an API key.
 *
 * @author chris
 */
public class Account {

    /** The user ID for this account. */
    private final int id;

    /** The API key for this account. */
    private final String key;

    /**
     * Creates a new account with the specified details.
     *
     * @param id The user ID for the account
     * @param key The API key for the account
     */
    public Account(final int id, final String key) {
        this.id = id;
        this.key = key;
    }

    /**
     * Retrieves the user ID associated with this account.
     *
     * @return This account's user ID
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieves the API key associated with this account.
     *
     * @return This account's API key
     */
    public String getKey() {
        return key;
    }

    /**
     * Uses the specified API factory to create a new instance of the EVE Api
     * and initialises its ID and Key settings to those of this account.
     *
     * @param factory The API factory to use
     * @return An API instance for this account
     */
    public EveApi getApi(final ApiFactory factory) {
        final EveApi api = factory.getApi();
        api.setUserID(id);
        api.setApiKey(key);

        return api;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "[" + id + " - " + key.substring(0, 6) + "...]";
    }

}
