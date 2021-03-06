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

package uk.co.md87.evetool.ui.components;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import uk.co.md87.evetool.api.listable.Listable;
import uk.co.md87.evetool.api.listable.ListableConfig;
import uk.co.md87.evetool.api.listable.ListableParser;
import uk.co.md87.evetool.api.listable.UpdateListener;

/**
 *
 * TODO: Document ListablePanel
 * @author chris
 */
public class ListablePanel extends JPanel implements UpdateListener {

    /**
     * A version number for this class. It should be changed whenever the class
     * structure is changed (or anything else that would prevent serialized
     * objects being unserialized with the new class).
     */
    private static final long serialVersionUID = 10;

    private final Listable source;

    private final ListableParser parser;

    private final ListableConfig config;

    private final JLabel image, topLeft, topRight, bottomLeft, bottomRight;

    public ListablePanel(final Listable source, final ListableParser parser,
            final ListableConfig config) {
        super(new MigLayout("ins 0, fillx"));
        
        this.source = source;
        this.parser = parser;
        this.config = config;

        image = new JLabel();
        topLeft = new JLabel("?");
        topRight = new JLabel("?", JLabel.RIGHT);
        bottomLeft = new JLabel("?");
        bottomRight = new JLabel("?", JLabel.RIGHT);

        add(image, "spany 2, width 48, height 48");
        add(topLeft, "growx, pushx, gaptop 7");
        add(topRight, "growx, pushx, al right, wrap");
        add(bottomLeft, "growx, pushx");
        add(bottomRight, "growx, pushx, al right, gapbottom 8");

        source.addUpdateListener(this);

        updateLabels();
        updateImage();
    }

    protected void updateLabels() {
        topLeft.setText(config.topLeft.getValue(source, parser));
        topRight.setText(config.topRight.getValue(source, parser));
        bottomLeft.setText(config.bottomLeft.getValue(source, parser));
        bottomRight.setText(config.bottomRight.getValue(source, parser));
    }

    protected void updateImage() {
        image.setIcon(source.getImage());
    }

    /** {@inheritDoc} */
    @Override
    public void listableUpdated(final Listable listable) {
        updateLabels();
        updateImage();
    }
}
