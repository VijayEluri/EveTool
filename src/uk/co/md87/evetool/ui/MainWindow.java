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

package uk.co.md87.evetool.ui;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

import uk.co.md87.evetool.AccountManager;
import uk.co.md87.evetool.ApiFactory;
import uk.co.md87.evetool.ConfigManager;
import uk.co.md87.evetool.ImageManager;
import uk.co.md87.evetool.ui.ContentPanel.Page;
import uk.co.md87.evetool.ui.data.AccountChar;
import uk.co.md87.evetool.api.listable.Listable;
import uk.co.md87.evetool.api.listable.UpdateListener;
import uk.co.md87.evetool.ui.pages.OverviewPage;
import uk.co.md87.evetool.ui.pages.ShipsPage;
import uk.co.md87.evetool.ui.pages.SkillPage;

/**
 *
 * TODO: Document MainWindow
 * @author chris
 */
public class MainWindow extends JFrame implements UpdateListener {

    /**
     * A version number for this class. It should be changed whenever the class
     * structure is changed (or anything else that would prevent serialized
     * objects being unserialized with the new class).
     */
    private static final long serialVersionUID = 10;

    private AccountChar selectedChar = null;
    private final AccountManager manager;
    private final ImageManager imageman;
    private final ApiFactory factory;
    private final MenuPanel menuPanel;
    private final ConfigManager config;
    private final ContentPanel contentPanel;
    private final ContextPanel contextPanel;
    private final Map<String, ContentPanel.Page> pages;

    public MainWindow(final AccountManager manager, final ApiFactory factory,
            final ConfigManager config, final ImageManager imageman) {
        super("EVE Tool - No character selected");

        UIManager.put("swing.boldMetal", false);

        this.factory = factory;
        this.manager = manager;
        this.config = config;
        this.imageman = imageman;
        this.contextPanel = new ContextPanel();

        pages = new TreeMap<String, ContentPanel.Page>();
        pages.put("Overview", new OverviewPage(this, manager, factory, config));
        pages.put("Skills", new SkillPage(this, manager, factory));
        pages.put("Ships", new ShipsPage(this, manager, factory));

        this.menuPanel = new MenuPanel(this, pages);
        this.contentPanel = new ContentPanel(this, manager, factory, pages);
        
        setLayout(new MigLayout("insets 0, fill, wrap 2", "[]0[fill,grow]",
                "[fill,grow]0[]"));
        addComponents();
        setMinimumSize(new Dimension(300, 300));
        setSize(new Dimension(800, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setPage("Overview");
        
        try {
            final Image image = ImageIO.read(getClass().getResource("res/icon.png"));
            setIconImage(image);

            if (SystemTray.isSupported()) {
                final TrayIcon trayicon = new TrayIcon(image
                        .getScaledInstance(SystemTray.getSystemTray().getTrayIconSize().width,
                        SystemTray.getSystemTray().getTrayIconSize().height,
                        Image.SCALE_SMOOTH), "EVE Tool");
                SystemTray.getSystemTray().add(trayicon);
            }
        } catch (IOException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AWTException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ImageManager getImageManager() {
        return imageman;
    }

    public void setPage(final String page) {
        menuPanel.setSelectedPage(page);
        contextPanel.removeAll();
        pages.get(page).activated(contextPanel);
        contentPanel.show(page);
        contextPanel.revalidate();
    }

    public void setSelectedChar(final AccountChar newChar) {
        if (selectedChar != null) {
            selectedChar.removeUpdateListener(this);
        }

        selectedChar = newChar;

        selectedChar.addUpdateListener(this);

        for (Page page : pages.values()) {
            page.setActiveChar(newChar);
        }

        menuPanel.setSelectedChar(newChar);
        setTitle("EVE Tool - " + newChar.getCharInfo().getName());

        config.setGeneralSetting("selectedChar", newChar.getCharInfo().getId());
    }

    public ContextPanel getContextPanel() {
        return contextPanel;
    }

    protected void addComponents() {
        add(menuPanel, "width 201!, spany2");
        add(contentPanel);
        add(contextPanel, "growx, height 30!");
        add(new StatusPanel(this), "growx, span, height 30!");
    }

    public void listableUpdated(Listable listable) {
        menuPanel.setSelectedChar(selectedChar);
    }

}
