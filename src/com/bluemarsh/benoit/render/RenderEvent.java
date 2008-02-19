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
 * $Id: RenderEvent.java 2001 2005-09-12 02:08:06Z nfiedler $
 *
 ********************************************************************/

package com.bluemarsh.benoit.render;

import java.util.EventObject;

/**
 * Event object sent out when a Renderer has updated a portion of the set.
 * This event encapsulates information about the current status of the
 * set, such as how much of the set is rendered.
 *
 * @author  Nathan Fiedler
 */
public class RenderEvent extends EventObject {
    /** silence the compiler warnings */
    private static final long serialVersionUID = 1L;
    /** The percentage of the set that is completely rendered. */
    private int percentDone;

    /**
     * Creates a new render event object that is associated with
     * the given source object.
     *
     * @param  source  source object for this event
     */
    public RenderEvent(Object source) {
        super(source);
    }

    /**
     * Creates a new render event object that is associated with
     * the given source object.
     *
     * @param  source    source object for this event
     * @param  percent   percentage of set rendered
     */
    public RenderEvent(Object source, int percent) {
        this(source);
        percentDone = percent;
    }

    /**
     * Returns the percentage of the set that has been rendered so far.
     *
     * @return  a number between 0 and 100, inclusive.
     */
    public int getPercentDone() {
        return percentDone;
    }
}
