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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import java.util.List;
import javax.swing.JSeparator;

import uk.co.md87.evetool.AccountManager;
import uk.co.md87.evetool.ApiFactory;
import uk.co.md87.evetool.api.wrappers.data.BasicShipInfo;
import uk.co.md87.evetool.ui.MainWindow;
import uk.co.md87.evetool.ui.components.HeaderPanel;
import uk.co.md87.evetool.ui.components.ListablePanel;
import uk.co.md87.evetool.ui.data.BasicShipInfoSurrogate;
import uk.co.md87.evetool.ui.dialogs.listableconfig.ListableConfigDialog;
import uk.co.md87.evetool.ui.listable.ListableComparator;
import uk.co.md87.evetool.ui.listable.ListableConfig;
import uk.co.md87.evetool.ui.listable.ListableParser;

/**
 *
 * TODO: Document ShipsPage
 * @author chris
 */
public class ShipsPage extends ListablePage implements ActionListener {

    /**
     * A version number for this class. It should be changed whenever the class
     * structure is changed (or anything else that would prevent serialized
     * objects being unserialized with the new class).
     */
    private static final long serialVersionUID = 10;

    public ShipsPage(final MainWindow window, final AccountManager manager,
            final ApiFactory factory) {
        super(window, manager, factory);

        config = new ListableConfig();
        config.topLeft = new ListableConfig.BasicConfigElement("name");
        config.topRight = new ListableConfig.CompoundConfigElement();
        config.bottomLeft = new ListableConfig.BasicConfigElement("can fly");
        config.bottomRight = new ListableConfig.CompoundConfigElement();
        config.group = new ListableConfig.BasicConfigElement("group name");

        config.sortOrder = new ListableConfig.ConfigElement[]{
            new ListableConfig.BasicConfigElement("group name"),
            new ListableConfig.BasicConfigElement("name"),
        };
    }

    /** {@inheritDoc} */
    @Override
    public boolean isReady() {
        return character != null && character.getSheet() != null
                && character.getSheet().wasSuccessful();
    }

    protected void updatePage() {
        removeAll();

        final ListableParser parser = new ListableParser(BasicShipInfoSurrogate.class);
        final List<BasicShipInfoSurrogate> list = new ArrayList<BasicShipInfoSurrogate>();

        for (BasicShipInfo ship : factory.getApi().getShipList().getResult()
                .getShips().values()) {
            list.add(new BasicShipInfoSurrogate(ship, character.getSheet().getResult()));
        }

        if (config.sortOrder != null) {
            Collections.sort(list, new ListableComparator(config.sortOrder, parser));
        }

        String lastGroup = null;
        boolean first = true;
        
        for (BasicShipInfoSurrogate ship : list) {
            if (config.group != null) {
                final String thisGroup = config.group.getValue(ship, parser);

                if (lastGroup == null || !thisGroup.equals(lastGroup)) {
                    first = true;
                    lastGroup = thisGroup;
                    add(new HeaderPanel(lastGroup), "growx, pushx");
                }
            }

            if (first) {
                first = false;
            } else {
                add(new JSeparator(), "growx, pushx");
            }

            add(new ListablePanel(ship, parser, config),
                    "growx, pushx");
        }

        revalidate();
    }

    public void setConfig(final ListableConfig config) {
        this.config = config;
        
        updatePage();
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(final ActionEvent e) {
        new ListableConfigDialog(window, this, config,
                new BasicShipInfoSurrogate(factory.getApi().getShipList()
                .getResult().getShips().values().iterator().next(),
                character.getSheet().getResult())).setVisible(true);
    }

}
