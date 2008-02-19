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
 * $Id: StopAction.java 2003 2005-09-12 04:51:14Z nfiedler $
 *
 ********************************************************************/

package com.bluemarsh.benoit.action;

import java.awt.event.ActionEvent;

/**
 * Implements the stop program action.
 *
 * @author  Nathan Fiedler
 */
public class StopAction extends BenoitAction {
    /** silence the compiler warnings */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new StopAction object with the default action
     * command string of "stop".
     */
    public StopAction() {
        super("stop");
    }

    /**
     * Performs the stop action.
     *
     * @param  event  action event
     */
    public void actionPerformed(ActionEvent event) {
        getSet(event).renderStop();
    }
}
