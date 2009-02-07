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

import java.util.Collections;

import javax.swing.JLabel;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;

import uk.co.md87.evetool.AccountManager;
import uk.co.md87.evetool.ApiFactory;
import uk.co.md87.evetool.api.wrappers.data.TrainedSkillInfo;
import uk.co.md87.evetool.comparators.skills.TrainingTimeComparator;
import uk.co.md87.evetool.ui.ContentPanel.Page;
import uk.co.md87.evetool.ui.ContextPanel;
import uk.co.md87.evetool.ui.MainWindow;
import uk.co.md87.evetool.ui.components.FilterButton;
import uk.co.md87.evetool.ui.components.ListablePanel;
import uk.co.md87.evetool.ui.data.TrainedSkillInfoSurrogate;
import uk.co.md87.evetool.ui.listable.ListableConfig;
import uk.co.md87.evetool.ui.listable.ListableParser;

/**
 *
 * TODO: Document SkillPage
 * @author chris
 */
public class SkillPage extends Page {

    /**
     * A version number for this class. It should be changed whenever the class
     * structure is changed (or anything else that would prevent serialized
     * objects being unserialized with the new class).
     */
    private static final long serialVersionUID = 10;

    private final MainWindow window;
    private final ApiFactory factory;

    public SkillPage(final MainWindow window, final AccountManager manager,
            final ApiFactory factory) {
        this.window = window;
        this.factory = factory;

        setLayout(new MigLayout("fillx, wrap 1"));
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
        context.add(new FilterButton(), "growy, al right");

        removeAll();

        Collections.sort(character.getSheet().getResult().getSkills(),
                new TrainingTimeComparator(true));

        final ListableParser parser = new ListableParser(TrainedSkillInfoSurrogate.class);
        final ListableConfig config = new ListableConfig();
        config.topLeft = "Name";
        config.bottomLeft = "Group Name";
        
        boolean first = true;
        for (TrainedSkillInfo skill : character.getSheet().getResult().getSkills()) {
            if (first) {
                first = false;
            } else {
                add(new JSeparator(), "growx, pushx");
            }

            add(new ListablePanel(new TrainedSkillInfoSurrogate(skill), parser, config),
                    "growx, pushx");
        }
    }

}
