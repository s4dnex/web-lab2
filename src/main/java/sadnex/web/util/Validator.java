package sadnex.web.util;

import sadnex.web.fcgi.ResponseBodyKey;
import sadnex.web.data.Point;
import sadnex.web.exception.ValidationException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Map;

public class Validator {
    public Point validatePoint(Map<String, Object> params) {
        BigInteger x;
        try {
            x = new BigInteger(String.valueOf(params.get(ResponseBodyKey.X.toString())));
            if (x.compareTo(new BigInteger("-5")) < 0 || x.compareTo(new BigInteger("3")) > 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException|NullPointerException e) {
            throw new ValidationException("X must be an integer from -5 to 3");
        }

        BigDecimal y;
        try {
            y = new BigDecimal(String.valueOf(params.get(ResponseBodyKey.Y.toString())));
            if (y.compareTo(new BigDecimal("-3")) < 0 || y.compareTo(new BigDecimal("5")) > 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException|NullPointerException e) {
            throw new ValidationException("Y must be a decimal from -3 to 5");
        }

        BigDecimal r;
        try {
            r = new BigDecimal(String.valueOf(params.get(ResponseBodyKey.R.toString())));
            if (r.compareTo(new BigDecimal("2")) < 0 || r.compareTo(new BigDecimal("5")) > 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException|NullPointerException e) {
            throw new ValidationException("R must be a decimal from 2 to 5");
        }

        return new Point(x, y, r);
    }

    public boolean checkHit(Point point) {
        BigDecimal x = new BigDecimal(point.x());
        BigDecimal y = point.y();
        BigDecimal r = point.r();

        MathContext mc = MathContext.DECIMAL128;

        System.err.println(x + "\n" + y + "\n" + r);

        // Q1: x >= 0 && y >= 0
        if (x.compareTo(BigDecimal.ZERO) >= 0 && y.compareTo(BigDecimal.ZERO) >= 0) {
            System.err.println("q1");
            BigDecimal halfR = r.divide(new BigDecimal(2), mc);
            BigDecimal left = x.multiply(x).add(y.multiply(y));
            BigDecimal right = halfR.multiply(halfR);
            System.err.println(left.toString() + " " + right.toString());
            return left.compareTo(right) <= 0;
        }

        // Q2: x < 0 && y > 0
        if (x.compareTo(BigDecimal.ZERO) < 0 && y.compareTo(BigDecimal.ZERO) > 0) {
            System.err.println("q2");
            return false;
        }

        // Q3: x <= 0 && y <= 0
        if (x.compareTo(BigDecimal.ZERO) <= 0 && y.compareTo(BigDecimal.ZERO) <= 0) {
            System.err.println("q3");
            BigDecimal absX = x.abs();
            BigDecimal absY = y.abs();
            return absX.compareTo(r) <= 0 && absY.compareTo(r) <= 0;
        }

        // Q4: x >= 0 && y <= 0
        if (x.compareTo(BigDecimal.ZERO) >= 0 && y.compareTo(BigDecimal.ZERO) <= 0) {
            System.err.println("q4");
            return y.subtract(x).compareTo(r.negate()) >= 0;
        }

        return false;
    }
}
