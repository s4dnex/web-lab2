package sadnex.web.http;

import com.google.gson.reflect.TypeToken;
import jakarta.servlet.http.HttpServletRequest;
import sadnex.web.util.JsonManager;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class RequestReader {
    private final JsonManager jsonManager = new JsonManager();

    public String readBody(HttpServletRequest request) throws IOException {
        var buffer = request.getInputStream().readAllBytes();
        return new String(buffer, StandardCharsets.UTF_8);
    }

    public Map<String, Object> readJsonBody(HttpServletRequest request) throws IOException {
        String body = readBody(request);
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> json;

        try {
            json = jsonManager.fromJson(body, type);
        } catch (Exception e) {
            json = null;
        }

        return json;
    }
}
