package sadnex.web.util;

import sadnex.web.exception.ValidationException;
import sadnex.web.http.BodyKey;
import sadnex.web.model.Point;

import java.math.BigDecimal;
import java.util.Map;

public class Validator {
    public Point validatePoint(Map<String, Object> params) throws ValidationException {
        BigDecimal x;
        try {
            x = new BigDecimal(String.valueOf(params.get(BodyKey.X.toString())));
            if (x.compareTo(new BigDecimal("-5")) < 0 || x.compareTo(new BigDecimal("3")) > 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException | NullPointerException e) {
            throw new ValidationException("X must be an integer from -5 to 3");
        }

        BigDecimal y;
        try {
            y = new BigDecimal(String.valueOf(params.get(BodyKey.Y.toString())));
            if (y.compareTo(new BigDecimal("-5")) < 0 || y.compareTo(new BigDecimal("3")) > 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException | NullPointerException e) {
            throw new ValidationException("Y must be a decimal from -5 to 3");
        }

        BigDecimal r;
        try {
            r = new BigDecimal(String.valueOf(params.get(BodyKey.R.toString())));
            if (r.compareTo(new BigDecimal("2")) < 0 || r.compareTo(new BigDecimal("5")) > 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException | NullPointerException e) {
            throw new ValidationException("R must be a decimal from 2 to 5");
        }

        return new Point(x, y, r);
    }
}
