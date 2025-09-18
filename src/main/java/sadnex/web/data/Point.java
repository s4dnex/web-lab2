package sadnex.web.data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

public record Point(BigInteger x, BigDecimal y, BigDecimal r) {

    public Map<String, String> toMap() {
        return Map.of(
                Parameter.X.toString(), x.toString(),
                Parameter.Y.toString(), y.toString(),
                Parameter.R.toString(), r.toString()
        );
    }
}
