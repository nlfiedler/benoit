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
 * $Id: SetFrameMapper.java 2003 2005-09-12 04:51:14Z nfiedler $
 *
 ********************************************************************/

package com.bluemarsh.benoit.ui;

import com.bluemarsh.benoit.Set;
import java.awt.Component;
import java.awt.Frame;
import java.util.EventObject;
import java.util.Hashtable;
import javax.swing.JPopupMenu;

/**
 * Class SetFrameMapper is responsible for providing a mapping
 * between Frame objects and Set objects. This is used by the
 * BenoitAction classes to convert a Frame reference to a Set
 * instance.
 *
 * @author  Nathan Fiedler
 */
public class SetFrameMapper {
    /** Mapping of Frames to Sets. */
    protected static Hashtable framesToSets = new Hashtable();

    /**
     * Adds the given Frame to Set mapping.
     *
     * @param  f  top-level frame that is connected to Set.
     * @param  s  Set connect to top-level frame.
     */
    public static void addFrameSetMapping(Frame f, Set s) {
        framesToSets.put(f, s);
    }

    /**
     * Find the hosting frame for this object. Often used when
     * displaying dialogs which require a host frame.
     *
     * @param  o  Object with which to find the parent frame.
     *            Could be a subclass of EventObject or Component.
     * @return  hosting frame or null if none.
     */
    public static Frame getOwningFrame(Object o) {
        if (o instanceof EventObject) {
            o = ((EventObject) o).getSource();
        }
        if (!(o instanceof Component)) {
            throw new IllegalArgumentException(
                "o did not yield a Component");
        }

        // Find the top frame parent of the component.
        do {
            if (o instanceof JPopupMenu) {
                // Special case for popup menus which do not
                // have parents but have invokers instead.
                o = ((JPopupMenu) o).getInvoker();
            }
            if (o instanceof Frame) {
                return (Frame) o;
            }
            o = ((Component) o).getParent();
        } while (o != null);

        // If we got here, the child simply has no parent frame.
        throw new IllegalArgumentException("o is not a child of any Frame");
    }

    /**
     * Finds the Set that is associated with the window that
     * contains the component that is the source of the given action
     * event.
     *
     * @param  e  event with which to find Set.
     * @return  Set instance, or null if error.
     */
    public static Set getSetForEvent(EventObject e) {
        // Use the SetFrameMapper to get the Set for the Frame.
        return getSetForFrame(getOwningFrame(e));
    }

    /**
     * Looks for a Set that is associated with the given Frame.
     *
     * @param  f  Frame to look up Set with.
     * @return  Set for the given frame, if any.
     */
    public static Set getSetForFrame(Frame f) {
        return (Set) framesToSets.get(f);
    }

    /**
     * Removes the given Frame to Set mapping.
     *
     * @param  f  top-level frame to be removed from the mapping.
     */
    public static void removeFrameSetMapping(Frame f) {
        framesToSets.remove(f);
    }
}
