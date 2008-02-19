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
 * $Id: DefaultSet.java 2001 2005-09-12 02:08:06Z nfiedler $
 *
 ********************************************************************/

package com.bluemarsh.benoit;

import com.bluemarsh.benoit.model.BenoitNumber;
import com.bluemarsh.benoit.model.Parameters;
import com.bluemarsh.benoit.render.BigDecimalRenderer;
import com.bluemarsh.benoit.render.FastDoublesRenderer;
import com.bluemarsh.benoit.render.Renderer;
import com.bluemarsh.benoit.render.RenderEvent;
import com.bluemarsh.benoit.render.RenderListener;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Scrollable;

/**
 * The default implementation of a Set.
 *
 * @author  Nathan Fiedler
 */
public class DefaultSet extends AbstractSet implements RenderListener {
    /** Widgets representing the region boundaries. */
    private Settings settings;
    /** Wrapper object for our set. */
    private JComponent wrapper;
    /** The component that shows the image. */
    private ImageComponent imageComponent;
    /** Image to which the renderer draws. */
    private Image image;
    /** Set renderer. */
    private Renderer renderer;
    /** Thread on which to perform the rendering. */
    private Thread renderThread;
    /** One of the BenoitNumber constants. */
    private int numericType;
    /** Cursor shown over rendered image. */
    private Cursor crossHairCursor;
    /** Cursor shown while rendering image. */
    private Cursor waitCursor;

    /**
     * Constructs a DefaultSet.
     */
    public DefaultSet() {
        image = newImage();
        imageComponent = new ImageComponent(image);
        crossHairCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
        waitCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
        imageComponent.setCursor(crossHairCursor);
        JScrollPane scroller = new JScrollPane(imageComponent);

        settings = new Settings();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        wrapper = new JPanel(gbl);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gbl.setConstraints(settings, gbc);
        wrapper.add(settings);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbl.setConstraints(scroller, gbc);
        wrapper.add(scroller);

        BenoitNumber minX = new BenoitNumber(-2.0);
        BenoitNumber maxX = new BenoitNumber(1.25);
        BenoitNumber minY = new BenoitNumber(-1.25);
        BenoitNumber maxY = new BenoitNumber(1.25);
        Parameters params = new Parameters(minX, maxX, minY, maxY);
        numericType = BenoitNumber.DOUBLE_TYPE;
        settings.setParameters(params);
    } // DefaultSet

    /**
     * Closes the Set in preparation for non-use.
     */
    public void close() {
        if (renderer != null) {
            renderer.removeListener(this);
            renderStop();
        }
    } // close

    /**
     * Retrieve the last rendered image (does not have to be completely
     * rendered yet).
     *
     * @return  image of current region.
     */
    public Image getImage() {
        return image;
    } // getImage

    /**
     * Return the JComponent wrapper to the rendered image. This
     * reference must remain valid indefinitely as it is used to
     * listen for mouse motion events over this component.
     *
     * @return  our image component.
     */
    public JComponent getImageComponent() {
        return imageComponent;
    } // getImageComponent

    /**
     * Gets the parameters used for rendering the current region
     * of the set.
     *
     * @return  current region boundaries; null if no parameters.
     */
    public Parameters getParameters() {
        return settings.getParameters();
    } // getParameters

    /**
     * Returns a reference to this Set's renderer.
     *
     * @return  renderer for this Set, may be null.
     */
    public Renderer getRenderer() {
        return renderer;
    } // getRenderer

    /**
     * Return the JComponent wrapper to this set.
     *
     * @return  our interface component.
     */
    public JComponent getUI() {
        return wrapper;
    } // getUI

    /**
     * Called whenever the rendered image has been updated.
     *
     * @param  e  render update event.
     */
    public void imageUpdated(RenderEvent e) {
        imageComponent.repaint();
        if (e.getPercentDone() == 100) {
            imageComponent.setCursor(crossHairCursor);
            cacheImage(image);
        }
    } // imageUpdated

    /**
     * Construct a new image to render to.
     *
     * @return  a new image.
     */
    protected Image newImage() {
        // Use 1.3 aspect ratio by default.
        return new BufferedImage(640, 492, BufferedImage.TYPE_INT_RGB);
    } // newImage

    /**
     * Render the region of the set given by the parameters.
     * This does not manage the parameters history list.
     *
     * @param  params  region of the set to render.
     */
    public void renderLow(Parameters params) {
        if (renderer == null) {
            throw new IllegalStateException("renderer not set");
        }
        image = newImage();
        showImage(image);
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        params.adjustAspect(width, height);
        setParameters(params);
        imageComponent.setCursor(waitCursor);
        renderThread = new Thread(new RenderRunner(image, params, renderer));
        renderThread.start();
    } // renderLow

