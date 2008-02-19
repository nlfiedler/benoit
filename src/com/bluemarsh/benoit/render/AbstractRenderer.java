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
 * $Id: AbstractRenderer.java 2001 2005-09-12 02:08:06Z nfiedler $
 *
 ********************************************************************/

package com.bluemarsh.benoit.render;

import javax.swing.event.EventListenerList;

/**
 * Defines the interface for all renderers.
 *
 * @author  Nathan Fiedler
 */
public abstract class AbstractRenderer implements Renderer {
    /** List of render listeners. */
    private EventListenerList renderListeners;

    /**
     * Constructs a AbstractRenderer.
     */
    public AbstractRenderer() {
        renderListeners = new EventListenerList();
    }

    /**
     * Adds the given listener to the renderer's list of listeners.
     *
     * @param  l  listener to add.
     */
    public void addListener(RenderListener l) {
        renderListeners.add(RenderListener.class, l);
    }

    /**
     * Let all the render listeners know that the rendered image
     * has been updated.
     *
     * @param  percent  percentage of region rendered.
     */
    protected void fireUpdate(int percent) {
        if (renderListeners == null) {
            return;
        }

        // Create the render update event.
        RenderEvent re = new RenderEvent(this, percent);
        // Get the listener list as class/instance pairs.
        Object[] listeners = renderListeners.getListenerList();
        // Process the listeners last to first.
        // List is in pairs: class, instance
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == RenderListener.class) {
                RenderListener rl = (RenderListener) listeners[i + 1];
                rl.imageUpdated(re);
            }
        }
    }

    /**
     * Removes the given listener from the renderer's list of listeners.
     *
     * @param  l  listener to remove.
     */
    public void removeListener(RenderListener l) {
        renderListeners.remove(RenderListener.class, l);
    }
}
