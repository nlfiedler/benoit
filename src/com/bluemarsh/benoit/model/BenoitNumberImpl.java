/*********************************************************************
 *
 *      Copyright (C) 1999-2005 Nathan Fiedler
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
 * $Id: BenoitNumberImpl.java 2003 2005-09-12 04:51:14Z nfiedler $
 *
 ********************************************************************/

package com.bluemarsh.benoit.model;

import java.math.BigDecimal;

/**
 * Interface for the various Benoit number implementations.
 *
 * @author  Nathan Fiedler
 */
interface BenoitNumberImpl {

    /**
     * Adds the given value to this number.
     *
     * @param  a  value to add to this number
     */
    void add(double a);

    /**
     * Adds the given value to this number.
     *
     * @param  a  value to add to this number
     */
    void add(BenoitNumber a);

    /**
     * Returns the BigDecimal-precision value of this number.
     *
     * @return  value of this number in BigDecimal format
     */
    BigDecimal bigValue();

    /**
     * Compares this object to the given object for order. Returns a
     * negative integer, zero, or a positive integer as this Object
     * is less than, equal to, or greater than the given Object.
     *
     * @param  o  object to compare to for order
     * @return  negative number if less than,
     *          zero if equal, or
     *          positive number if greater than, given object
     */
    int compareTo(Object o);

    /**
     * Divides this number with the value of a. The result is
     * this equals this divided by a.
     *
     * @param  a  divisor
     */
    void divide(double a);

    /**
     * Divides this number with the value of a. The result is
     * this equals this divided by a.
     *
     * @param  a  divisor
     */
    void divide(BenoitNumber a);

    /**
     * Returns the double-precision value of this number.
     *
     * @return  value of this number in double format
     */
    double doubleValue();

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param  obj  the reference object with which to compare.
     * @return  true if this object is the same as the obj argument;
     *          false otherwise.
     */
    boolean equals(Object obj);

    /**
     * Returns the float-precision value of this number.
     *
     * @return  value of this number in float format
     */
    float floatValue();

    /**
     * Returns the int-precision value of this number.
     *
     * @return  value of this number in int format
     */
    int intValue();

    /**
     * Returns the long-precision value of this number.
     *
     * @return  value of this number in long format
     */
    long longValue();

    /**
     * Multiplies the given number with this one.
     *
     * @param  a  multiplier
     */
    void multiply(double a);

    /**
     * Multiplies the given number with this one.
     *
     * @param  a  multiplier
     */
    void multiply(BenoitNumber a);

    /**
     * Finds the square root of this number.
     */
    void sqrt();

    /**
     * Subtracts the given number from this one.
     *
     * @param  a  number to subtract from this one
     */
    void subtract(BenoitNumber a);

    /**
     * Returns the string representation of this number.
     *
     * @return  the string representation of this number
     */
    String toString();
}
