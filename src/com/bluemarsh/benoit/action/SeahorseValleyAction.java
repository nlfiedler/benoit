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
 * $Id: SeahorseValleyAction.java 2003 2005-09-12 04:51:14Z nfiedler $
 *
 ********************************************************************/

package com.bluemarsh.benoit.action;

import com.bluemarsh.benoit.Set;
import com.bluemarsh.benoit.model.BenoitNumber;
import com.bluemarsh.benoit.model.Parameters;
import java.awt.event.ActionEvent;

/**
 * Implements the SeahorseValley program action.
 *
 * @author  Nathan Fiedler
 */
public class SeahorseValleyAction extends BenoitAction {
    /** silence the compiler warnings */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new SeahorseValleyAction object with the default action
     * command string of "seahorseValley".
     */
    public SeahorseValleyAction() {
        super("seahorseValley");
    }

    /**
     * Performs the SeahorseValley action.
     *
     * @param  event  action event
     */
    public void actionPerformed(ActionEvent event) {
        Set set = getSet(event);
        BenoitNumber minX = new BenoitNumber(-0.8058342783);
        BenoitNumber maxX = new BenoitNumber(-0.7809802721);
        BenoitNumber minY = new BenoitNumber(-0.1686330887);
        BenoitNumber maxY = new BenoitNumber(-0.1495265714);
        Parameters params = new Parameters(minX, maxX, minY, maxY);
        set.setParameters(params);
        set.render();
    }
}
