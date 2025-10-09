package sadnex.web.model;

import sadnex.web.http.BodyKey;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

public record Point(BigInteger x, BigDecimal y, BigDecimal r) {
    public Map<BodyKey, Object> toMap() {
        return Map.of(
                BodyKey.X, x.toString(),
                BodyKey.Y, y.toString(),
                BodyKey.R, r.toString()
        );
    }
}
