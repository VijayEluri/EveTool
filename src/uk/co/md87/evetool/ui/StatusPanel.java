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
import uk.co.md87.evetool.Main;
import uk.co.md87.evetool.api.io.ApiDownloader;
import uk.co.md87.evetool.api.io.QueueSizeListener;

/**
 *
 * TODO: Document StatusPanel
 * @author chris
 */
public class StatusPanel extends JPanel implements QueueSizeListener {

    /**
     * A version number for this class. It should be changed whenever the class
     * structure is changed (or anything else that would prevent serialized
     * objects being unserialized with the new class).
     */
    private static final long serialVersionUID = 10;

    private final JLabel leftLabel, centreLabel, rightLabel;

    public StatusPanel(final MainWindow window) {
        super(new MigLayout());

        leftLabel = new JLabel("Welcome to EVE Tool", JLabel.LEFT);
        centreLabel = new JLabel("", JLabel.CENTER);
        rightLabel = new JLabel(Main.version, JLabel.RIGHT);

        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.DARK_GRAY));
        setBackground(Color.GRAY);
        add(leftLabel, "push, grow");
        add(centreLabel, "push, grow");
        add(rightLabel, "push, grow");

        ApiDownloader.addQueueSizeListener(this);
    }

    public void queueSizeUpdate(final int size) {
        centreLabel.setText(size == 0 ? "" : 
            (size + " queued request" + (size == 1 ? "" : "s")));
    }

}