    /**
     * Cause the current rendering to stop. Has no affect if rendering
     * has already completed.
     */
    public void renderStop() {
        if (renderThread != null && renderThread.isAlive()) {
            renderThread.interrupt();
            try {
                // Wait for the render thread to stop.
                renderThread.join();
            } catch (InterruptedException ie) {
                // fall through
            }
            renderThread = null;
            imageComponent.setCursor(crossHairCursor);
        }
    } // renderStop

    /**
     * Sets the numeric precision used by this Set.
     *
     * @param  type  numeric type (one of the BenoitNumber constants).
     */
    public void setNumberType(int type) {
        if (renderer != null) {
            renderer.removeListener(this);
        }
        if (type == BenoitNumber.DOUBLE_TYPE) {
            renderer = new FastDoublesRenderer();
        } else if (type == BenoitNumber.BIG_TYPE) {
            renderer = new BigDecimalRenderer();
        } else {
            throw new IllegalArgumentException("invalid number type");
        }
        numericType = type;
        getParameters().setType(type);
        renderer.addListener(this);
        fireChange(RENDERER_CHANGED);
    } // setNumberType

    /**
     * Sets the parameters used for rendering the set. This does not
     * cause an update of the view.
     *
     * @param  params  new region boundaries.
     */
    public void setParameters(Parameters params) {
        settings.setParameters(params);
    } // setParameters

    /**
     * Show the given image in preference to the one presently shown.
     *
     * @param  image  image to take place of current image.
     */
    protected void showImage(Image image) {
        imageComponent.setImage(image);
    } // showImage

    /**
     * Show an indication that the given region is selected in the
     * rendered image. The coordinates are with respect to the image.
     *
     * @param  left    left-most coordinate of selection.
     * @param  top     top-most coordinate of selection.
     * @param  right   right-most coordinate of selection.
     * @param  bottom  bottom-most coordinate of selection.
     */
    public void showSelection(int left, int top, int right, int bottom) {
        imageComponent.setSelection(left, top, right, bottom);
    } // showSelection

    /**
     * Implements a class for displaying and manipulating the parameters
     * of the mandelbrot set. It also acts as a mouse motion listener on
     * the mandelbrot set views and displays the mouse position.
     *
     * @author  Nathan Fiedler
     */
    public class Settings extends JPanel {
        /** silence the compiler warnings */
        private static final long serialVersionUID = 1L;
        /** Field for minimum X. */
        private JTextField minXField;
        /** Field for minimum Y. */
        private JTextField minYField;
        /** Field for maximum X. */
        private JTextField maxXField;
        /** Field for maximum Y. */
        private JTextField maxYField;

        /**
         * Constructs a Settings.
         */
        public Settings() {
            super(new GridBagLayout());
            GridBagLayout gbl = (GridBagLayout) getLayout();
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(1, 1, 1, 1);

            // add vertical space between fields and buttons
            Dimension vstrut10 = new Dimension(0, 10);
            Component strut = Box.createRigidArea(vstrut10);
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbl.setConstraints(strut, gbc);
            add(strut);

            // create the "minimum x" input field
            JLabel label = new JLabel("min X:");
            gbc.anchor = GridBagConstraints.EAST;
            gbc.gridwidth = 1;
            gbl.setConstraints(label, gbc);
            add(label);
            minXField = new JTextField();
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            gbl.setConstraints(minXField, gbc);
            add(minXField);

            // add space between these two fields
            Dimension hstrut25 = new Dimension(25, 0);
            strut = Box.createRigidArea(hstrut25);
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.NONE;
            gbc.weightx = 0.0;
            gbl.setConstraints(strut, gbc);
            add(strut);

            // create the "maximum x" input field
            label = new JLabel("max X:");
            gbc.anchor = GridBagConstraints.EAST;
            gbl.setConstraints(label, gbc);
            add(label);
            maxXField = new JTextField();
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.weightx = 1.0;
            gbl.setConstraints(maxXField, gbc);
            add(maxXField);

            gbc.gridwidth = 1;

            // create the "minimum y" input field
            label = new JLabel("min Y:");
            gbc.anchor = GridBagConstraints.EAST;
            gbc.fill = GridBagConstraints.NONE;
            gbc.weightx = 0.0;
            gbl.setConstraints(label, gbc);
            add(label);
            minYField = new JTextField();
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            gbl.setConstraints(minYField, gbc);
            add(minYField);

            // add space between these two fields
            strut = Box.createRigidArea(hstrut25);
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.NONE;
            gbc.weightx = 0.0;
            gbl.setConstraints(strut, gbc);
            add(strut);

            // create the "maximum y" input field
            label = new JLabel("max Y:");
            gbc.anchor = GridBagConstraints.EAST;
            gbl.setConstraints(label, gbc);
            add(label);
            maxYField = new JTextField();
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.weightx = 1.0;
            gbl.setConstraints(maxYField, gbc);
            add(maxYField);
        } // Settings

