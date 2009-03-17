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

package uk.co.md87.evetool.api.listable;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

/**
 *
 * TODO: Document ListableImpl
 * @author chris
 */
public class ListableImpl implements Listable {

    protected ImageIcon icon;

    protected final List<UpdateListener> listeners = new ArrayList<UpdateListener>();

    /** {@inheritDoc} */
    @Override
    public ImageIcon getImage() {
        return icon;
    }

    protected void updateImage(final ImageIcon newImage) {
        icon = newImage;
        
        fireUpdateListener();
    }

    protected void fireUpdateListener() {
        synchronized (listeners) {
            for (UpdateListener listener : listeners) {
                listener.listableUpdated(this);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void addUpdateListener(final UpdateListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void removeUpdateListener(final UpdateListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

}
