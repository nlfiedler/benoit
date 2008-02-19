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
 * $Id: RegionSelector.java 2003 2005-09-12 04:51:14Z nfiedler $
 *
 ********************************************************************/

package com.bluemarsh.benoit.ui;

import com.bluemarsh.benoit.Set;
import com.bluemarsh.benoit.model.BenoitNumber;
import com.bluemarsh.benoit.model.Parameters;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;

/**
 * Class PositionTracker is responsible for tracking the movement
 * of the mouse over the view, and translating that into region
 * coordinates, displaying those in a label.
 *
 * @author  Nathan Fiedler
 */
public class RegionSelector implements MouseListener, MouseMotionListener {
    /** The Set we are associated with. */
    protected Set set;
    /** X position where mouse was pressed. */
    protected int mousePressedX;
    /** Y position where mouse was pressed. */
    protected int mousePressedY;

    /**
     * Constructs a RegionSelector for the given Set.
     *
     * @param  set  associated Set.
     */
    public RegionSelector(Set set) {
        this.set = set;
    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     */
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * Invoked when a mouse button is pressed on a component and then
     * dragged.  <code>MOUSE_DRAGGED</code> events will continue to be
     * delivered to the component where the drag originated until the
     * mouse button is released (regardless of whether the mouse position
     * is within the bounds of the component).
     *
     * <p>Due to platform-dependent Drag&Drop implementations,
     * <code>MOUSE_DRAGGED</code> events may not be delivered during a
     * native Drag&Drop operation.</p>
     *
     * @param  e  mouse event.
     */
    public void mouseDragged(MouseEvent e) {
        int originalX = mousePressedX;
        int originalY = mousePressedY;
        int latestX = e.getX();
        int latestY = e.getY();

        // Swap the original and latest values, if necessary.
        if (latestX < originalX) {
            int temp = originalX;
            originalX = latestX;
            latestX = temp;
        }
        if (latestY < originalY) {
            int temp = originalY;
            originalY = latestY;
            latestY = temp;
        }

        // Show the selection rectangle.
        set.showSelection(originalX, originalY, latestX, latestY);
    }

    /**
     * Invoked when the mouse enters a component.
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Invoked when the mouse exits a component.
     */
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Invoked when the mouse button has been moved on a component
     * (with no buttons down).
     */
    public void mouseMoved(MouseEvent e) {
    }

    /**
     * Mouse button was pressed.
     *
     * @param  e  mouse event
     */
    public void mousePressed(MouseEvent e) {
        mousePressedX = e.getX();
        mousePressedY = e.getY();
    }

    /**
     * Mouse button was released.
     *
     * @param  e  mouse event
     */
    public void mouseReleased(MouseEvent e) {
        // Erase the selection rectangle.
        set.showSelection(0, 0, 0, 0);

        int originalX = mousePressedX;
        int originalY = mousePressedY;
        int latestX = e.getX();
        int latestY = e.getY();

        // Swap the original and latest values, if necessary.
        if (latestX < originalX) {
            int temp = originalX;
            originalX = latestX;
            latestX = temp;
        }
        if (latestY < originalY) {
            int temp = originalY;
            originalY = latestY;
            latestY = temp;
        }

        // Transform the mouse coordinates to set coordinates.
        JComponent view = (JComponent) e.getSource();
        int width = view.getWidth();
        int height = view.getHeight();
        Parameters params = set.getParameters();
        BenoitNumber x1 = params.transformX(originalX, width);
        BenoitNumber y1 = params.transformY(originalY, height);
        BenoitNumber x2 = params.transformX(latestX, width);
        BenoitNumber y2 = params.transformY(latestY, height);

        // Set the new parameters and render the region.
        params = new Parameters(x1, x2, y1, y2);
        set.setParameters(params);
        set.render();
    }
}
