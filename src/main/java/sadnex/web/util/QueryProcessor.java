package sadnex.web.util;

import sadnex.web.exception.ValidationException;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryProcessor {
    public static Map<String, String> queryToMap(String query) {
        if (query == null || query.isBlank()) {
            throw new ValidationException("Empty request's parameters");
        }

        try {
            return Arrays.stream(query.split("&"))
                    .map(pair -> pair.split("="))
                    .collect(
                            Collectors.toMap(
                                    pair -> URLDecoder.decode(pair[0], StandardCharsets.UTF_8),
                                    pair -> URLDecoder.decode(pair[1], StandardCharsets.UTF_8),
                                    (a, b) -> b,
                                    HashMap::new
                            )
                    );
        } catch (Exception e) {
            throw new ValidationException("Couldn't parse request's parameters: " + query);
        }
    }
}