        /**
         * Retrieve the parameter settings from the widgets.
         *
         * @return  set of mandelbrot parameters.
         */
        public Parameters getParameters() {
            BenoitNumber minX = BenoitNumber.valueOf(
                minXField.getText(), numericType);
            BenoitNumber maxX = BenoitNumber.valueOf(
                maxXField.getText(), numericType);
            BenoitNumber minY = BenoitNumber.valueOf(
                minYField.getText(), numericType);
            BenoitNumber maxY = BenoitNumber.valueOf(
                maxYField.getText(), numericType);
            return new Parameters(minX, maxX, minY, maxY);
        } // getParameters

        /**
         * Update the settings display to show the given parameter values.
         *
         * @param  params  set of mandelbrot parameters
         */
        public void setParameters(Parameters params) {
            minXField.setText(params.getMinX().toString());
            minYField.setText(params.getMinY().toString());
            maxXField.setText(params.getMaxX().toString());
            maxYField.setText(params.getMaxY().toString());
        } // setParameters
    } // Settings

    /**
     * Renders the image by invoking the Renderer.
     */
    protected class RenderRunner implements Runnable {
        /** Image to render to. */
        private Image image;
        /** Boundaries of region. */
        private Parameters params;
        /** Renderer to do the rendering. */
        private Renderer renderer;

        /**
         * Constructs a RenderRunner.
         *
         * @param  image     image to render to.
         * @param  params    boundaries of region.
         * @param  renderer  object to do the rendering.
         */
        public RenderRunner(Image image, Parameters params,
                            Renderer renderer) {
            this.image = image;
            this.params = params;
            this.renderer = renderer;
        } // RenderRunner

        /**
         * Invoke the renderer.
         */
        public void run() {
            renderer.render(image, params);
        } // run
    } // RenderRunner

    /**
     * The JComponent that holds the rendered image.
     */
    protected class ImageComponent extends JComponent
        implements ImageObserver, Scrollable {
        /** silence the compiler warnings */
        private static final long serialVersionUID = 1L;
        /** Image to be rendered. */
        private Image image;
        /** Left of selection, if any. */
        private int selectionLeft;
        /** Right of selection, if any. */
        private int selectionRight;
        /** Top of selection, if any. */
        private int selectionTop;
        /** Bottom of selection, if any. */
        private int selectionBottom;

        /**
         * Constructs an ImageComponent with the given image.
         *
         * @param  image  image for this component.
         */
        public ImageComponent(Image image) {
            setImage(image);
        } // ImageComponent

        /**
         * Returns the current height of this component.
         *
         * @return  height of the image.
         */
        public int getHeight() {
            return image.getHeight(this);
        } // getHeight

        /**
         * Returns maximum size.
         *
         * @return  maximum size.
         */
        public Dimension getMaximumSize() {
            return getPreferredSize();
        } // getMaximumSize

        /**
         * Returns minimum size.
         *
         * @return  minimum size.
         */
        public Dimension getMinimumSize() {
            return new Dimension(100, 100);
        } // getMinimumSize

        /**
         * Returns preferred size.
         *
         * @return  preferred size.
         */
        public Dimension getPreferredSize() {
            return new Dimension(getWidth(), getHeight());
        } // getPreferredSize

        /**
         * Returns the preferred size of the viewport for a view
         * component. For example the preferredSize of a JList component
         * is the size required to accommodate all of the cells in its
         * list however the value of preferredScrollableViewportSize is
         * the size required for JList.getVisibleRowCount() rows. A
         * component without any properties that would effect the
         * viewport size should just return getPreferredSize() here.
         *
         * @return  The preferredSize of a JViewport whose view is this
         *          Scrollable.
         * @see  JViewport#getPreferredSize
         */
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        } // getPreferredScrollableViewportSize

        /**
         * Components that display logical rows or columns should compute
         * the scroll increment that will completely expose one new row
         * or column, depending on the value of orientation.  Ideally,
         * components should handle a partially exposed row or column by
         * returning the distance required to completely expose the item.
         *
         * <p>Scrolling containers, like JScrollPane, will use this method
         * each time the user requests a unit scroll.</p>
         *
         * @param  visibleRect  The view area visible within the viewport
         * @param  orientation  Either SwingConstants.VERTICAL or
         *                      SwingConstants.HORIZONTAL.
         * @param  direction    Less than zero to scroll up/left, greater
         *                      than zero for down/right.
         * @return  The "unit" increment for scrolling in the specified
         *          direction. This value should always be positive.
         * @see JScrollBar#setUnitIncrement
         */
        public int getScrollableUnitIncrement(Rectangle visibleRect,
                                              int orientation, int direction) {
            return 20;
        } // getScrollableUnitIncrement

