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

import java.awt.Color;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import uk.co.md87.evetool.ui.components.MenuButton;
import uk.co.md87.evetool.ui.data.AccountChar;
import uk.co.md87.evetool.ui.workers.PortraitLoaderWorker;

/**
 *
 * TODO: Document MenuPanel
 * @author chris
 */
public class MenuPanel extends JPanel implements ActionListener {

    /**
     * A version number for this class. It should be changed whenever the class
     * structure is changed (or anything else that would prevent serialized
     * objects being unserialized with the new class).
     */
    private static final long serialVersionUID = 10;

    protected final JLabel portrait;
    protected final MainWindow window;
    protected final MenuButton overviewButton;
    protected final MenuButton skillsButton;
    protected final MenuButton settingsButton;
    protected final Map<String, ContentPanel.Page> pages;

    public MenuPanel(final MainWindow window, final Map<String, ContentPanel.Page> pages) {
        super(new MigLayout("wrap 1, fillx, ins 4"));

        this.window = window;
        this.pages = pages;

        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.DARK_GRAY));
        setBackground(Color.GRAY);

        portrait = new JLabel("Select a character", JLabel.CENTER);
        add(portrait, "height 192!, width 192!");

        overviewButton = new MenuButton("Overview", this);

        skillsButton = new MenuButton("Skills", this);
        skillsButton.setEnabled(false);

        settingsButton = new MenuButton("Settings", this);
        settingsButton.setEnabled(false);

        for (Map.Entry<String, ContentPanel.Page> entry : pages.entrySet()) {
            final MenuButton button = new MenuButton(entry.getKey(), this);

            if (!entry.getValue().isReady()) {
                button.setEnabled(false);
            }

            add(button, "growx");
        }
    }

    public void setSelectedChar(final AccountChar newChar) {
        portrait.setIcon(null);
        portrait.setText("Loading...");
        new PortraitLoaderWorker(newChar.getCharInfo().getId(),
                portrait, 192).execute();

        skillsButton.setEnabled(true);
    }

    /** {@inheritDoc} */
    public void actionPerformed(final ActionEvent e) {
        window.setPage(((MenuButton) e.getSource()).getText());
    }

}
