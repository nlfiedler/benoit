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
 * $Id: Renderer.java 2001 2005-09-12 02:08:06Z nfiedler $
 *
 ********************************************************************/

package com.bluemarsh.benoit.render;

import com.bluemarsh.benoit.model.Parameters;
import java.awt.Image;

/**
 * Defines the interface for all renderers.
 *
 * @author  Nathan Fiedler
 */
public interface Renderer {

    /**
     * Adds the given listener to the renderer's list of listeners.
     *
     * @param  l  listener to add.
     */
    void addListener(RenderListener l);

    /**
     * Removes the given listener from the renderer's list of listeners.
     *
     * @param  l  listener to remove.
     */
    void removeListener(RenderListener l);

    /**
     * Render the given region of the set.
     *
     * @param  image   image to render to.
     * @param  params  boundaries of region to draw.
     */
    void render(Image image, Parameters params);
}