        /**
         * Components that display logical rows or columns should compute
         * the scroll increment that will completely expose one block
         * of rows or columns, depending on the value of orientation.
         *
         * <p>Scrolling containers, like JScrollPane, will use this method
         * each time the user requests a block scroll.</p>
         *
         * @param  visibleRect  The view area visible within the viewport
         * @param  orientation  Either SwingConstants.VERTICAL or
         *                      SwingConstants.HORIZONTAL.
         * @param  direction    Less than zero to scroll up/left, greater
         *                      than zero for down/right.
         * @return  The "block" increment for scrolling in the specified
         *          direction. This value should always be positive.
         * @see JScrollBar#setBlockIncrement
         */
        public int getScrollableBlockIncrement(Rectangle visibleRect,
                                               int orientation,
                                               int direction) {
            return 100;
        } // getScrollableBlockIncrement

        /**
         * Return true if a viewport should always force the width of
         * this <code>Scrollable</code> to match the width of the
         * viewport. For example a normal text view that supported line
         * wrapping would return true here, since it would be
         * undesirable for wrapped lines to disappear beyond the right
         * edge of the viewport. Note that returning true for a
         * Scrollable whose ancestor is a JScrollPane effectively
         * disables horizontal scrolling.
         *
         * <p>Scrolling containers, like JViewport, will use this method
         * each time they are validated.</p>
         *
         * @return  True if a viewport should force the Scrollables width
         *          to match its own.
         */
        public boolean getScrollableTracksViewportWidth() {
            return false;
        } // getScrollableTracksViewportWidth

        /**
         * Return true if a viewport should always force the height of this
         * Scrollable to match the height of the viewport.  For example a
         * columnar text view that flowed text in left to right columns
         * could effectively disable vertical scrolling by returning
         * true here.
         *
         * <p>Scrolling containers, like JViewport, will use this method each
         * time they are validated.</p>
         *
         * @return  True if a viewport should force the Scrollables height
         *          to match its own.
         */
        public boolean getScrollableTracksViewportHeight() {
            return false;
        } // getScrollableTracksViewportHeight

        /**
         * Returns the current width of this component.
         *
         * @return  width of the image.
         */
        public int getWidth() {
            return image.getWidth(this);
        } // getWidth

        /**
         * This method is called when information about an image which was
         * previously requested using an asynchronous interface becomes
         * available.
         *
         * @param  img        the image being observed.
         * @param  infoflags  the bitwise inclusive OR of the following
         *                    flags: <code>WIDTH</code>, <code>HEIGHT</code>,
         *                    <code>PROPERTIES</code>, <code>SOMEBITS</code>,
         *                    <code>FRAMEBITS</code>, <code>ALLBITS</code>,
         *                    <code>ERROR</code>, <code>ABORT</code>.
         * @param  x          the <i>x</i> coordinate.
         * @param  y          the <i>y</i> coordinate.
         * @param  width      the width.
         * @param  height     the height.
         * @return  <code>false</code> if the infoflags indicate that the
         *          image is completely loaded; <code>true</code> otherwise.
         */
        public boolean imageUpdate(Image img, int infoflags, int x,
                                   int y, int width, int height) {
            return false;
        } // imageUpdate

        /**
         * Paints this component.
         *
         * @param  g  graphics context.
         */
        public void paint(Graphics g) {
            super.paint(g);
            g.drawImage(image, 0, 0, this);
            // Draw a bounding rectangle to show what is selected.
            if (selectionRight > 0) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.white);
                float dash[] = { 2.0f };
                BasicStroke stroke = new BasicStroke(
                    1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                    10.0f, dash, 0.0f);
                g2.setStroke(stroke);
                int x = selectionLeft;
                int y = selectionTop;
                int w = selectionRight - selectionLeft;
                int h = selectionBottom - selectionTop;
                g2.drawRect(x, y, w, h);
            }
        } // paint

        /**
         * Sets the image this component paints.
         *
         * @param  image  new image to paint.
         */
        public void setImage(Image image) {
            this.image = image;
            repaint();
        } // setImage

        /**
         * Show an indication that the given region is selected.
         *
         * @param  left    left-most coordinate of selection.
         * @param  top     top-most coordinate of selection.
         * @param  right   right-most coordinate of selection.
         * @param  bottom  bottom-most coordinate of selection.
         */
        public void setSelection(int left, int top, int right, int bottom) {
            selectionLeft = left;
            selectionRight = right;
            selectionTop = top;
            selectionBottom = bottom;
            repaint();
        } // setSelection
    } // ImageComponent
} // DefaultSet
