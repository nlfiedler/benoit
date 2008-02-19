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
 * $Id: Set.java 2001 2005-09-12 02:08:06Z nfiedler $
 *
 ********************************************************************/

package com.bluemarsh.benoit;

import com.bluemarsh.benoit.model.Parameters;
import com.bluemarsh.benoit.render.Renderer;
import java.awt.Image;
import javax.swing.JComponent;

/**
 * A Set represents a Mandelbrot set. It provides the methods for
 * accessing the coordinates of the region being viewed, rendering
 * the region to a color bitmap, and provides the graphical component
 * for displaying the set.
 *
 * @author  Nathan Fiedler
 */
public interface Set {

    /**
     * Adds the given listener to the set's list of listeners.
     *
     * @param  l  listener to add.
     */
    void addListener(SetListener l);

    /**
     * Closes the Set in preparation for non-use.
     */
    void close();

    /**
     * Retrieve the last rendered image (does not have to be completely
     * rendered yet).
     *
     * @return  image of current region.
     */
    Image getImage();

    /**
     * Return the JComponent wrapper to the rendered image. This
     * reference must remain valid indefinitely as it is used to
     * listen for mouse motion events over this component.
     *
     * @return  our image component.
     */
    JComponent getImageComponent();

    /**
     * Gets the parameters used for rendering the current region
     * of the set.
     *
     * @return  current region boundaries.
     */
    Parameters getParameters();

    /**
     * Returns a reference to this Set's renderer.
     *
     * @return  renderer for this Set.
     */
    Renderer getRenderer();

    /**
     * Return the JComponent wrapper to this set.
     *
     * @return  our interface component.
     */
    JComponent getUI();

    /**
     * Return true if the current parameters are the first parameters
     * in the history list.
     *
     * @return  true if at start of history.
     */
    boolean historyAtStart();

    /**
     * Return true if the current parameters are the last parameters
     * in the history list.
     *
     * @return  true if at end of history.
     */
    boolean historyAtEnd();

    /**
     * Redraw the region of the set represented by the parameters in
     * the next slot of the parameters history list.
     */
    void historyNext();

    /**
     * Redraw the region of the set represented by the parameters in
     * the previous slot of the parameters history list.
     */
    void historyPrev();

    /**
     * Removes the given listener from the set's list of listeners.
     *
     * @param  l  listener to remove.
     */
    void removeListener(SetListener l);

    /**
     * Render a region of the set. The boundaries of that region
     * are specified in the call to <code>setParameters()</code>.
     */
    void render();

    /**
     * Cause the current rendering to stop. Has no affect if rendering
     * has already completed.
     */
    void renderStop();

    /**
     * Sets the numeric precision used by this Set.
     *
     * @param  type  numeric type (one of the BenoitNumber constants).
     */
    void setNumberType(int type);

    /**
     * Sets the parameters used for rendering the set. This does not
     * cause an update of the view.
     *
     * @param  params  new region boundaries.
     */
    void setParameters(Parameters params);

    /**
     * Show an indication that the given region is selected in the
     * rendered image. The coordinates are with respect to the image.
     *
     * @param  left    left-most coordinate of selection.
     * @param  top     top-most coordinate of selection.
     * @param  right   right-most coordinate of selection.
     * @param  bottom  bottom-most coordinate of selection.
     */
    void showSelection(int left, int top, int right, int bottom);
}
