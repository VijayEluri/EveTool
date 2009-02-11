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
import uk.co.md87.evetool.api.wrappers.data.BasicShipInfo;
import uk.co.md87.evetool.ui.MainWindow;
import uk.co.md87.evetool.ui.data.BasicShipInfoSurrogate;
import uk.co.md87.evetool.ui.listable.ListableConfig;
import uk.co.md87.evetool.ui.listable.ListableConfig.BasicConfigElement;
import uk.co.md87.evetool.ui.listable.ListableConfig.CompoundConfigElement;

/**
 *
 * TODO: Document ShipsPage
 * @author chris
 */
public class ShipsPage extends ListablePage<BasicShipInfoSurrogate> implements ActionListener {

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
        config.topLeft = new BasicConfigElement("name");
        config.topRight = new CompoundConfigElement();
        config.bottomLeft = new BasicConfigElement("can fly");
        config.bottomRight = new CompoundConfigElement();
        config.group = new BasicConfigElement("group name");

        config.sortOrder = new CompoundConfigElement(
            new BasicConfigElement("group name"),
            new BasicConfigElement("name")
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
    protected List<BasicShipInfoSurrogate> getListables() {
        final List<BasicShipInfoSurrogate> list = new ArrayList<BasicShipInfoSurrogate>();

        for (BasicShipInfo ship : factory.getApi().getShipList().getResult()
                .getShips().values()) {
            list.add(new BasicShipInfoSurrogate(ship, character.getSheet().getResult()));
        }

        return list;
    }

}
