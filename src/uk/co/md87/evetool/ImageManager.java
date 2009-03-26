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

package uk.co.md87.evetool;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import uk.co.md87.evetool.api.io.QueueSizeListener;

/**
 *
 * TODO: Document ImageManager
 * @author chris
 */
public class ImageManager {

    protected static final List<String> REQUESTS = new ArrayList<String>();

    private static final List<QueueSizeListener> listeners = new ArrayList<QueueSizeListener>();

    private static final AtomicInteger queueSize = new AtomicInteger(0);

    protected final String cacheDir;

    public ImageManager(final String cacheDir) {
        this.cacheDir = cacheDir;

        final File dir = new File(cacheDir);
        if (!dir.isDirectory() && !dir.mkdirs()) {
            Logger.getLogger(ImageManager.class.getName()).log(Level.SEVERE,
                    "Unable to create image cache directory");
        }
    }

    public Image getImage(final ImageType type, final Object ... arguments) throws IOException {
        final String url = type.getUrl(arguments);

        synchronized (REQUESTS) {
            fireQueueSizeChange(queueSize.incrementAndGet());

            while (REQUESTS.contains(url)) {
                try {
                    REQUESTS.wait();
                } catch (InterruptedException ex) {
                    // Ignore
                }
            }

            REQUESTS.add(url);
        }

        try {
            final Image res = ImageIO.read(new URL(url));
            return res;
        } finally {
            synchronized (REQUESTS) {
                fireQueueSizeChange(queueSize.decrementAndGet());
                
                REQUESTS.remove(url);
                REQUESTS.notifyAll();
            }
        }
    }

    public static void addQueueSizeListener(final QueueSizeListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
            listener.queueSizeUpdate(queueSize.get());
        }
    }

    protected static void fireQueueSizeChange(final int queueSize) {
        synchronized (listeners) {
            for (QueueSizeListener listener : listeners) {
                listener.queueSizeUpdate(queueSize);
            }
        }
    }
    
}
