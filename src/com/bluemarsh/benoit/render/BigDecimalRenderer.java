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
 * $Id: BigDecimalRenderer.java 2001 2005-09-12 02:08:06Z nfiedler $
 *
 ********************************************************************/

package com.bluemarsh.benoit.render;

import com.bluemarsh.benoit.model.Parameters;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.math.BigDecimal;

/**
 * The BigDecimal concrete implementation of a renderer.
 *
 * @author  Nathan Fiedler
 */
public class BigDecimalRenderer extends AbstractRenderer {

    /**
     * Renders the mandelbrot set to the given image.
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

        BigDecimal minX = params.getMinX().bigValue();
        BigDecimal maxX = params.getMaxX().bigValue();
        BigDecimal minY = params.getMinY().bigValue();
        BigDecimal maxY = params.getMaxY().bigValue();

        // Scale for the big decimals (i.e. number of digits).
        int numDigits = params.getMinX().getScale();

        // The explicit scale is needed, otherwise it defaults to two.
        BigDecimal dx = maxX.subtract(minX).divide(
            new BigDecimal((double) width), numDigits,
            BigDecimal.ROUND_HALF_UP);
        BigDecimal dy = maxY.subtract(minY).divide(
            new BigDecimal((double) height), numDigits,
            BigDecimal.ROUND_HALF_UP);

        int dwellLimit = 100;
        // avoid using sqrt() repeatedly by squaring the escape radius.
        double escapeRadius = 4.0; // 2.0 ^ 2

        // Use successive-refinement to give a rough representation
        // of the region before proceeding to more detailed images.
        boolean interrupted = false;
        BigDecimal two = new BigDecimal(2.0);
        for (int step = 16, base = 0; step > 0; step >>= 1, base += 20) {
            for (int x = 0; x < width; x += step) {
                BigDecimal cr = dx.multiply(new BigDecimal((double) x));
                cr = cr.setScale(numDigits, BigDecimal.ROUND_HALF_UP);
                cr = cr.add(minX);
                for (int y = 0; y < height; y += step) {

                    if (step != 16 && ((x / step) % 2) == 0
                        && ((y / step) % 2) == 0) {
                        continue;
                    }

                    BigDecimal ci = dy.multiply(new BigDecimal((double) y));
                    ci = ci.setScale(numDigits, BigDecimal.ROUND_HALF_UP);
                    ci = ci.add(minY);
                    BigDecimal zr = cr;
                    BigDecimal zi = ci;
                    double m;
                    int iter = 1;

                    do {
                        // z = z * z + c;
                        BigDecimal zrzr = zr.multiply(zr);
                        // Must set scale or number of digits explodes.
                        zrzr = zrzr.setScale(numDigits,
                                             BigDecimal.ROUND_HALF_UP);
                        BigDecimal zizi = zi.multiply(zi);
                        zizi = zizi.setScale(numDigits,
                                             BigDecimal.ROUND_HALF_UP);
                        BigDecimal r = zrzr.subtract(zizi);
                        BigDecimal zrzi = zr.multiply(zi);
                        zrzi = zrzi.setScale(numDigits,
                                             BigDecimal.ROUND_HALF_UP);
                        zi = two.multiply(zrzi);
                        zi = zi.setScale(numDigits,
                                         BigDecimal.ROUND_HALF_UP);
                        zi = zi.add(ci);
                        zr = r.add(cr);

                        // magnitude (would use sqrt() normally)
                        zrzr = zr.multiply(zr);
                        zrzr = zrzr.setScale(numDigits,
                                             BigDecimal.ROUND_HALF_UP);
                        zizi = zi.multiply(zi);
                        zizi = zizi.setScale(numDigits,
                                             BigDecimal.ROUND_HALF_UP);
                        m = zrzr.add(zizi).doubleValue();
                        iter++;
                    } while (m < escapeRadius && iter < dwellLimit);

                    float h = (float) iter / (float) dwellLimit;
                    float b = 1.0f - h * h;
                    g.setColor(Color.getHSBColor(h, 0.8f, b));
                    g.fillRect(x, y, step, step);
                }

                if (Thread.interrupted()) {
                    interrupted = true;
                    step = 0;
                    break;
                }
                if (x % 2 == 0) {
                    // For now the starting value for step of 16 implies
                    // five phases, so 100% / 5 = 20%, hence the value
                    // of 20 here. The 'base' value steps through the
                    // values 0, 20, 40, 60, and 80.
                    fireUpdate(20 * x / width + base);
                }
            }
        }

        // Fire off the final update.
        if (!interrupted) {
            fireUpdate(100);
        }
    }
}
