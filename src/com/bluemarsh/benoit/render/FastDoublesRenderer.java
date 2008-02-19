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
 * $Id: FastDoublesRenderer.java 2001 2005-09-12 02:08:06Z nfiedler $
 *
 ********************************************************************/

package com.bluemarsh.benoit.render;

import com.bluemarsh.benoit.model.Parameters;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

/**
 * The fast-doubles concrete implementation of a renderer.
 *
 * @author  Nathan Fiedler
 */
public class FastDoublesRenderer extends AbstractRenderer {

    /**
     * Renders the Mandelbrot set to the image using the current
     * parameters. Changing the parameters during this rendering will
     * not affect it. This method uses a very simple approach, drawing
     * each vertical column of pixels from left to right. The points are
     * calculated using the escape-iterations algorithm.
     *
     * @param  image   image to render to.
     * @param  params  boundaries of region to draw.
     */
    public void render(Image image, Parameters params) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        if (width == -1 || height == -1) {
            // Image is not ready yet.
            throw new IllegalArgumentException("image not loaded");
        }
        Graphics g = image.getGraphics();

        double minX = params.getMinX().doubleValue();
        double maxX = params.getMaxX().doubleValue();
        double minY = params.getMinY().doubleValue();
        double maxY = params.getMaxY().doubleValue();
        double dx = (maxX - minX) / width;
        double dy = (maxY - minY) / height;
        int dwellLimit = 100;
        // avoid using sqrt() repeatedly by squaring the escape radius.
        double escapeRadius = 4.0; // 2.0 ^ 2
        boolean interrupted = false;
        for (int x = 0; x < width; x++) {
            double cr = dx * x + minX;
            for (int y = 0; y < height; y++) {
                double ci = dy * y + minY;
                double zr = cr;
                double zi = ci;
                double m;
                int iter = 1;

                do {
                    // z = z * z + c
                    double r = zr * zr - zi * zi;
                    zi = 2.0 * zr * zi + ci;
                    zr = r + cr;
                    // magnitude (would use sqrt() normally)
                    m = zr * zr + zi * zi;
                    iter++;
                } while (m < escapeRadius && iter < dwellLimit);

                float h = (float) iter / (float) dwellLimit;
                float b = 1.0f - h * h;
                g.setColor(Color.getHSBColor(h, 0.8f, b));
                g.drawLine(x, y, x, y);
            }

            if (Thread.interrupted()) {
                interrupted = true;
                break;
            }
            if (x % 25 == 0) {
                fireUpdate(100 * x / width);
            }
        }

        if (!interrupted) {
            fireUpdate(100);
        }
    }
}
