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

package uk.co.md87.evetool.ui.data;

import java.awt.Image;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.SwingWorker;

import uk.co.md87.evetool.ImageManager;
import uk.co.md87.evetool.ImageType;
import uk.co.md87.evetool.api.wrappers.CharacterSheet;
import uk.co.md87.evetool.api.wrappers.data.BasicShipInfo;
import uk.co.md87.evetool.api.listable.ListableImpl;
import uk.co.md87.evetool.api.listable.Retrievable;

/**
 *
 * TODO: Document BasicShipInfoSurrogate
 * @author chris
 */
public class BasicShipInfoSurrogate extends ListableImpl {

    private final BasicShipInfo info;
    private final CharacterSheet sheet;
    private final ImageManager manager;

    public BasicShipInfoSurrogate(final ImageManager manager,
            final BasicShipInfo info, final CharacterSheet sheet) {
        this.manager = manager;
        this.info = info;
        this.sheet = sheet;

        new ImageWorker().execute();
    }

    @Retrievable(deferred=true)
    public BasicShipInfo getInfo() {
        return info;
    }

    @Retrievable
    public boolean canFly() {
        return sheet.hasSkills(info.getRequirements());
    }

    private class ImageWorker extends SwingWorker<Image,Void> {

        @Override
        protected Image doInBackground() throws Exception {
            return manager.getImage(ImageType.SHIP, info.getID())
                    .getScaledInstance(48, 48, Image.SCALE_SMOOTH);
        }

        @Override
        protected void done() {
            try {
                updateImage(new ImageIcon(get()));
            } catch (InterruptedException ex) {
                Logger.getLogger(BasicShipInfoSurrogate.class.getName())
                        .log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(BasicShipInfoSurrogate.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }

    }

}
