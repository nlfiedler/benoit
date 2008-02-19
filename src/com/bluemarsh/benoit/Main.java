/*********************************************************************
 *
 *      Copyright (C) 1999-2005 Nathan Fiedler
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
 * $Id: Main.java 2001 2005-09-12 02:08:06Z nfiedler $
 *
 ********************************************************************/

package com.bluemarsh.benoit;

import com.bluemarsh.benoit.model.BenoitNumber;
import com.bluemarsh.benoit.ui.HistoryAdapter;
import com.bluemarsh.benoit.ui.MainWindow;
import com.bluemarsh.benoit.ui.PositionTracker;
import com.bluemarsh.benoit.ui.RegionSelector;
import com.bluemarsh.benoit.ui.SetFrameMapper;
import com.bluemarsh.benoit.ui.StatusUpdater;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Frame;
import java.util.Vector;
import javax.swing.JComponent;

/**
 * Class Main is the bootstrap for Benoit.
 *
 * @author  Nathan Fiedler
 */
public class Main {
    /** List of the open Sets. */
    private static Vector openSets = new Vector();

    /**
     * Closes the Set associated with the given frame and destroys
     * its user interface.
     *
     * @param  frame  window associated with set.
     */
    public static void closeSet(Frame frame) {
        Set set = SetFrameMapper.getSetForFrame(frame);
        if (set == null) {
            throw new IllegalArgumentException("frame not mapped to set");
        }
        set.close();
        openSets.remove(set);
        frame.dispose();
        if (openSets.size() == 0) {
            // No more open sets, time to leave.
            System.exit(0);
        }
    }

    /**
     * Main method for Benoit program.
     *
     * @param  args  list of command-line arguments.
     */
    public static void main(String[] args) {
        // Start the program by creating a Set.
        newSet();
    }

    /**
     * Create a new Set and the user interface that goes with it.
     *
     * @return  new instance of Set, or null if error.
     */
    public static Set newSet() {
        // Construct the Set and MainWindow.
        Set set = new DefaultSet();
        final MainWindow mainWindow = new MainWindow();

        // Add the mappings.
        openSets.add(set);
        SetFrameMapper.addFrameSetMapping(mainWindow, set);

        // Build out the window contents and adapters.
        Container pane = mainWindow.getContentPane();
        pane.add(set.getUI(), BorderLayout.CENTER);
        PositionTracker pt = new PositionTracker();
        pane.add(pt.getUI(), BorderLayout.SOUTH);
        JComponent imageComp = set.getImageComponent();
        imageComp.addMouseMotionListener(pt);
        RegionSelector rs = new RegionSelector(set);
        imageComp.addMouseListener(rs);
        imageComp.addMouseMotionListener(rs);
        StatusUpdater su = new StatusUpdater(mainWindow);
        set.addListener(su);
        set.setNumberType(BenoitNumber.DOUBLE_TYPE);
        HistoryAdapter ha = new HistoryAdapter(mainWindow);
        set.addListener(ha);

        // Initialize and display.
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                mainWindow.pack();
                mainWindow.setVisible(true);
            }
        });
        return set;
    }
}
