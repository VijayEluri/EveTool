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
import java.awt.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;

import javax.swing.JLabel;
import javax.swing.JPanel;
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

/**
 *
 * TODO: Document ListableConfigDialog
 * @author chris
 */
public class ListableConfigDialog extends JDialog {

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

    private final JPanel configPanel, previewPanel;
    private final ListablePanel panel;

    public ListableConfigDialog(final Window owner, final ListableConfig config,
            final Listable sample) {
        super(owner, "EVE Tool - display configuration", ModalityType.APPLICATION_MODAL);

        setLayout(new MigLayout("wrap 1, fill", "[fill]", "[|fill|fill]"));

        this.config = config;
        this.sample = sample;
        this.parser = new ListableParser(sample.getClass());
        
        this.retrievables = parser.getRetrievableNames();
        
        this.configPanel = new JPanel(new MigLayout("fill", "[fill]"));
        this.previewPanel = new JPanel(new MigLayout("fill", "[fill]", "[fill]"));
        
        configPanel.setBorder(BorderFactory.createTitledBorder("Configuration"));
        previewPanel.setBorder(BorderFactory.createTitledBorder("Preview"));

        this.panel = new ListablePanel(sample, parser, config);
        
        previewPanel.add(panel);

        add(new JLabel("Blurby blurb McBlurb"));
        add(configPanel);
        add(previewPanel);

        layoutConfigPanel();

        pack();

        setLocationRelativeTo(owner);
        setResizable(false);
    }

    protected void layoutConfigPanel() {
        configPanel.add(new JLabel("Top left:", JLabel.RIGHT));
        addComponents(config.topLeft);
        configPanel.add(new JButton("+"));
        configPanel.add(new JLabel("Bottom left:", JLabel.RIGHT), "newline");
        addComponents(config.bottomLeft);
        configPanel.add(new JButton("+"));
        configPanel.add(new JLabel("Top right:", JLabel.RIGHT), "newline");
        addComponents(config.topRight);
        configPanel.add(new JButton("+"));
        configPanel.add(new JLabel("Bottom right:", JLabel.RIGHT), "newline");
        addComponents(config.bottomRight);
        configPanel.add(new JButton("+"));
    }

    protected void addComponents(final ConfigElement element) {
        final List<JComponent> components = getComponents(element);

        String firstText = ", split " + components.size();
        for (JComponent component : components) {
            final int compWidth = component instanceof JComboBox ? 150 : 100;
            configPanel.add(component, "width " + compWidth + "!" + firstText);
            firstText = "";
        }
    }

    protected List<JComponent> getComponents(final ConfigElement element) {
        final List<JComponent> components = new ArrayList<JComponent>();

        if (element instanceof LiteralConfigElement) {
            components.add(new JTextField(((LiteralConfigElement) element).getText()));
        } else if (element instanceof BasicConfigElement) {
            final JComboBox box = new JComboBox(retrievables.toArray());
            box.setSelectedItem(((BasicConfigElement) element).getName());
            components.add(box);
        } else {
            for (ConfigElement subElement : ((CompoundConfigElement) element).getElements()) {
                components.addAll(getComponents(subElement));
            }
        }

        return components;
    }

}
