## What Is It? ##

Benoit is a Java application that renders the Mandelbrot Set to the screen.
It has the capability to use arbitrary precision in its numeric
calculations.

## Features ##

### History ###

Benoit supports a history of visited regions of the set. That is, as you
visit regions of the set, the boundaries are saved in the order they were
viewed. You can visit old regions using the left arrow button in the
toolbar. To see the more recent regions, use the right arrow button.

### Image Caching ###

Benoit attempts to cache the rendered images in a memory sensitive manner.
That is, if there is sufficient free memory, the images will be cached
indefinitely. Under low-memory conditions, some or all of the cached images
may be freed. Thus revisiting past regions of the set (using the Back
button), may result in the image being rendered again, while other times it
may appear immediately.

### Precision ###

Benoit allows performing the calculations using arbitrary numeric
precision. It supports both double and BigDecimal precision. By default it
uses doubles, which are less precise but very fast. The BigDecimal type is
an object that represents a real number without any loss of precision. The
default scale of the BigDecimal is 30 digits. If you want more digits, you
must use the "Set Scale" menu item in the "Options" menu.

You will notice that BigDecimals are incredibly slow compared to doubles.
For this reason, the rendering algorithm is significantly different than
the one used when the calculations are performed with doubles. This is to
allow a rough representation of the region to appear initially, which is
continually refined in subsequent passes.
