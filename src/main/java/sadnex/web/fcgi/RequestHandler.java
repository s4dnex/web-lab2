package sadnex.web.fcgi;

import com.fastcgi.FCGIInterface;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import sadnex.web.data.Result;
import sadnex.web.exception.ValidationException;
import sadnex.web.util.JsonManager;
import sadnex.web.util.Validator;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler {
    public void start() throws IOException {
        System.err.println("FastCGI starting.");
        FCGIInterface fcgi = new FCGIInterface();

        while (fcgi.FCGIaccept() >= 0) {
            String method = FCGIInterface.request.params.getProperty("REQUEST_METHOD");
            System.err.println("Request method: " + method);

            HttpStatusCode status = HttpStatusCode.OK;
            Map<ResponseBodyKey, Object> responseMap = new HashMap<>();
            long startTime = System.nanoTime();

            switch (method.toUpperCase()) {
                case "POST":
                    try {
                        String body = readBody();
                        System.err.println("Request body: " + body);

                        Type type = new TypeToken<Map<String, Object>>() {}.getType();
                        Map<String, Object> json = JsonManager.fromJson(body, type);
                        if (json == null) json = new HashMap<>();

                        Validator validator = new Validator();
                        var point = validator.validatePoint(json);
                        Result result = validator.checkHit(point) ? Result.OK : Result.MISS;

                        responseMap.putAll(point.toMap());
                        responseMap.put(ResponseBodyKey.RESULT, result);
                    } catch (ValidationException|JsonSyntaxException e) {
                        status = HttpStatusCode.BAD_REQUEST;
                        responseMap = new HashMap<>();
                        responseMap.put(ResponseBodyKey.RESULT, Result.ERROR);
                        responseMap.put(ResponseBodyKey.ERROR, e.getMessage());
                    }
                    break;

                default:
                    status = HttpStatusCode.METHOD_NOT_ALLOWED;
                    responseMap = new HashMap<>();
                    responseMap.put(ResponseBodyKey.RESULT, Result.ERROR);
                    responseMap.put(ResponseBodyKey.ERROR, "Unsupported request method");
                    break;
            }

            responseMap.put(ResponseBodyKey.EXECUTION_TIME, ((System.nanoTime() - startTime) / Math.pow(10, 6)) + " ms");
            responseMap.put(ResponseBodyKey.CURRENT_TIME, LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)));
            String response = JsonManager.toJson(responseMap);

            ResponseSupplier responseSupplier = new ResponseSupplier();
            System.out.print(responseSupplier.getResponse(status, response));

            System.out.flush();
            System.err.println("Request ended");
        }

        System.err.println("FastCGI terminating.");
    }

    private String readBody() throws IOException {
        int bodyLength = 0;
        try {
            bodyLength = Integer.parseInt(FCGIInterface.request.params.getProperty("CONTENT_LENGTH"));
        } catch (NumberFormatException e) {
            System.err.println("Could not parse request content-length");
        }
        Reader reader = new InputStreamReader(System.in, StandardCharsets.UTF_8);
        char[] buffer = new char[bodyLength];
        int charRead = 0;
        while (charRead < bodyLength) {
            int n = reader.read(buffer, charRead, bodyLength - charRead);
            if (n < 0) break;
            charRead += n;
        }
        return new String(buffer);
    }
}
