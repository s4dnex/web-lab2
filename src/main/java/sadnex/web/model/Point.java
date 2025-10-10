package sadnex.web.model;

import sadnex.web.http.BodyKey;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Map;

public record Point(BigInteger x, BigDecimal y, BigDecimal r, Result result) {
    public Point(BigInteger x, BigDecimal y, BigDecimal r) {
        this(x, y, r, checkHit(x, y, r) ? Result.OK : Result.MISS);
    }

    private static boolean checkHit(BigInteger x, BigDecimal y, BigDecimal r) {
        BigDecimal xDecimal = new BigDecimal(x);

        MathContext mc = MathContext.DECIMAL128;

        System.err.println(xDecimal + "\n" + y + "\n" + r);

        // Q1: x >= 0 && y >= 0
        if (xDecimal.compareTo(BigDecimal.ZERO) >= 0 && y.compareTo(BigDecimal.ZERO) >= 0) {
            System.err.println("q1");
            return false;
        }

        // Q2: x < 0 && y > 0
        if (xDecimal.compareTo(BigDecimal.ZERO) < 0 && y.compareTo(BigDecimal.ZERO) > 0) {
            System.err.println("q2");
            BigDecimal absX = xDecimal.abs();
            BigDecimal absY = y.abs();
            return absX.compareTo(r.divide(BigDecimal.TWO, mc)) <= 0 && absY.compareTo(r) <= 0;
        }

        // Q3: x <= 0 && y <= 0
        if (xDecimal.compareTo(BigDecimal.ZERO) <= 0 && y.compareTo(BigDecimal.ZERO) <= 0) {
            System.err.println("q3");
            BigDecimal halfR = r.divide(BigDecimal.TWO, mc);
            BigDecimal left = xDecimal.multiply(xDecimal).add(y.multiply(y));
            BigDecimal right = halfR.multiply(halfR);
            System.err.println(left.toString() + " " + right.toString());
            return left.compareTo(right) <= 0;
        }

        // Q4: x >= 0 && y <= 0
        if (xDecimal.compareTo(BigDecimal.ZERO) >= 0 && y.compareTo(BigDecimal.ZERO) <= 0) {
            System.err.println("q4");
            return y.subtract(xDecimal).compareTo(r.negate()) >= 0;
        }

        return false;
    }

    public Map<BodyKey, Object> toMap() {
        return Map.of(
                BodyKey.X, x.toString(),
                BodyKey.Y, y.toString(),
                BodyKey.R, r.toString(),
                BodyKey.RESULT, result.toString()
        );
    }
}
