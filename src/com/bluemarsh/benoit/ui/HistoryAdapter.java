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
 * $Id: HistoryAdapter.java 2003 2005-09-12 04:51:14Z nfiedler $
 *
 ********************************************************************/

package com.bluemarsh.benoit.ui;

import com.bluemarsh.benoit.Set;
import com.bluemarsh.benoit.SetEvent;
import com.bluemarsh.benoit.SetListener;
import javax.swing.JButton;

/**
 * Class HistoryAdapter listens for changes in the Set history and
 * enables and disables the back and forward buttons as necessary.
 *
 * @author  Nathan Fiedler
 */
public class HistoryAdapter implements SetListener {
    /** Back button. */
    protected JButton backButton;
    /** Forward button. */
    protected JButton forwardButton;

    /**
     * Creates a HistoryAdapter that creates a back and forward button
     * and adds them to the given window's toolbar.
     *
     * @param  frame  main window.
     */
    public HistoryAdapter(MainWindow frame) {
        backButton = frame.createToolbarButton("back");
        backButton.setEnabled(false);
        forwardButton = frame.createToolbarButton("forward");
        forwardButton.setEnabled(false);
    }

    /**
     * Called whenever the image history has changed. This includes
     * simply moving forward and backward through the history.
     * Generally anything that would affect the back/forward buttons.
     *
     * @param  e  set update event.
     */
    public void historyChanged(SetEvent e) {
        Set set = (Set) e.getSource();
        backButton.setEnabled(!set.historyAtStart());
        forwardButton.setEnabled(!set.historyAtEnd());
    }

    /**
     * Called whenever the renderer has changed.
     *
     * @param  e  set update event.
     */
    public void rendererChanged(SetEvent e) {
    }
}
