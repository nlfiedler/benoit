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
 * $Id: BenoitAction.java 2003 2005-09-12 04:51:14Z nfiedler $
 *
 ********************************************************************/

package com.bluemarsh.benoit.action;

import com.bluemarsh.benoit.Set;
import com.bluemarsh.benoit.ui.SetFrameMapper;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.StringTokenizer;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Base action class which all other actions subclass. Provides some
 * utility functions needed by nearly all of the actions in Benoit.
 *
 * @author  Nathan Fiedler
 */
public abstract class BenoitAction extends AbstractAction {

    /**
     * Creates a new BenoitAction command with the given
     * action command string.
     *
     * @param  name  action command string
     */
    public BenoitAction(String name) {
        super(name);
    } // BenoitAction

    /**
     * Display an error message in a dialog.
     *
     * @param  o    Object with which to find the parent frame.
     *              Could be a subclass of EventObject or Component.
     * @param  msg  error message to be displayed.
     */
    public static void displayError(Object o, String msg) {
        Frame frame = SetFrameMapper.getOwningFrame(o);
        Object message = msg;
        if (msg.indexOf('\n') >= 0 || msg.length() > 80) {
            // Multi-line messages must be split.
            String[] arr = splitOnNewline(msg);
            boolean longLines = false;
            for (int ii = 0; ii < arr.length; ii++) {
                if (arr[ii].length() > 80) {
                    longLines = true;
                    break;
                }
            }

            if (longLines) {
                // Show the message in a scrollable area.
                JTextArea textArea = new JTextArea(msg);
                textArea.setEditable(false);
                JScrollPane scroller = new JScrollPane(textArea);
                scroller.setPreferredSize(new Dimension(400, 200));
                message = scroller;
            } else {
                message = arr;
            }
        }
        JOptionPane.showMessageDialog(
            frame, message, Bundle.getString("Error.title"),
            JOptionPane.ERROR_MESSAGE);
    } // displayError

    /**
     * Find the hosting frame for this object. Often used when
     * displaying dialogs which require a host frame.
     *
     * @param  o  Object with which to find the parent frame.
     *            Could be a subclass of EventObject or Component.
     * @return  hosting frame or null if none.
     */
    public static Frame getOwningFrame(Object o) {
        return SetFrameMapper.getOwningFrame(o);
    } // getOwningFrame

    /**
     * Find the Set associated with the hosting frame for this object.
     *
     * @param  o  Object with which to find the parent frame.
     *            Could be a subclass of EventObject or Component.
     * @return  instance of Set.
     */
    public static Set getSet(Object o) {
        Frame fr = SetFrameMapper.getOwningFrame(o);
        return SetFrameMapper.getSetForFrame(fr);
    } // getSet

    /**
     * Splits the given string on the newline character (\n).
     *
     * @param  str  string to split.
     * @return  array of strings.
     */
    protected static String[] splitOnNewline(String str) {
        StringTokenizer st = new StringTokenizer(str, "\n");
        int size = st.countTokens();
        String[] arr = new String[size];
        for (int ii = 0; ii < size; ii++) {
            arr[ii] = st.nextToken();
        }
        return arr;
    } // splitOnNewline
} // BenoitAction
