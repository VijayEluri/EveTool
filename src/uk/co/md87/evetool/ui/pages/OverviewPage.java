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

package uk.co.md87.evetool.ui.pages;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;

import uk.co.md87.evetool.Account;
import uk.co.md87.evetool.AccountManager;
import uk.co.md87.evetool.ApiFactory;
import uk.co.md87.evetool.api.EveApi;
import uk.co.md87.evetool.ui.ContentPanel.Page;
import uk.co.md87.evetool.ui.ContextPanel;
import uk.co.md87.evetool.ui.components.AddButton;
import uk.co.md87.evetool.ui.workers.AccountUpdateWorker;

/**
 *
 * TODO: Document OverviewPage
 * @author chris
 */
public class OverviewPage extends Page implements AccountManager.AccountListener {

    private final ApiFactory factory;
    private final Map<Account, EveApi> apis = new HashMap<Account, EveApi>();
    private final Map<Account, JPanel> panels = new HashMap<Account, JPanel>();

    public OverviewPage(final ContextPanel context, final AccountManager manager,
            final ApiFactory factory) {
        this.factory = factory;

        setLayout(new MigLayout("fillx"));

        for (Account account : manager.getAccounts(this)) {
            addAccount(account);
        }

        context.add(new AddButton("Add account"), "growy");
    }

    protected void addAccount(final Account account) {
        // TODO: Number them or reformat or something
        add(new JLabel("Account N - " + account.getId()), "span, wrap");

        final JPanel panel = new JPanel(new MigLayout(" fillx", "[|fill,grow|fill,grow]"));
        panel.add(new JLabel("Loading..."), "span");
        add(panel, "growx, wrap");

        panels.put(account, panel);
        apis.put(account, account.getApi(factory));
        
        new AccountUpdateWorker(apis.get(account), panel).execute();
    }

    /** {@inheritDoc} */
    @Override
    public void accountAdded(final Account account) {
        SwingUtilities.invokeLater(new Runnable() {

            /** {@inheritDoc} */
            @Override
            public void run() {
                addAccount(account);
            }
        });
    }

}
