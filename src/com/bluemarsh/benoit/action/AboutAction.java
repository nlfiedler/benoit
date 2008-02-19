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
 * $Id: AboutAction.java 2003 2005-09-12 04:51:14Z nfiedler $
 *
 ********************************************************************/

package com.bluemarsh.benoit.action;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * Implements the about action used to show the credits for Benoit.
 *
 * @author  Nathan Fiedler
 */
public class AboutAction extends BenoitAction {
    /** silence the compiler warnings */
    private static final long serialVersionUID = 1L;
    /** The resource bundle contained in this object. */
    private static ResourceBundle resourceBundle;

    static {
        resourceBundle = ResourceBundle.getBundle(
            "com.bluemarsh.benoit.action.version");
    }

    /**
     * Creates a new AboutAction object with the default action
     * command string of "about".
     */
    public AboutAction() {
        super("about");
    }

    /**
     * Performs the about action. This simply displays a dialog
     * showing the credits for the program.
     *
     * @param  event  action event
     */
    public void actionPerformed(ActionEvent event) {
        Frame frame = getOwningFrame(event);
        String version = getVersion();
        Object[] messages = {
            new JLabel("- Benoit " + version + " -", JLabel.CENTER),
            new JLabel(Bundle.getString("about1"), JLabel.CENTER),
            new JLabel(Bundle.getString("about2"), JLabel.CENTER)
        };
        JOptionPane.showMessageDialog(
            frame, messages, Bundle.getString("About.title"),
            JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Retrieves the version number for the application.
     *
     * @return  application version number string.
     */
    public static String getVersion() {
        try {
            return resourceBundle.getString("version");
        } catch (MissingResourceException mre) {
            return "x.y";
        }
    }
}
