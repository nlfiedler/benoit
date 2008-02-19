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
 * $Id: StatusUpdater.java 2003 2005-09-12 04:51:14Z nfiedler $
 *
 ********************************************************************/

package com.bluemarsh.benoit.ui;

import com.bluemarsh.benoit.Set;
import com.bluemarsh.benoit.SetEvent;
import com.bluemarsh.benoit.SetListener;
import com.bluemarsh.benoit.render.Renderer;
import com.bluemarsh.benoit.render.RenderEvent;
import com.bluemarsh.benoit.render.RenderListener;
import java.awt.Frame;

/**
 * Class StatusUpdater listeners to the renderer and indicates the
 * percent of the region that has been rendered so far.
 *
 * @author  Nathan Fiedler
 */
public class StatusUpdater implements RenderListener, SetListener {
    /** Frame in which to indicate status. */
    protected Frame frame;
    /** Renderer we are listening to. */
    protected Renderer renderer;

    /**
     * Constructs a StatusUpdater.
     *
     * @param  frame  frame to indicate status in.
     */
    public StatusUpdater(Frame frame) {
        this.frame = frame;
    }

    /**
     * Called whenever the image history has changed. This includes
     * simply moving forward and backward through the history.
     * Generally anything that would affect the back/forward buttons.
     *
     * @param  e  set update event.
     */
    public void historyChanged(SetEvent e) {
    }

    /**
     * Called whenever the rendered image has been updated.
     *
     * @param  e  render update event.
     */
    public void imageUpdated(RenderEvent e) {
        int pct = e.getPercentDone();
        frame.setTitle(Bundle.getString("AppTitle") + " - " +
                       pct + Bundle.getString("pctComplete"));
    }

    /**
     * Called whenever the renderer has changed.
     *
     * @param  e  set update event.
     */
    public void rendererChanged(SetEvent e) {
        if (renderer != null) {
            renderer.removeListener(this);
        }
        Set set = (Set) e.getSource();
        renderer = set.getRenderer();
        renderer.addListener(this);
    }
}
