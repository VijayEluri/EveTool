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


import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

import uk.co.md87.evetool.AccountManager;
import uk.co.md87.evetool.ApiFactory;
import uk.co.md87.evetool.api.wrappers.data.TrainedSkillInfo;
import uk.co.md87.evetool.ui.ContentPanel.Page;
import uk.co.md87.evetool.ui.ContextPanel;
import uk.co.md87.evetool.ui.MainWindow;
import uk.co.md87.evetool.ui.components.FilterButton;
import uk.co.md87.evetool.ui.components.SkillPanel;

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

        add(new JLabel("Skills! Moo!"));
    }

    /** {@inheritDoc} */
    @Override
    public boolean isReady() {
        return character != null && character.getSheet() != null
                && character.getSheet().wasSuccessful();
    }

    @Override
    public void activated(final ContextPanel context) {
        context.add(new FilterButton(), "growy, al right");

        removeAll();

        // TODO: Sorting
        for (TrainedSkillInfo skill : character.getSheet().getResult().getSkills()) {
            add(new SkillPanel(skill));
        }
    }

}
