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

import java.util.concurrent.ExecutionException;
import javax.swing.JLabel;
import javax.swing.JSeparator;

import javax.swing.SwingWorker;
import net.miginfocom.swing.MigLayout;

import uk.co.md87.evetool.AccountManager;
import uk.co.md87.evetool.ApiFactory;
import uk.co.md87.evetool.ui.ContentPanel.Page;
import uk.co.md87.evetool.ui.ContextPanel;
import uk.co.md87.evetool.ui.MainWindow;
import uk.co.md87.evetool.ui.components.FilterButton;
import uk.co.md87.evetool.ui.components.HeaderPanel;
import uk.co.md87.evetool.ui.components.ListablePanel;
import uk.co.md87.evetool.ui.dialogs.listableconfig.ListableConfigDialog;
import uk.co.md87.evetool.ui.listable.Listable;
import uk.co.md87.evetool.ui.listable.ListableComparator;
import uk.co.md87.evetool.ui.listable.ListableConfig;
import uk.co.md87.evetool.ui.listable.ListableConfig.CompoundConfigElement;
import uk.co.md87.evetool.ui.listable.ListableParser;

/**
 *
 * TODO: Document ListablePage
 * @author chris
 */
public abstract class ListablePage<T extends Listable> extends Page implements ActionListener {

    /**
     * A version number for this class. It should be changed whenever the class
     * structure is changed (or anything else that would prevent serialized
     * objects being unserialized with the new class).
     */
    private static final long serialVersionUID = 10;

    protected final MainWindow window;
    protected final ApiFactory factory;
    protected final AccountManager manager;

    protected ListableConfig config;

    public ListablePage(final MainWindow window, final AccountManager manager,
            final ApiFactory factory) {
        this.window = window;
        this.factory = factory;
        this.manager = manager;

        setLayout(new MigLayout("fillx, wrap 1"));
    }

    /** {@inheritDoc} */
    @Override
    public void activated(final ContextPanel context) {
        final FilterButton button = new FilterButton();
        button.addActionListener(this);
        context.add(button, "growy, al right");

        updatePage();
    }

    public void setConfig(final ListableConfig config) {
        this.config = config;

        updatePage();
    }

    protected void updatePage() {
        removeAll();

        add(new JLabel("Loading..."));

        new UpdateWorker().execute();
    }

    protected abstract List<T> getListables();

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(final ActionEvent e) {
        new ListableConfigDialog(window, this, config, getListables().get(0)).setVisible(true);
    }
    
    protected class UpdateResult {
        
        private final List<T> list;
        private final ListableParser parser;

        public UpdateResult(List<T> list, ListableParser parser) {
            this.list = list;
            this.parser = parser;
        }

        public List<T> getList() {
            return list;
        }

        public ListableParser getParser() {
            return parser;
        }
    }

    protected class UpdateWorker extends SwingWorker<UpdateResult, Void> {

        @Override
        protected UpdateResult doInBackground() throws Exception {
            final List<T> list = new ArrayList<T>(getListables());
            final ListableParser parser = new ListableParser(list.get(0).getClass());

            if (config.sortOrder != null) {
                Collections.sort(list, new ListableComparator(config.sortOrder, parser));
            }

            return new UpdateResult(list, parser);
        }

        @Override
        protected void done() {
            try {
                final UpdateResult res = get();

                removeAll();

                String lastGroup = null;
                boolean first = true;

                for (T listable : res.getList()) {
                    if (config.group != null &&
                            (!(config.group instanceof CompoundConfigElement)
                            || ((CompoundConfigElement) config.group)
                            .getElements().length > 0)) {
                        final String thisGroup = config.group.getValue(listable,
                                res.getParser());

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

                    add(new ListablePanel(listable, res.getParser(), config), "growx, pushx");
                }

                revalidate();
            } catch (ExecutionException ex) {

            } catch (InterruptedException ex) {

            }
        }

    }

}
