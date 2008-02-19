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
 * $Id: SetListener.java 2001 2005-09-12 02:08:06Z nfiedler $
 *
 ********************************************************************/

package com.bluemarsh.benoit;

import java.util.EventListener;

/**
 * Defines an interface for listeners of the Set to implement
 * in order to be kept informed of changes to the Set.
 *
 * @author  Nathan Fiedler
 */
public interface SetListener extends EventListener {

    /**
     * Called whenever the image history has changed. This includes
     * simply moving forward and backward through the history.
     * Generally anything that would affect the back/forward buttons.
     *
     * @param  e  set update event.
     */
    void historyChanged(SetEvent e);

    /**
     * Called whenever the renderer has changed.
     *
     * @param  e  set update event.
     */
    void rendererChanged(SetEvent e);
}
