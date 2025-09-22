package sadnex.web.data;

import sadnex.web.fcgi.ResponseBodyKey;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

public record Point(BigInteger x, BigDecimal y, BigDecimal r) {
    public Map<ResponseBodyKey, Object> toMap() {
        return Map.of(
                ResponseBodyKey.X, x.toString(),
                ResponseBodyKey.Y, y.toString(),
                ResponseBodyKey.R, r.toString()
        );
    }
}
