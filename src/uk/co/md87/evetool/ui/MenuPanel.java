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

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import uk.co.md87.evetool.ui.components.MenuButton;
import uk.co.md87.evetool.ui.workers.PortraitLoaderWorker;

/**
 *
 * TODO: Document MenuPanel
 * @author chris
 */
public class MenuPanel extends JPanel {

    public MenuPanel() {
        super(new MigLayout("wrap 1, fillx, ins 4"));

        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.DARK_GRAY));
        setBackground(Color.GRAY);

        final JLabel label = new JLabel("Loading...");
        add(label, "height 192!, width 192!");
        new PortraitLoaderWorker(113499922, label).execute();

        add(new MenuButton("Overview"), "growx");
        add(new MenuButton("Skills"), "growx");
        add(new MenuButton("Assets"), "growx");
        add(new MenuButton("Settings"), "growx");
    }

}
