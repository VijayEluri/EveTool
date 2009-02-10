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

import net.miginfocom.swing.MigLayout;

import uk.co.md87.evetool.AccountManager;
import uk.co.md87.evetool.ApiFactory;
import uk.co.md87.evetool.api.wrappers.data.TrainedSkillInfo;
import uk.co.md87.evetool.ui.ContentPanel.Page;
import uk.co.md87.evetool.ui.ContextPanel;
import uk.co.md87.evetool.ui.MainWindow;
import uk.co.md87.evetool.ui.components.FilterButton;
import uk.co.md87.evetool.ui.components.HeaderPanel;
import uk.co.md87.evetool.ui.components.ListablePanel;
import uk.co.md87.evetool.ui.data.TrainedSkillInfoSurrogate;
import uk.co.md87.evetool.ui.dialogs.listableconfig.ListableConfigDialog;
import uk.co.md87.evetool.ui.listable.ListableComparator;
import uk.co.md87.evetool.ui.listable.ListableConfig;
import uk.co.md87.evetool.ui.listable.ListableParser;

/**
 *
 * TODO: Document SkillPage
 * @author chris
 */
public class SkillPage extends Page implements ActionListener {

    /**
     * A version number for this class. It should be changed whenever the class
     * structure is changed (or anything else that would prevent serialized
     * objects being unserialized with the new class).
     */
    private static final long serialVersionUID = 10;

    private final MainWindow window;
    private final ApiFactory factory;

    private ListableConfig config;

    public SkillPage(final MainWindow window, final AccountManager manager,
            final ApiFactory factory) {
        this.window = window;
        this.factory = factory;

        setLayout(new MigLayout("fillx, wrap 1"));

        config = new ListableConfig();
        config.topLeft = new ListableConfig.BasicConfigElement("name");
        config.topRight = new ListableConfig.CompoundConfigElement(
                new ListableConfig.BasicConfigElement("trained skillpoints"),
                new ListableConfig.LiteralConfigElement("/"),
                new ListableConfig.BasicConfigElement("max skillpoints"));
        config.bottomLeft = new ListableConfig.BasicConfigElement("group name");
        config.bottomRight = new ListableConfig.BasicConfigElement("time to next level");
        config.group = new ListableConfig.BasicConfigElement("group name");

        config.sortOrder = new ListableConfig.ConfigElement[]{
            new ListableConfig.BasicConfigElement("group name"),
            new ListableConfig.BasicConfigElement("current level"),
            new ListableConfig.BasicConfigElement("trained skillpoints"),
        };
    }

    /** {@inheritDoc} */
    @Override
    public boolean isReady() {
        return character != null && character.getSheet() != null
                && character.getSheet().wasSuccessful();
    }

    /** {@inheritDoc} */
    @Override
    public void activated(final ContextPanel context) {
        final FilterButton button = new FilterButton();
        button.addActionListener(this);
        context.add(button, "growy, al right");
        
        updatePage();
    }


    protected void updatePage() {
        removeAll();

        final ListableParser parser = new ListableParser(TrainedSkillInfoSurrogate.class);
        final List<TrainedSkillInfoSurrogate> list = new ArrayList<TrainedSkillInfoSurrogate>();

        for (TrainedSkillInfo skill : character.getSheet().getResult().getSkills()) {
            list.add(new TrainedSkillInfoSurrogate(skill));
        }

        if (config.sortOrder != null) {
            Collections.sort(list, new ListableComparator(config.sortOrder, parser));
        }

        String lastGroup = null;
        boolean first = true;
        
        for (TrainedSkillInfoSurrogate skill : list) {
            if (config.group != null) {
                final String thisGroup = config.group.getValue(skill, parser);

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

            add(new ListablePanel(skill, parser, config),
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
        new ListableConfigDialog(window, this, config, new TrainedSkillInfoSurrogate(
                character.getSheet().getResult().getSkills().get(0))).setVisible(true);
    }

}
