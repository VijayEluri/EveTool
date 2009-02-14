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

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;

import net.miginfocom.swing.MigLayout;

import uk.co.md87.evetool.Account;
import uk.co.md87.evetool.AccountManager;
import uk.co.md87.evetool.ApiFactory;
import uk.co.md87.evetool.ConfigManager;
import uk.co.md87.evetool.api.EveApi;
import uk.co.md87.evetool.ui.ContentPanel.Page;
import uk.co.md87.evetool.ui.ContextPanel;
import uk.co.md87.evetool.ui.MainWindow;
import uk.co.md87.evetool.ui.components.AddButton;
import uk.co.md87.evetool.ui.components.FilterButton;
import uk.co.md87.evetool.ui.data.AccountChar;
import uk.co.md87.evetool.ui.dialogs.addaccount.AddAccountDialog;
import uk.co.md87.evetool.ui.workers.AccountUpdateWorker;

/**
 *
 * TODO: Document OverviewPage
 * @author chris
 */
public class OverviewPage extends Page implements AccountManager.AccountListener,
        ActionListener {

    /**
     * A version number for this class. It should be changed whenever the class
     * structure is changed (or anything else that would prevent serialized
     * objects being unserialized with the new class).
     */
    private static final long serialVersionUID = 10;

    private final MainWindow window;
    private final ApiFactory factory;
    private final AccountManager manager;
    private final ConfigManager config;
    private final Map<Account, EveApi> apis = new HashMap<Account, EveApi>();
    private final Map<Account, JPanel> panels = new HashMap<Account, JPanel>();
    private final List<AccountChar> chars = new ArrayList<AccountChar>();

    public OverviewPage(final MainWindow window, final AccountManager manager,
            final ApiFactory factory, final ConfigManager config) {
        this.window = window;
        this.factory = factory;
        this.manager = manager;
        this.config = config;

        setLayout(new MigLayout("fillx"));

        for (Account account : manager.getAccounts(this)) {
            addAccount(account);
        }

        new Timer(1000, this).start();
    }

    public void addChar(final AccountChar ac) {
        synchronized (chars) {
            chars.add(ac);
        }
    }

    public void setSelectedChar(final AccountChar ac) {
        window.setSelectedChar(ac);
    }

    public void updateCharacters() {
        for (AccountChar character : chars) {
            character.updateSkillInfo(false);
        }
    }

    protected void addAccount(final Account account) {
        final JLabel header = new JLabel("Account #" + account.getId());
        final JPanel headerP = new JPanel(new MigLayout("fillx"));
        header.setFont(header.getFont().deriveFont(14f).deriveFont(Font.BOLD));
        headerP.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createBevelBorder(BevelBorder.RAISED),
                BorderFactory.createEmptyBorder(0, 5, 0, 5)));
        headerP.add(header, "growx");

        try {
            final JButton editButton = new JButton(new ImageIcon(ImageIO.read(
                    getClass().getResource("/uk/co/md87/evetool/ui/res/edit-inactive.png"))));
            editButton.setRolloverIcon(new ImageIcon(ImageIO
                    .read(getClass().getResource("/uk/co/md87/evetool/ui/res/edit.png"))));
            editButton.setBorder(BorderFactory.createEmptyBorder());
            editButton.setOpaque(false);
            editButton.setContentAreaFilled(false);

            final JButton delButton = new JButton(new ImageIcon(ImageIO.read(
                    getClass().getResource("/uk/co/md87/evetool/ui/res/close-inactive.png"))));
            delButton.setRolloverIcon(new ImageIcon(ImageIO.read(
                    getClass().getResource("/uk/co/md87/evetool/ui/res/close-active.png"))));
            delButton.setBorder(BorderFactory.createEmptyBorder());
            delButton.setOpaque(false);
            delButton.setContentAreaFilled(false);

            headerP.add(editButton, "al right");
            headerP.add(delButton, "al right");
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,
                    "Error loading images", ex);
        }

        add(headerP, "span, growx, wrap");

        final JPanel panel = new JPanel(new MigLayout(" fillx", "[|fill,grow|fill,grow]"));
        panel.add(new JLabel("Loading..."), "span");
        add(panel, "growx, wrap");

        panels.put(account, panel);
        apis.put(account, account.getApi(factory));

        int selectChar = config.getGeneralSettingInt("selectedChar", -1);
        
        new AccountUpdateWorker(this, window, apis.get(account), panel, selectChar).execute();
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

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() instanceof AddButton) {
            new AddAccountDialog(window, manager).setVisible(true);
        } else {
            updateCharacters();
        }
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void activated(final ContextPanel context) {
        final AddButton addButton = new AddButton("Add account");
        addButton.addActionListener(this);
        
        context.add(addButton, "growy");
        //context.add(new FilterButton(), "growy, al right");
    }

}
