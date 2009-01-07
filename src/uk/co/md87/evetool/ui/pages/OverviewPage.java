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

import net.miginfocom.swing.MigLayout;

import uk.co.md87.evetool.api.EveApi;
import uk.co.md87.evetool.ui.workers.AccountUpdateWorker;

/**
 *
 * TODO: Document OverviewPage
 * @author chris
 */
public class OverviewPage extends JPanel {

    private final EveApi api;
    private final Map<String, JPanel> panels = new HashMap<String, JPanel>();

    public OverviewPage(final EveApi api) {
        this.api = api;

        setLayout(new MigLayout("fillx"));
        add(new JLabel("Account 1 - 403848"), "span, wrap");

        final JPanel panel = new JPanel(new MigLayout());
        panel.add(new JLabel("Loading..."));
        panels.put("Account 1", panel);
        add(panel, "wrap");

        new AccountUpdateWorker("Account 1",this).execute();
    }

    public EveApi getApi() {
        return api;
    }

    public Map<String, JPanel> getPanels() {
        return panels;
    }

}
