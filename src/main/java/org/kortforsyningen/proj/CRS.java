/*
 * Copyright © 2019 Agency for Data Supply and Efficiency
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.kortforsyningen.proj;

import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.CoordinateSystem;


/**
 * Wrappers around {@code osgeo::proj::crs::CRS} subtypes.
 * Each subtype is represented by an inner class in this file.
 *
 * @author  Martin Desruisseaux (Geomatys)
 * @version 1.0
 * @since   1.0
 */
class CRS extends IdentifiableObject implements CoordinateReferenceSystem {
    /**
     * Default number of dimensions when we can not infer it.
     */
    static final int DEFAULT_DIMENSION = 2;

    /**
     * Casts the given value to {@link CRS}.
     *
     * @param  name   argument name, used only for formatting error message.
     * @param  value  value to cast.
     * @return the given CRS as a PROJ implementation.
     * @throws IllegalArgumentException if the given value is {@code null}
     *         or is not a PROJ implementation.
     */
    static CRS cast(final String name, final CoordinateReferenceSystem value) {
        if (value instanceof CRS) {
            return (CRS) value;
        } else {
            throw new UnsupportedImplementationException(UnsupportedImplementationException.message(name, value));
        }
    }

    /**
     * Creates a new wrapper for the given {@code osgeo::proj::crs::CRS}.
     *
     * @param  ptr  pointer to the wrapped PROJ object.
     */
    CRS(final long ptr) {
        super(ptr);
    }

    /**
     * Returns the number of dimensions of this CRS.
     * This is a shortcut for {@code getCoordinateSystem().getDimension}.
     *
     * @return number of dimensions in this CRS.
     *
     * @see CoordinateSystem#getDimension()
     */
    final int getDimension() {
        return DEFAULT_DIMENSION;                   // TODO
    }

    /**
     * Returns the coordinate system of a single CRS, or a view over all coordinate systems of a compound CRS.
     *
     * @return the coordinate system.
     */
    @Override
    public CoordinateSystem getCoordinateSystem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}