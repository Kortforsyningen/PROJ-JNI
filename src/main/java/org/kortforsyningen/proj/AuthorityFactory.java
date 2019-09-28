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

import java.util.Objects;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.util.FactoryException;


/**
 * Wrappers around {@code osgeo::proj::io::AuthorityFactory}.
 * This is an entry point by which geodetic objects can be created from authority codes.
 *
 * <p>Each {@code osgeo::proj::io::AuthorityFactory} contains (indirectly) a reference
 * to a {@code PJ_CONTEXT} object. Consequently those two objects shall be used in the
 * same thread.</p>
 *
 * @author  Martin Desruisseaux (Geomatys)
 * @version 1.0
 * @since   1.0
 */
final class AuthorityFactory extends NativeResource {
    /**
     * The pointer to PROJ structure allocated in the C/C++ heap. This value has no meaning in Java code.
     * <strong>Do not modify</strong>, since this value is required for using PROJ. Do not rename neither,
     * unless potential usage of this field is also verified in the C/C++ source code.
     */
    private final long ptr;

    /**
     * Creates a new factory for the given authority.
     * This constructor should not be invoked directly; use {@link Context#factory(String)} instead.
     *
     * @param  context    pointer to the PROJ thread context.
     * @param  authority  the authority name, for example {@code "EPSG"}.
     * @param  sibling    if another factory has been created for the same context, that factory.
     *                    Otherwise null. This is used for sharing the same database context.
     * @throws FactoryException if the factory can not be created.
     *
     * @see Context#factory(String)
     */
    AuthorityFactory(final long context, final String authority, final AuthorityFactory sibling) throws FactoryException {
        Objects.requireNonNull(authority);
        ptr = newInstance(context, authority, (sibling != null) ? sibling.ptr : 0);
    }

    /**
     * Instantiates an {@code osgeo::proj::io::AuthorityFactory}.
     * Also creates the {@code osgeo::proj::io::DatabaseContext}.
     * Each database context can be used by only one thread.
     *
     * @param  context    pointer to the PROJ thread context.
     * @param  authority  name of the authority. Shall not be null.
     * @param  sibling    if another factory has been created for the same context, pointer to that factory.
     *                    Otherwise zero. This is used for sharing the same database context when possible.
     * @return shared pointer to the factory, or 0 if out of memory.
     * @throws FactoryException if the factory can not be created.
     */
    private static native long newInstance(long context, String authority, long sibling) throws FactoryException;

    /**
     * Returns the pointer to a {@code cs::CoordinateSystem} from the specified code.
     *
     * @param  ptr   pointer to the {@code osgeo::proj::io::AuthorityFactory} wrapped by this class.
     * @param  code  object code allocated by authority.
     * @return pointer to the PROJ {@code osgeo::proj::cs::CoordinateSystem}, or 0 if out of memory.
     * @throws FactoryException if no object can be created for the given code.
     */
    private static native long createCoordinateSystem(long ptr, String code) throws FactoryException;

    /**
     * Releases resources used by this factory. This method decrements the {@code object.use_count()}
     * value of the shared pointer. The authority factory is not necessarily immediately destroyed;
     * it depends on whether it is still used by other C++ code.
     */
    native void release();


    /**
     * Reference a {@code AuthorityFactory.createFoo(…)} method to invoke for creating objects.
     * This interface allows authority factories to use a single method for creating any kind of
     * objects (coordinate system, CRS, datum, <i>etc.</i>) identified by a single authority code.
     */
    private static interface Invoker {
        /**
         * Creates a new object for the given code.
         *
         * @param  ptr   pointer to the {@code osgeo::proj::io::AuthorityFactory} to use.
         * @param  code  object code allocated by authority.
         * @return pointer to the PROJ object, or 0 if out of memory.
         * @throws FactoryException if no object can be created for the given code.
         */
        long create(long ptr, String code) throws FactoryException;
    }


    /**
     * The authority factory for coordinate systems.
     */
    static final class CS {
        /**
         * The authority name of this factory.
         */
        private final String authority;

        /**
         * Creates a new factory for the given authority.
         *
         * @param  authority  authority name of this factory.
         */
        CS(final String authority) {
            this.authority = authority;
        }

        /**
         * Creates an object (coordinate system, CRS, datum, …) for the given authority code.
         * This method delegates to the {@code create} method specified by the given handler.
         *
         * @param  handler  the method to invoke.
         * @param  code     object code allocated by authority.
         * @return pointer to the PROJ object, or 0 if out of memory.
         * @throws FactoryException if no object can be created for the given code.
         */
        private long create(final Invoker handler, final String code) throws FactoryException {
            Objects.requireNonNull(code);
            try (Context c = Context.acquire()) {
                return handler.create(c.factory(authority).ptr, code);
            }
        }

        /**
         * Returns an arbitrary coordinate system from a code.
         *
         * @param  code  value allocated by authority.
         * @return the coordinate system for the given code.
         * @throws FactoryException if the object creation failed.
         */
        public CoordinateSystem createCoordinateSystem(final String code) throws FactoryException {
            return new org.kortforsyningen.proj.CS(create(AuthorityFactory::createCoordinateSystem, code));
        }
    }
}
