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
 * $Id: PositionTracker.java 2003 2005-09-12 04:51:14Z nfiedler $
 *
 ********************************************************************/

package com.bluemarsh.benoit.ui;

import com.bluemarsh.benoit.Set;
import com.bluemarsh.benoit.model.BenoitNumber;
import com.bluemarsh.benoit.model.Parameters;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

/**
 * Class PositionTracker is responsible for tracking the movement
 * of the mouse over the view, and translating that into region
 * coordinates, displaying those in a label.
 *
 * @author  Nathan Fiedler
 */
public class PositionTracker implements MouseMotionListener {
    /** Panel that contains our position labels. */
    protected JPanel positionPanel;
    /** Label that indicates the X mouse position. */
    protected JTextField positionXLabel;
    /** Label that indicates the Y mouse position. */
    protected JTextField positionYLabel;

    /**
     * Constructs a PositionTracker.
     */
    public PositionTracker() {
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        positionPanel = new JPanel(gbl);
        gbc.insets = new Insets(1, 1, 1, 1);

        // X position components
        JLabel xLabel = new JLabel("X:");
        gbl.setConstraints(xLabel, gbc);
        positionPanel.add(xLabel);

        positionXLabel = new JTextField();
        positionXLabel.setEditable(false);
        positionXLabel.setBorder(BorderFactory.createBevelBorder(
                                     BevelBorder.LOWERED));
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbl.setConstraints(positionXLabel, gbc);
        positionPanel.add(positionXLabel);

        // Spacer between the two halves.
        Component strut = Box.createHorizontalStrut(20);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbl.setConstraints(strut, gbc);
        positionPanel.add(strut);

        // Y position components
        JLabel yLabel = new JLabel("Y:");
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbl.setConstraints(yLabel, gbc);
        positionPanel.add(yLabel);

        positionYLabel = new JTextField();
        positionYLabel.setEditable(false);
        positionYLabel.setBorder(BorderFactory.createBevelBorder(
                                     BevelBorder.LOWERED));
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gbl.setConstraints(positionYLabel, gbc);
        positionPanel.add(positionYLabel);
    }

    /**
     * Get the component wrapper of this position tracker.
     *
     * @return  component.
     */
    public JComponent getUI() {
        return positionPanel;
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
        trackPosition(e);
    }

    /**
     * Invoked when the mouse button has been moved on a component
     * (with no buttons down).
     *
     * @param  e  mouse event.
     */
    public void mouseMoved(MouseEvent e) {
        trackPosition(e);
    }

    /**
     * Mouse has moved, track its position.
     *
     * @param  e  mouse event.
     */
    protected void trackPosition(MouseEvent e) {
        Set set = SetFrameMapper.getSetForEvent(e);
        Parameters params = set.getParameters();
        if (params != null) {
            Image image = set.getImage();
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            BenoitNumber x = params.transformX(e.getX(), width);
            BenoitNumber y = params.transformY(e.getY(), height);
            positionXLabel.setText(x.toString());
            positionYLabel.setText(y.toString());
        } else {
            positionXLabel.setText("");
            positionYLabel.setText("");
        }
    }
}
