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
 * $Id: ActionTable.java 2003 2005-09-12 04:51:14Z nfiedler $
 *
 ********************************************************************/

package com.bluemarsh.benoit.action;

import java.util.Hashtable;
import java.util.logging.Logger;
import javax.swing.Action;

/**
 * This class holds the application actions and provides access to them.
 *
 * @author  Nathan Fiedler
 */
public class ActionTable {
    /** Suffix added to command string to retrieve action classes. */
    protected static final String ACTION_SUFFIX = "Action";
    /** Table of available actions in our program. Other classes
     * can look up actions by their action string using the
     * <code>getAction()</code> method. */
    protected static Hashtable actionTable = new Hashtable();
    /** Logger. */
    protected static Logger logger;

    static {
        // Initialize the logger.
        logger = Logger.getLogger("com.bluemarsh.benoit.action");
    }

    /**
     * Retrieves the action corresponding to the given command string.
     * This will first look in the list of actions registered with
     * ActionTable. If the action is not there, it will try to
     * instantiate the action. If successful, it adds the action
     * to the list of registered actions and returns a reference
     * to the action. Else it does nothing and returns null.
     *
     * @param  cmd  command string to find action for.
     * @return  action matching command string or null if not found.
     */
    public static Action getAction(String cmd) {
        Action a = (Action) actionTable.get(cmd);
        if (a == null) {
            // All the actions are specified in the ui package's Bundle.
            String actionName = com.bluemarsh.benoit.ui.Bundle.getString(
                cmd + ACTION_SUFFIX);
            if (actionName != null) {
                try {
                    Class actionClass = Class.forName(
                        "com.bluemarsh.benoit.action." + actionName);
                    a = (Action) actionClass.newInstance();
                    actionTable.put(a.getValue(Action.NAME), a);
                } catch (Exception e) {
                    logger.severe("ERROR: Couldn't instantiate action: " +
                                  actionName + ": " + e);
                }
            }
        }
        return a;
    }
}
