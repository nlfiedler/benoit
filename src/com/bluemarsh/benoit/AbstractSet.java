/*********************************************************************
 *
 *      Copyright (C) 2002-2005 Nathan Fiedler
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * $Id: AbstractSet.java 2001 2005-09-12 02:08:06Z nfiedler $
 *
 ********************************************************************/

package com.bluemarsh.benoit;

import com.bluemarsh.benoit.model.Parameters;
import java.awt.Image;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import javax.swing.event.EventListenerList;

/**
 * An abstract implementation of a Set.
 *
 * @author  Nathan Fiedler
 */
public abstract class AbstractSet implements Set {
    /** Event type: renderer changed. */
    protected static final int RENDERER_CHANGED = 1;
    /** Event type: history changed. */
    protected static final int HISTORY_CHANGED = 2;
    /** History of visited regions of the set. */
    protected List historyList;
    /** Offset within the history marking the current position.
     * A value of -1 indicates there is no history. */
    protected int historyIndex;
    /** List of set listeners. */
    protected EventListenerList setListeners;

    /**
     * Constructs an AbstractSet.
     */
    public AbstractSet() {
        historyList = new ArrayList();
        historyIndex = -1;
        setListeners = new EventListenerList();
    } // AbstractSet

    /**
     * Adds the given listener to the set's list of listeners.
     *
     * @param  l  listener to add.
     */
    public void addListener(SetListener l) {
        setListeners.add(SetListener.class, l);
    } // addListener

    /**
     * Save the image in the history cache.
     *
     * @param  image  image to cache.
     */
    protected void cacheImage(Image image) {
        ListEntry entry = (ListEntry) historyList.get(historyIndex);
        // Save the current image in case we should show it again.
        if (entry.cachedImage == null) {
            // That is, if it is not already saved.
            entry.cachedImage = new SoftReference(image);
        }
    } // cacheImage

    /**
     * Closes the Set in preparation for non-use.
     */
    public void close() {
    } // close

    /**
     * Let all the set listeners know that something has changed.
     *
     * @param  type  type of the event.
     */
    protected void fireChange(int type) {
        if (setListeners == null) {
            return;
        }

        // Create the render update event.
        SetEvent e = new SetEvent(this);
        // Get the listener list as class/instance pairs.
        Object[] listeners = setListeners.getListenerList();
        // Process the listeners last to first.
        // List is in pairs: class, instance
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == SetListener.class) {
                SetListener l = (SetListener) listeners[i + 1];
                if (type == RENDERER_CHANGED) {
                    l.rendererChanged(e);
                } else if (type == HISTORY_CHANGED) {
                    l.historyChanged(e);
                } else {
                    throw new IllegalArgumentException("invalid event type");
                }
            }
        }
    } // fireChange

    /**
     * Return true if the current parameters are the last parameters
     * in the history list.
     *
     * @return  true if at end of history.
     */
    public boolean historyAtEnd() {
        return historyIndex == (historyList.size() - 1);
    } // historyAtEnd

    /**
     * Return true if the current parameters are the first parameters
     * in the history list.
     *
     * @return  true if at start of history.
     */
    public boolean historyAtStart() {
        return historyIndex <= 0;
    } // historyAtStart

    /**
     * Perform the common bit of the prev/next history logic.
     */
    protected void historyCommon() {
        renderStop();
        ListEntry entry = (ListEntry) historyList.get(historyIndex);
        Image image = entry.getImage();
        if (image == null) {
            // Cached image disappeared, have to render again.
            renderLow(entry.parameters);
        } else {
            // Show the cached image.
            setParameters(entry.parameters);
            showImage(image);
        }
        fireChange(HISTORY_CHANGED);
    } // historyCommon

    /**
     * Redraw the region of the set represented by the parameters in
     * the next slot of the parameters history list.
     */
    public void historyNext() {
        if (historyAtEnd()) {
            throw new IllegalStateException("already at end");
        }
        historyIndex++;
        historyCommon();
    } // historyNext

    /**
     * Redraw the region of the set represented by the parameters in
     * the previous slot of the parameters history list.
     */
    public void historyPrev() {
        if (historyAtStart()) {
            throw new IllegalStateException("already at start");
        }
        historyIndex--;
        historyCommon();
    } // historyPrev

    /**
     * Removes the given listener from the set's list of listeners.
     *
     * @param  l  listener to remove.
     */
    public void removeListener(SetListener l) {
        setListeners.remove(SetListener.class, l);
    } // removeListener

    /**
     * Render a region of the set. The boundaries of that region
     * are specified in the call to <code>setParameters()</code>.
     * The parameters will be inserted into the history list at
     * the current location, obliterating any subsequent history.
     */
    public void render() {
        renderStop();
        // Are we at the end of the history?
        if (!historyAtEnd()) {
            // No, have to erase obsolete history.
            ListIterator iter = historyList.listIterator(historyIndex + 1);
            while (iter.hasNext()) {
                iter.next();
                iter.remove();
            }
        }

        Parameters params = getParameters();
        boolean saveEntry = true;

        if (historyList.size() > 0) {
            // Determine if we should save these parameters to the history.
            ListEntry entry = (ListEntry) historyList.get(historyIndex);
            if (params.equals(entry.parameters)) {
                saveEntry = false;
            }
        }

        if (saveEntry) {
            // Get the current parameters and save them.
            ListEntry entry = new ListEntry(params, null);
            historyList.add(entry);
            historyIndex++;
        }

        fireChange(HISTORY_CHANGED);
        renderLow(params);
    } // render

    /**
     * Render the region of the set given by the parameters.
     * This does not manage the parameters history list.
     *
     * @param  params  region of the set to render.
     */
    public abstract void renderLow(Parameters params);

    /**
     * Show the given image in preference to the one presently shown.
     *
     * @param  image  image to take place of current image.
     */
    protected abstract void showImage(Image image);

    /**
     * Wrapper for the Parameters and rendered image in the history list.
     */
    protected class ListEntry {
        /** Parameters */
        public Parameters parameters;
        /** Cached image */
        public SoftReference cachedImage;

        /**
         * Constructs a ListEntry.
         *
         * @param  parameters   Parameters instance.
         * @param  cachedImage  cached image, wrapped in a SoftReference.
         */
        public ListEntry(Parameters parameters, SoftReference cachedImage) {
            this.parameters = parameters;
            this.cachedImage = cachedImage;
        } // ListEntry

        /**
         * Retrieve the cached image, if any.
         *
         * @return  cached image, or null if none.
         */
        public Image getImage() {
            return cachedImage == null ? null : (Image) cachedImage.get();
        } // getImage
    } // ListEntry
} // AbstractSet
