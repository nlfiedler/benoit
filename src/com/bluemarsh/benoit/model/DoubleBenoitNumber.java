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
 * $Id: DoubleBenoitNumber.java 2003 2005-09-12 04:51:14Z nfiedler $
 *
 ********************************************************************/

package com.bluemarsh.benoit.model;

import java.math.BigDecimal;

/**
 * The double implementation of the BenoitNumber class.
 *
 * @author  Nathan Fiedler
 */
class DoubleBenoitNumber implements BenoitNumberImpl {
    /** The value of this number. */
    protected double value;

    /**
     * One-arg constructor that takes a double-precision value.
     *
     * @param  value  double-precision initial value
     */
    public DoubleBenoitNumber(double value) {
        this.value = value;
    } // DoubleBenoitNumber

    /**
     * Adds the given value to this number. For this implementation
     * it will only add the double-precision value.
     *
     * @param  a  value to add to this number as a double
     */
    public void add(double a) {
        value += a;
    } // add

    /**
     * Adds the given value to this number. For this implementation
     * it will only add the double-precision value.
     *
     * @param  a  value to add to this number as a double
     */
    public void add(BenoitNumber a) {
        value += a.doubleValue();
    } // add

    /**
     * Returns the BigDecimal-precision value of this number.
     *
     * @return  value of this number in BigDecimal format
     */
    public BigDecimal bigValue() {
        return new BigDecimal(value);
    } // bigValue

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
    public int compareTo(Object o) {
        BenoitNumber n = (BenoitNumber) o;
        double val = n.doubleValue();
        if (val < value) {
            return 1;
        } else if (val > value) {
            return -1;
        } else {
            return 0;
        }
    } // compareTo

    /**
     * Divides this number with the value of a. The result is
     * this equals this divided by a.
     *
     * @param  a  number to divide into this number
     */
    public void divide(double a) {
        value /= a;
    } // divide

    /**
     * Divides this number with the value of a. The result is
     * this equals this divided by a.
     *
     * @param  a  number to divide into this number
     */
    public void divide(BenoitNumber a) {
        value /= a.doubleValue();
    } // divide

    /**
     * Returns the double-precision value of this number.
     *
     * @return  value of this number in double format
     */
    public double doubleValue() {
        return value;
    } // doubleValue

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param  obj  the reference object with which to compare.
     * @return  true if this object is the same as the obj argument;
     *          false otherwise.
     */
    public boolean equals(Object obj) {
        if (obj instanceof BenoitNumberImpl) {
            BenoitNumberImpl bni = (BenoitNumberImpl) obj;
            return value == bni.doubleValue();
        } else {
            return false;
        }
    } // equals

    /**
     * Returns the float-precision value of this number.
     *
     * @return  value of this number in float format
     */
    public float floatValue() {
        return (float) value;
    } // floatValue

    /**
     * Returns the int-precision value of this number.
     *
     * @return  value of this number in int format
     */
    public int intValue() {
        return (int) value;
    } // intValue

    /**
     * Returns the long-precision value of this number.
     *
     * @return  value of this number in long format
     */
    public long longValue() {
        return (long) value;
    } // longValue

    /**
     * Mutliplies the given number with this one.
     *
     * @param  a  multiplier
     */
    public void multiply(double a) {
        value *= a;
    } // multiply

    /**
     * Mutliplies the given number with this one.
     *
     * @param  a  multiplier
     */
    public void multiply(BenoitNumber a) {
        value *= a.doubleValue();
    } // multiply

    /**
     * Sets the value of this number to a new value.
     *
     * @param  value  new value
     */
    public void setValue(double value) {
        this.value = value;
    } // setValue

    /**
     * Finds the square root of this number.
     */
    public void sqrt() {
        value = Math.sqrt(value);
    } // sqrt

    /**
     * Subtracts the given number from this one.
     *
     * @param  a  number to subtract from this one
     */
    public void subtract(BenoitNumber a) {
        value -= a.doubleValue();
    } // subtract

    /**
     * Returns the string representation of this number.
     *
     * @return  the string representation of this number
     */
    public String toString() {
        return Double.toString(value);
    } // toString
} // DoubleBenoitNumber
