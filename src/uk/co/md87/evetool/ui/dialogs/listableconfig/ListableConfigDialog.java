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

package uk.co.md87.evetool.ui.dialogs.listableconfig;

import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import uk.co.md87.evetool.ui.components.ListablePanel;
import uk.co.md87.evetool.ui.listable.Listable;
import uk.co.md87.evetool.ui.listable.ListableConfig;
import uk.co.md87.evetool.ui.listable.ListableConfig.BasicConfigElement;
import uk.co.md87.evetool.ui.listable.ListableConfig.CompoundConfigElement;
import uk.co.md87.evetool.ui.listable.ListableConfig.ConfigElement;
import uk.co.md87.evetool.ui.listable.ListableConfig.LiteralConfigElement;
import uk.co.md87.evetool.ui.listable.ListableParser;
import uk.co.md87.evetool.ui.pages.ListablePage;

/**
 *
 * TODO: Document ListableConfigDialog
 * @author chris
 */
public class ListableConfigDialog extends JDialog implements ActionListener,
        ItemListener, KeyListener {

    /**
     * A version number for this class. It should be changed whenever the class
     * structure is changed (or anything else that would prevent serialized
     * objects being unserialized with the new class).
     */
    private static final long serialVersionUID = 10;

    private final ListableConfig config;
    private final Listable sample;
    private final ListableParser parser;

    private final Set<String> retrievables;
    private final Map<String, List<JComponent>> components
            = new HashMap<String, List<JComponent>>();

    private volatile boolean complete = false;

    private final JPanel configPanel, previewPanel;
    private final ListablePanel panel;

    private final ListablePage page;

    private static ImageIcon editInactiveIcon, editActiveIcon;

    static {
        try {
            editInactiveIcon = new ImageIcon(ImageIO.read(ListableConfigDialog
                    .class.getResource("/uk/co/md87/evetool/ui/res/edit-inactive.png")));
            editActiveIcon = new ImageIcon(ImageIO.read(ListableConfigDialog
                    .class.getResource("/uk/co/md87/evetool/ui/res/edit.png")));
        } catch (IOException ex) {
            Logger.getLogger(ListableConfigDialog.class.getName())
                    .log(Level.WARNING, "Unable to load images", ex);
        }
    }

    public ListableConfigDialog(final Window window, final ListablePage page,
            final ListableConfig config, final Listable sample) {
        super(window, "Display Configuration", ModalityType.APPLICATION_MODAL);

        setLayout(new MigLayout("wrap 1, fill", "[fill]", "[|fill|fill]"));

        this.page = page;
        this.config = config.clone();
        this.sample = sample;
        this.parser = new ListableParser(sample.getClass());
        
        this.retrievables = parser.getRetrievableNames();

        this.configPanel = new JPanel(new MigLayout("fill", "[fill]"));
        this.previewPanel = new JPanel(new MigLayout("fill", "[fill]", "[fill]"));

        configPanel.setBorder(BorderFactory.createTitledBorder("Configuration"));
        previewPanel.setBorder(BorderFactory.createTitledBorder("Preview"));

        this.panel = new ListablePanel(sample, parser, this.config);
        
        previewPanel.add(panel);

        final JButton cancelButton = new JButton("Cancel");
        final JButton okButton = new JButton("OK");

        cancelButton.addActionListener(this);
        okButton.addActionListener(this);

        add(configPanel);
        add(previewPanel);
        add(cancelButton, "split");
        add(okButton, "split");

        initConfigPanel();
        layoutConfigPanel();

        pack();

        setLocationRelativeTo(window);
        setResizable(false);
    }

    protected void initConfigPanel() {
        components.put("tl", getComponents(config.topLeft));
        components.put("tr", getComponents(config.topRight));
        components.put("bl", getComponents(config.bottomLeft));
        components.put("br", getComponents(config.bottomRight));
        components.put("+group", getComponents(config.group));
        components.put("+sort", getComponents(config.sortOrder));
        
        complete = true;
    }

    protected void layoutConfigPanel() {
        configPanel.removeAll();
        
        JButton editButton;

        final List<String> keyset = new ArrayList<String>(components.keySet());
        Collections.sort(keyset);

        for (int i = 0; i < keyset.size(); i++) {
            final String key = keyset.get(i);

            editButton = new JButton(editInactiveIcon);
            editButton.setRolloverIcon(editActiveIcon);
            editButton.setBorder(BorderFactory.createEmptyBorder());
            editButton.setOpaque(false);
            editButton.setContentAreaFilled(false);
            editButton.setMaximumSize(new Dimension(20, 20));
            
            editButton.addActionListener(new ButtonActionListener(key));
            configPanel.add(new JLabel(getText(key), JLabel.RIGHT));
            addComponents(key);
            configPanel.add(editButton, "span, al right, gapleft 10");

            if (key.length() > 2 && i < keyset.size() - 1
                    && !keyset.get(i + 1).startsWith(key.substring(0, 4))) {
                configPanel.add(new JSeparator(),
                        "gaptop 10, gapbottom 10, newline, span, growx");
            }
        }
        
        configPanel.revalidate();
        pack();
    }

    protected String getText(final String key) {
        if (key.equals("+group")) {
            return "Group by:";
        }

        if (key.equals("+sort")) {
            return "Sort by:";
        }

        final StringBuilder builder = new StringBuilder();
        
        builder.append(key.startsWith("t") ? "Top" : "Bottom");
        builder.append(' ');
        builder.append(key.endsWith("r") ? "right" : "left");
        builder.append(':');

        return builder.toString();
    }

    protected void addComponents(final String location) {
        for (JComponent component : components.get(location)) {
            configPanel.add(component, "growy, width 100!");
        }
    }

    protected List<JComponent> getComponents(final ConfigElement element) {
        final List<JComponent> res = new ArrayList<JComponent>();

        if (element instanceof LiteralConfigElement) {
            final JTextField tf = new JTextField(((LiteralConfigElement) element).getText());
            tf.addKeyListener(this);
            res.add(tf);
        } else if (element instanceof BasicConfigElement) {
            final JComboBox box = new JComboBox(retrievables.toArray());
            box.addItemListener(this);
            box.setSelectedItem(((BasicConfigElement) element).getName());
            res.add(box);
        } else {
            for (ConfigElement subElement : ((CompoundConfigElement) element).getElements()) {
                res.addAll(getComponents(subElement));
            }
        }

        return res;
    }

    protected void rebuildConfig() {        
        for (String loc : components.keySet()) {
            final List<ConfigElement> elements = new ArrayList<ConfigElement>();

            for (JComponent component : components.get(loc)) {
                if (component instanceof JTextField) {
                    elements.add(new LiteralConfigElement(((JTextField) component).getText()));
                } else if (component instanceof JComboBox) {
                    elements.add(new BasicConfigElement((String)
                            ((JComboBox) component).getSelectedItem()));
                }
            }

            final ConfigElement res = new CompoundConfigElement(
                    elements.toArray(new ConfigElement[0]));

            if (loc.equals("tl")) {
                config.topLeft = res;
            } else if (loc.equals("tr")) {
                config.topRight = res;
            } else if (loc.equals("br")) {
                config.bottomRight = res;
            } else if (loc.equals("bl")) {
                config.bottomLeft = res;
            } else if (loc.equals("+group")) {
                config.group = res;
            } else if (loc.equals("+sort")) {
                config.sortOrder = res;
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void itemStateChanged(final ItemEvent e) {
        if (complete) {
            rebuildConfig();
            panel.listableUpdated(sample);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void keyTyped(final KeyEvent e) {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void keyPressed(final KeyEvent e) {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void keyReleased(final KeyEvent e) {
        rebuildConfig();
        panel.listableUpdated(sample);
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (((JButton) e.getSource()).getText().equals("OK")) {
            page.setConfig(config);
        }

        dispose();
    }

    private class ButtonActionListener implements ActionListener {

        private final String location;

        public ButtonActionListener(final String location) {
            this.location = location;
        }

        /** {@inheritDoc} */
        @Override
        public void actionPerformed(final ActionEvent e) {
            final JPopupMenu menu = new JPopupMenu();

            JMenuItem mi = new JMenuItem("New string");
            mi.addActionListener(new MenuActionListener(location, true));
            menu.add(mi);

            mi = new JMenuItem("New variable");
            mi.addActionListener(new MenuActionListener(location, false));
            menu.add(mi);

            if (!components.get(location).isEmpty()) {
                menu.add(new JSeparator());
            }

            for (JComponent component : components.get(location)) {
                final String text = component instanceof JTextField ?
                    ((JTextField) component).getText() :
                    String.valueOf(((JComboBox) component).getSelectedItem());
                mi = new JMenuItem("Remove '" + text + "'");
                mi.addActionListener(new MenuRemoveActionListener(location, component));
                menu.add(mi);
            }

            menu.show((JComponent) e.getSource(), 0,
                    ((JComponent) e.getSource()).getHeight());
        }
    }

    private class MenuRemoveActionListener implements ActionListener {

        private final String location;

        private final JComponent component;

        public MenuRemoveActionListener(String location, JComponent component) {
            this.location = location;
            this.component = component;
        }

        /** {@inheritDoc} */
        @Override
        public void actionPerformed(ActionEvent e) {
            components.get(location).remove(component);
            layoutConfigPanel();
            rebuildConfig();
            panel.listableUpdated(sample);
        }

    }

    private class MenuActionListener implements ActionListener {

        private final String location;

        private final boolean isString;

        public MenuActionListener(final String location, final boolean isString) {
            this.location = location;
            this.isString = isString;
        }

        /** {@inheritDoc} */
        @Override
        public void actionPerformed(final ActionEvent e) {
            if (isString) {
                final JTextField tf = new JTextField();
                tf.addKeyListener(ListableConfigDialog.this);
                components.get(location).add(tf);
            } else {
                final JComboBox box = new JComboBox(retrievables.toArray());
                box.addItemListener(ListableConfigDialog.this);
                components.get(location).add(box);
            }

            layoutConfigPanel();
            rebuildConfig();
            panel.listableUpdated(sample);
        }
    }

}
