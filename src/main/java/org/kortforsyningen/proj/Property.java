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

import java.lang.annotation.Native;


/**
 * Identification of a property to read in an object.
 *
 * @author  Martin Desruisseaux (Geomatys)
 * @version 1.0
 * @since   1.0
 */
final class Property {
    /**
     * Do not allow instantiation of this class.
     */
    private Property() {
    }

    /**
     * Identify properties which can be returned by {@link SharedPointer#getObjectProperty(short)} method.
     */
    @Native
    static final short NAME              =  0,
                       COORDINATE_SYSTEM =  1,
                       DATUM             =  2,
                       ELLIPSOID         =  3,
                       PRIME_MERIDIAN    =  4,
                       BASE_CRS          =  5,
                       CONVERT_FROM_BASE =  6,
                       OPERATION_METHOD  =  7;

    /**
     * Identify properties which can be returned by {@link SharedPointer#getVectorElement(short, int)} method.
     */
    @Native
    static final short AXIS              =  0,
                       IDENTIFIER        =  1,
                       SOURCE_TARGET_CRS =  2;       // Index 0 for source, 1 for target.

    /**
     * Identify properties which can be returned by {@link SharedPointer#getStringProperty(short)} method.
     */
    @Native
    static final short NAME_STRING       =  0,
                       IDENTIFIER_STRING =  1,
                       CODESPACE         =  2,
                       CODE              =  3,
                       VERSION           =  4,
                       CITATION_TITLE    =  5,
                       ABBREVIATION      =  6,
                       DIRECTION         =  7,
                       SCOPE             =  8,
                       REMARKS           =  9,
                       FORMULA           = 10,
                       FORMULA_TITLE     = 11,
                       OPERATION_VERSION = 12;

    /**
     * Identify properties which can be returned by {@link SharedPointer#getNumericProperty(short)} method.
     */
    @Native
    static final short MINIMUM = 0,
                       MAXIMUM = 1;
}
