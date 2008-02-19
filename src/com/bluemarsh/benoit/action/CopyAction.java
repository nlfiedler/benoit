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
 * $Id: CopyAction.java 2003 2005-09-12 04:51:14Z nfiedler $
 *
 ********************************************************************/

package com.bluemarsh.benoit.action;

import com.bluemarsh.benoit.Set;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * Implements the copy action used to show the credits for Benoit.
 *
 * @author  Nathan Fiedler
 */
public class CopyAction extends BenoitAction {
    /** silence the compiler warnings */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new CopyAction object with the default action
     * command string of "copy".
     */
    public CopyAction() {
        super("copy");
    }

    /**
     * Performs the copy action. This simply displays a dialog
     * showing the credits for the program.
     *
     * @param  event  action event
     */
    public void actionPerformed(ActionEvent event) {
        Clipboard cp = Toolkit.getDefaultToolkit().getSystemClipboard();
        Set set = getSet(event);
        ImageTransferable it = new ImageTransferable(set.getImage());
        cp.setContents(it, it);
    }

    /**
     * A Transferable for Java images.
     */
    protected class ImageTransferable implements ClipboardOwner, Transferable {
        /** Transferable image. */
        protected Image image;
        /** Flavors we support. */
        protected DataFlavor[] flavors;

        /**
         * Constructs a ImageTransferable for the given image.
         */
        public ImageTransferable(Image image) {
            this.image = image;
            flavors = new DataFlavor[1];
            flavors[0] = DataFlavor.imageFlavor;
        }

        /**
         * Returns an array of DataFlavor objects indicating the flavors
         * the data can be provided in. The array should be ordered
         * according to preference for providing the data (from most
         * richly descriptive to least descriptive).
         *
         * @return  an array of data flavors in which this data can be
         *          transferred
         */
        public DataFlavor[] getTransferDataFlavors() {
            return flavors;
        }

        /**
         * Returns whether or not the specified data flavor is supported
         * for this object.
         *
         * @param  flavor  the requested flavor for the data.
         * @return  boolean indicating whether or not the data flavor is
         *          supported
         */
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor.equals(flavors[0]);
        }

        /**
         * Returns an object which represents the data to be
         * transferred. The class of the object returned is defined by
         * the representation class of the flavor.
         *
         * @param  flavor  the requested flavor for the data
         * @see  DataFlavor#getRepresentationClass
         * @throws  IOException
         *          if the data is no longer available in the requested flavor.
         * @throws  UnsupportedFlavorException
         *          if the requested data flavor is not supported.
         */
        public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException,
                   IOException {

            if (flavor.equals(flavors[0])) {
                return image;
            } else {
                throw new UnsupportedFlavorException(flavor);
            }
        }

        /**
         * Notifies this object that it is no longer the owner of the
         * contents of the clipboard.
         *
         * @param  clipboard  the clipboard that is no longer owned.
         * @param  contents   the contents which this owner had placed
         *                    on the clipboard.
         */
        public void lostOwnership(Clipboard clipboard, Transferable contents) {
        }
    }
}
