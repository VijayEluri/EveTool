/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.md87.evetool.ui.workers;

import java.awt.Image;
import java.net.URL;
import java.util.concurrent.ExecutionException;
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
