package sadnex.web.fcgi;

import java.nio.charset.StandardCharsets;

public class ResponseSupplier {
    private static final String RESPONSE = """
            Status: %d %s
            Content-Type: %s
            Content-Length: %d
            
            %s
            """;

    public String getResponse(HttpStatusCode result, ContentType contentType, String body) {
        return String.format(RESPONSE,
                result.getCode(),
                result.getDescription(),
                contentType.toString(),
                body.getBytes(StandardCharsets.UTF_8).length,
                body
        );
    }
}
