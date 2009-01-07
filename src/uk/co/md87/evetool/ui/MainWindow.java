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

import java.awt.Dimension;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;
import uk.co.md87.evetool.api.EveApi;

/**
 *
 * TODO: Document MainWindow
 * @author chris
 */
public class MainWindow extends JFrame {

    private final EveApi api;

    public MainWindow(final EveApi api) {
        super("EVE Tool - Initialising...");

        this.api = api;
        
        setLayout(new MigLayout("insets 0, fill, wrap 2", "[]0[fill,grow]",
                "[fill,grow]0[]"));
        addComponents();
        setMinimumSize(new Dimension(300, 300));
        setSize(new Dimension(800, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        try {
            setIconImage(ImageIO.read(getClass().getResource("res/icon.png")));
        } catch (IOException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void addComponents() {
        add(new MenuPanel(), "width 201!");
        add(new ContentPanel(api));
        add(new StatusPanel(), "growx, span, height 30!");
    }

}
