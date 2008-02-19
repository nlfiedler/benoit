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
 * $Id: BenoitNumber.java 2003 2005-09-12 04:51:14Z nfiedler $
 *
 ********************************************************************/

package com.bluemarsh.benoit.model;

import java.math.BigDecimal;

/**
 * An arbitrary precision numeric value. Supports the usual numeric
 * operations: add, subtract, multiply, and divide. Trigonometric
 * functions are too difficult with BigDecimal numbers and so they
 * are not provided.
 *
 * <p>What makes this class different is the ability to set the
 * precision of the numbers at run-time. Rather than using just double
 * numbers, the user can set the precision at run-time as desired. This
 * is easily done with the <code>setType()</code> method. The types
 * supported are DOUBLE_TYPE and BIG_TYPE. These are double and
 * BigDecimal precision, respectively.</p>
 *
 * @author  Nathan Fiedler
 */
public class BenoitNumber extends Number implements Comparable, Cloneable {
    /** silence the compiler warnings */
    private static final long serialVersionUID = 1L;
    /** Indicates this number is a double-precision value. */
    public static final int DOUBLE_TYPE = -1;
    /** Indicates this number is a BigDecimal-precision value. */
    public static final int BIG_TYPE = -2;
    /** The particular implementation of the Benoit number.
     * This holds the actual value of the number. */
    protected BenoitNumberImpl imp;

    /**
     * Constructor that takes a double precision value.
     *
     * @param  value  double-precision initial value
     */
    public BenoitNumber(double value) {
        imp = new DoubleBenoitNumber(value);
    } // BenoitNumber

    /**
     * Constructor that takes a BigDecimal precision value.
     *
     * @param  value  BigDecimal-precision initial value
     */
    public BenoitNumber(BigDecimal value) {
        imp = new BigDecimalBenoitNumber(value);
    } // BenoitNumber

    /**
     * Adds a value to this number.
     *
     * @param  a  value to add to this number
     */
    public void add(double a) {
        imp.add(a);
    } // add

    /**
     * Adds a value to this number.
     *
     * @param  a  value to add to this number
     */
    public void add(BenoitNumber a) {
        imp.add(a);
    } // add

    /**
     * Adds the two given numbers together and returns a new
     * number that is the result.
     *
     * @param  a  first number to add
     * @param  b  second number to add
     * @return  sum of a and b
     */
    public static BenoitNumber add(BenoitNumber a, double b) {
        BenoitNumber sum = (BenoitNumber) a.clone();
        sum.add(b);
        return sum;
    } // add

    /**
     * Adds the two given numbers together and returns a new
     * number that is the result.
     *
     * @param  a  first number to add
     * @param  b  second number to add
     * @return  sum of a and b
     */
    public static BenoitNumber add(BenoitNumber a, BenoitNumber b) {
        BenoitNumber sum = (BenoitNumber) a.clone();
        sum.add(b);
        return sum;
    } // add

    /**
     * Returns the BigDecimal-precision value of this number.
     *
     * @return  value of this number in BigDecimal format
     */
    public BigDecimal bigValue() {
        return imp.bigValue();
    } // bigValue

    /**
     * Creates a complete clone of this number object.
     *
     * @return  full clone of this object
     */
    public Object clone() {
        Object obj = null;
        if (imp instanceof DoubleBenoitNumber) {
            obj = new BenoitNumber(imp.doubleValue());
        } else if (imp instanceof BigDecimalBenoitNumber) {
            obj = new BenoitNumber(imp.bigValue());
        }
        return obj;
    } // clone

    /**
     * Compares this object to the given object for order. Returns a
     * negative integer, zero, or a positive integer as this Object
     * is less than, equal to, or greater than the given Object.
     *
     * @param  o  object to compare to for order.
     * @return  negative number if less than,
     *          zero if equal, or
     *          positive number if greater than, given object.
     */
    public int compareTo(Object o) {
        return imp.compareTo(o);
    } // compareTo

    /**
     * Divides this number with the value of a. The result is
     * this equals this divided by a.
     *
     * @param  a  divisor.
     */
    public void divide(double a) {
        imp.divide(a);
    } // divide

    /**
     * Divides this number with the value of a. The result is
     * this equals this divided by a.
     *
     * @param  a  divisor.
     */
    public void divide(BenoitNumber a) {
        imp.divide(a);
    } // divide

    /**
     * Divides the first number with the second number and returns
     * the result as a new number. The result is a / b.
     *
     * @param  a  dividend
     * @param  b  divisor
     * @return  quotient
     */
    public static BenoitNumber divide(BenoitNumber a, BenoitNumber b) {
        BenoitNumber quot = (BenoitNumber) a.clone();
        quot.divide(b);
        return quot;
    } // divide

