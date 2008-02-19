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
 * $Id: BigDecimalBenoitNumber.java 2003 2005-09-12 04:51:14Z nfiedler $
 *
 ********************************************************************/

package com.bluemarsh.benoit.model;

import java.math.BigDecimal;

/**
 * The BigDecimal implementation of the BenoitNumber class.
 *
 * @author  Nathan Fiedler
 */
class BigDecimalBenoitNumber implements BenoitNumberImpl {
    /** Default scale of this type. */
    public static final int DEFAULT_SCALE = 30;
    /** The value of this number. */
    protected BigDecimal value;
    /** Number of digits that will be stored by this number. */
    protected int numDigits;

    /**
     * One-arg constructor that takes a BigDecimal-precision value.
     *
     * @param  value  BigDecimal-precision initial value
     */
    public BigDecimalBenoitNumber(BigDecimal value) {
        this.value = value;
        numDigits = DEFAULT_SCALE;
    } // BigDecimalBenoitNumber

    /**
     * Adds the given value to this number. For this implementation
     * it will only add the BigDecimal-precision value.
     *
     * @param  a  value to add to this number as a BigDecimal
     */
    public void add(double a) {
        value = value.add(new BigDecimal(a));
    } // add

    /**
     * Adds the given value to this number. For this implementation
     * it will only add the BigDecimal-precision value.
     *
     * @param  a  value to add to this number as a BigDecimal
     */
    public void add(BenoitNumber a) {
        value = value.add(a.bigValue());
    } // add

    /**
     * Returns the BigDecimal-precision value of this number.
     *
     * @return  value of this number in BigDecimal format
     */
    public BigDecimal bigValue() {
        return value;
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
        return value.compareTo(n.bigValue());
    } // compareTo

    /**
     * Divides this number with the value of a. The result is
     * this equals this divided by a.
     *
     * @param  a  number to divide into this number
     */
    public void divide(double a) {
        // Yes, the explicit scale is necessary.
        value = value.divide(new BigDecimal(a), numDigits,
                             BigDecimal.ROUND_HALF_UP);
    } // divide

    /**
     * Divides this number with the value of a. The result is
     * this equals this divided by a.
     *
     * @param  a  number to divide into this number
     */
    public void divide(BenoitNumber a) {
        // Yes, the explicit scale is necessary.
        value = value.divide(a.bigValue(), numDigits,
                             BigDecimal.ROUND_HALF_UP);
    } // divide

    /**
     * Returns the double-precision value of this number.
     *
     * @return  value of this number in double format
     */
    public double doubleValue() {
        return value.doubleValue();
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
            // Compare without respect for the scale.
            return value.compareTo(bni.bigValue()) == 0;
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
        return value.floatValue();
    } // floatValue

    /**
     * Returns the scale of this big decimal number. This is the
     * same as the number of digits this number will store.
     *
     * @return  number of digits this number can hold.
     */
    public int getScale() {
        return numDigits;
    } // getScale

    /**
     * Returns the int-precision value of this number.
     *
     * @return  value of this number in int format
     */
    public int intValue() {
        return value.intValue();
    } // intValue

    /**
     * Returns the long-precision value of this number.
     *
     * @return  value of this number in long format
     */
    public long longValue() {
        return value.longValue();
    } // longValue

    /**
     * Mutliplies the given number with this one.
     *
     * @param  a  multiplier
     */
    public void multiply(double a) {
        value = value.multiply(new BigDecimal(a));
        value = value.setScale(numDigits, BigDecimal.ROUND_HALF_UP);
    } // multiply

    /**
     * Mutliplies the given number with this one.
     *
     * @param  a  multiplier
     */
    public void multiply(BenoitNumber a) {
        value = value.multiply(a.bigValue());
        value = value.setScale(numDigits, BigDecimal.ROUND_HALF_UP);
    } // multiply

    /**
     * Sets the number of digits this number will store.
     *
     * @param  numDigits  number of digits to store
     */
    public void setScale(int numDigits) {
        this.numDigits = numDigits;
        value = value.setScale(numDigits, BigDecimal.ROUND_HALF_UP);
    } // setScale

    /**
     * Sets the value of this number to a new value.
     *
     * @param  value  new value
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    } // setValue

    /**
     * Finds the square root of this number.
     */
    public void sqrt() {
        // a few handy constants to have
        BigDecimal half = new BigDecimal(0.5);
        BigDecimal zero = new BigDecimal(0.0);
        BigDecimal one = new BigDecimal(1.0);
        BigDecimal two = new BigDecimal(2.0);

        // Number to square root.
        BigDecimal y = value;

        // Initial guess. This will quickly converge to an
        // approximation of the answer in only a few iterations.
        BigDecimal a = y.add(one).multiply(half);

        // Initial reciprocal of guess. This is used to simply
        // the calculations below and avoid a division.
        BigDecimal b = (a.compareTo(one) < 0) ? two : half;

        // Iterative process for finding square root. Avoids
        // divisions and other trigonometric functions, but
        // is considered only an "approximation" of the answer.
        for (int c = 0; c < 14; c++) {
            a = half.multiply(a.add(y.multiply(b)));
            a = a.setScale(numDigits, BigDecimal.ROUND_HALF_UP);
            BigDecimal t = b.multiply(two.subtract(a.multiply(b)));
            t = t.setScale(numDigits, BigDecimal.ROUND_HALF_UP);
            b = (t.compareTo(zero) > 0) ? t : b.multiply(half).setScale(
                numDigits, BigDecimal.ROUND_HALF_UP);
        }
        value = a;
    } // sqrt

    /**
     * Subtracts the given number from this one.
     *
     * @param  a  number to subtract from this one
     */
    public void subtract(BenoitNumber a) {
        value = value.subtract(a.bigValue());
    } // subtract

    /**
     * Returns the string representation of this number.
     *
     * @return  the string representation of this number
     */
    public String toString() {
        return value.toString();
    } // toString
} // BigDecimalBenoitNumber
