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

package uk.co.md87.evetool.ui.workers;

import java.awt.Image;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingWorker;

/**
 *
 * TODO: Document PortraitLoaderWorker
 * TODO: Cache images locally
 * @author chris
 */
public class PortraitLoaderWorker extends SwingWorker<ImageIcon, Object> {

    private final int charId;
    private final JLabel target;
    private final int size;

    public PortraitLoaderWorker(final int charId, final JLabel target, final int size) {
        this.charId = charId;
        this.target = target;
        this.size = size;
    }

    @Override
    protected ImageIcon doInBackground() throws Exception {
        return new ImageIcon(ImageIO.read(
                    new URL("http://img.eve.is/serv.asp?s=256&c=" + charId))
                    .getScaledInstance(size, size, Image.SCALE_SMOOTH));
    }

    @Override
    protected void done() {
        try {
            target.setIcon(get());
        } catch (Exception ex) {
            Logger.getLogger(PortraitLoaderWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
