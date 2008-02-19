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
 * $Id: SetScaleAction.java 2003 2005-09-12 04:51:14Z nfiedler $
 *
 ********************************************************************/

package com.bluemarsh.benoit.action;

import com.bluemarsh.benoit.Set;
import com.bluemarsh.benoit.model.Parameters;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Implements the setScale program action.
 *
 * @author  Nathan Fiedler
 */
public class SetScaleAction extends BenoitAction {
    /** silence the compiler warnings */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new SetScaleAction object with the default action
     * command string of "setScale".
     */
    public SetScaleAction() {
        super("setScale");
    }

    /**
     * Performs the setScale action.
     *
     * @param  event  action event
     */
    public void actionPerformed(ActionEvent event) {
        Frame frame = getOwningFrame(event);
        Set set = getSet(event);
        Parameters params = set.getParameters();
        int scale = params.getScale();

        Object messages[] = {
            Bundle.getString("SetScale.desc"),
            Bundle.getString("SetScale.scale"),
            new JTextField(String.valueOf(scale), 30)
        };

        boolean responseOkay = false;
        while (!responseOkay) {
            // Show dialog to get user input.
            int response = JOptionPane.showOptionDialog(
                frame, messages,
                Bundle.getString("SetScale.title"),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, null, null);
            if (response != JOptionPane.OK_OPTION) {
                // user cancelled
                return;
            }

            String newScale = ((JTextField) messages[2]).getText();
            if (newScale == null || newScale.length() == 0) {
                displayError(event, Bundle.getString("SetScale.missingScale"));
            } else {
                try {
                    scale = Integer.parseInt(newScale);
                    responseOkay = true;
                } catch (NumberFormatException nfe) {
                    displayError(event, Bundle.getString(
                                     "SetScale.invalidNumber"));
                }
            }
        }

        params.setScale(scale);
    }
}
