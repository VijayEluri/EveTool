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

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import uk.co.md87.evetool.AccountManager;
import uk.co.md87.evetool.ApiFactory;
import uk.co.md87.evetool.api.wrappers.data.TrainedSkillInfo;
import uk.co.md87.evetool.ui.MainWindow;
import uk.co.md87.evetool.ui.data.TrainedSkillInfoSurrogate;
import uk.co.md87.evetool.ui.listable.ListableConfig;
import uk.co.md87.evetool.ui.listable.ListableConfig.BasicConfigElement;
import uk.co.md87.evetool.ui.listable.ListableConfig.CompoundConfigElement;
import uk.co.md87.evetool.ui.listable.ListableConfig.LiteralConfigElement;

/**
 *
 * TODO: Document SkillPage
 * @author chris
 */
public class SkillPage extends ListablePage<TrainedSkillInfoSurrogate>
        implements ActionListener {

    /**
     * A version number for this class. It should be changed whenever the class
     * structure is changed (or anything else that would prevent serialized
     * objects being unserialized with the new class).
     */
    private static final long serialVersionUID = 10;

    public SkillPage(final MainWindow window, final AccountManager manager,
            final ApiFactory factory) {
        super(window, manager, factory);

        config = new ListableConfig();
        config.topLeft = new BasicConfigElement("name");
        config.topRight = new CompoundConfigElement(
                new BasicConfigElement("trained skillpoints"),
                new LiteralConfigElement("/"),
                new BasicConfigElement("max skillpoints"));
        config.bottomLeft = new BasicConfigElement("group name");
        config.bottomRight = new BasicConfigElement("time to next level");
        config.group = new BasicConfigElement("group name");

        config.sortOrder = new CompoundConfigElement(
            new BasicConfigElement("group name"),
            new BasicConfigElement("current level"),
            new BasicConfigElement("trained skillpoints")
        );
    }

    /** {@inheritDoc} */
    @Override
    public boolean isReady() {
        return character != null && character.getSheet() != null
                && character.getSheet().wasSuccessful();
    }

    /** {@inheritDoc} */
    @Override
    protected List<TrainedSkillInfoSurrogate> getListables() {
        final List<TrainedSkillInfoSurrogate> list = new ArrayList<TrainedSkillInfoSurrogate>();

        for (TrainedSkillInfo skill : character.getSheet().getResult().getSkills().values()) {
            list.add(new TrainedSkillInfoSurrogate(skill));
        }

        return list;
    }

}