    /**
     * Returns the double-precision value of this number.
     *
     * @return  value of this number in double format
     */
    public double doubleValue() {
        return imp.doubleValue();
    } // doubleValue

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param  obj  the reference object with which to compare.
     * @return  true if this object is the same as the obj argument;
     *          false otherwise.
     */
    public boolean equals(Object obj) {
        if (obj instanceof BenoitNumber) {
            BenoitNumber bnobj = (BenoitNumber) obj;
            return imp.equals(bnobj.imp);
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
        return imp.floatValue();
    } // floatValue

    /**
     * Returns the precision of this number. Equal to either
     * DOUBLE_TYPE or BIG_TYPE.
     *
     * @return  the precision of this number
     */
    public int getType() {
        int type = 0;
        if (imp instanceof DoubleBenoitNumber) {
            type = DOUBLE_TYPE;
        } else if (imp instanceof BigDecimalBenoitNumber) {
            type = BIG_TYPE;
        }
        return type;
    } // getType

    /**
     * Gets the numeric scale of this number. This is equivalent
     * to the number of digits this number can store. This only
     * has meaning when called on BigDecimal numbers.
     *
     * @return  scale of this number, or zero.
     */
    public int getScale() {
        if (imp instanceof BigDecimalBenoitNumber) {
            return ((BigDecimalBenoitNumber) imp).getScale();
        } else {
            return BigDecimalBenoitNumber.DEFAULT_SCALE;
        }
    } // getScale

    /**
     * Returns the int-precision value of this number.
     *
     * @return  value of this number in int format
     */
    public int intValue() {
        return imp.intValue();
    } // intValue

    /**
     * Returns the long-precision value of this number.
     *
     * @return  value of this number in long format
     */
    public long longValue() {
        return imp.longValue();
    } // longValue

    /**
     * Multiplies the given number with this one.
     *
     * @param  a  multiplier
     */
    public void multiply(double a) {
        imp.multiply(a);
    } // multiply

    /**
     * Multiplies the given number with this one.
     *
     * @param  a  multiplier
     */
    public void multiply(BenoitNumber a) {
        imp.multiply(a);
    } // multiply

    /**
     * Multiplies the two given numbers and returns a new number
     * that holds the result.
     *
     * @param  a  multiplicand
     * @param  b  multiplier
     * @return  product of a and b
     */
    public static BenoitNumber multiply(BenoitNumber a, double b) {
        BenoitNumber prod = (BenoitNumber) a.clone();
        prod.multiply(b);
        return prod;
    } // multiply

    /**
     * Multiplies the two given numbers and returns a new number
     * that holds the result.
     *
     * @param  a  multiplicand
     * @param  b  multiplier
     * @return  product of a and b
     */
    public static BenoitNumber multiply(BenoitNumber a, BenoitNumber b) {
        BenoitNumber prod = (BenoitNumber) a.clone();
        prod.multiply(b);
        return prod;
    } // multiply

    /**
     * Sets the number of digits this number will store. This only
     * has an affect for big decimal numbers.
     *
     * @param  numDigits  number of digits to store.
     */
    public void setScale(int numDigits) {
        if (imp instanceof BigDecimalBenoitNumber) {
            ((BigDecimalBenoitNumber) imp).setScale(numDigits);
        }
    } // setScale

    /**
     * Sets the precision of this number. Can be set to either
     * DOUBLE_TYPE or BIG_TYPE.
     *
     * @param  type  the new precision of this number
     */
    public void setType(int type) {
        if (getType() == type) {
            return;
        }
        switch (type) {
        case DOUBLE_TYPE :
            imp = new DoubleBenoitNumber(doubleValue());
            break;
        case BIG_TYPE :
            imp = new BigDecimalBenoitNumber(bigValue());
            break;
        default :
            throw new IllegalArgumentException("Invalid type");
        }
    } // setType

    /**
     * Sets the value of this number to a new double precision
     * value. If it is type BigDecimal, then the value will be
     * turned into a BigDecimal and setValue(BigDecimal) will be
     * called instead.
     *
     * @param  value  new value
     */
    public void setValue(double value) {
        int type = getType();
        if (type == DOUBLE_TYPE) {
            ((DoubleBenoitNumber) imp).setValue(value);
        } else {
            setValue(new BigDecimal(value));
        }
    } // setValue

    /**
     * Sets the value of this number to a new BigDecimal precision
     * value. If the type of this number is not BigDecimal, the
     * number will be replaced by a BigDecimal precision number
     * with the given value.
     *
     * @param  value  new value
     */
    public void setValue(BigDecimal value) {
        if (getType() == BIG_TYPE) {
            ((BigDecimalBenoitNumber) imp).setValue(value);
        } else {
            imp = new BigDecimalBenoitNumber(value);
        }
    } // setValue

    /**
     * Finds the square root of the given number and returns
     * it in a new number.
     *
     * @param  a  number to find square root of
     * @return  square root of parameter
     */
    public static BenoitNumber sqrt(BenoitNumber a) {
        BenoitNumber result = (BenoitNumber) a.clone();
        result.imp.sqrt();
        return result;
    } // sqrt

    /**
     * Subtracts the given number from this one.
     *
     * @param  a  number to subtract from this one
     */
    public void subtract(BenoitNumber a) {
        imp.subtract(a);
    } // subtract

    /**
     * Subtracts the two given numbers and returns the result
     * in a new number. The result is a - b.
     *
     * @param  a  number to subtract from
     * @param  b  amount to subtract from a
     * @return  difference between a and b (a-b)
     */
    public static BenoitNumber subtract(BenoitNumber a, BenoitNumber b) {
        BenoitNumber result = (BenoitNumber) a.clone();
        result.subtract(b);
        return result;
    } // subtract

    /**
     * Returns the string representation of this number.
     *
     * @return  the string representation of this number
     */
    public String toString() {
        return imp.toString();
    } // toString

    /**
     * Returns a new BenoitNumber value initialized to the value
     * represented by the specified String. The precision of the
     * new number is determined by the type parameter.
     *
     * @param  s     the string to be parsed
     * @param  type  the precision for the new number
     * @throws  IllegalArgumentException
     *          if type is invalid.
     * @throws  NumberFormatException
     *          if number is malformed.
     */
    public static BenoitNumber valueOf(String s, int type) {
        BenoitNumber bn = null;
        switch (type) {
        case DOUBLE_TYPE:
            bn = new BenoitNumber(Double.valueOf(s).doubleValue());
            break;
        case BIG_TYPE:
            bn = new BenoitNumber(new BigDecimal(s));
            break;
        default :
            throw new IllegalArgumentException("Invalid type");
        }
        return bn;
    } // valueOf
} // BenoitNumber
