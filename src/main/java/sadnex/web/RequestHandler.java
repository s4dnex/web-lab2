package sadnex.web;

import com.fastcgi.FCGIInterface;
import sadnex.web.data.Parameter;
import sadnex.web.data.Status;
import sadnex.web.exception.ValidationException;
import sadnex.web.util.JsonManager;
import sadnex.web.util.QueryProcessor;
import sadnex.web.util.Validator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler {
    private static final String RESPONSE = """
            Status: 200 OK
            Content-Type: application/json
            Content-Length: %d
            
            %s
            """;

    private static final String ERROR = """
            Status: 400 Bad Request
            Content-Type: application/json
            Content-Length: %d
            
            %s
            """;

    public static void main(String[] args) throws IOException {
        System.err.println("FastCGI starting.");
        int count = 0;
        FCGIInterface fcgi = new FCGIInterface();

        while (fcgi.FCGIaccept() >= 0) {
            count++;

            String method = FCGIInterface.request.params.getProperty("REQUEST_METHOD");
            String query = FCGIInterface.request.params.getProperty("QUERY_STRING");
            System.err.println("Request query: " + query);

            boolean isNotValid = false;
            Map<String, String> responseMap;
            long startTime = System.nanoTime();

            switch (method.toUpperCase()) {
                case "POST":
                    System.err.println("Post method");
                    try {
                        var params = QueryProcessor.queryToMap(query);
                        var point = Validator.validatePoint(params);

                        Status status;
                        if (Validator.checkHit(point)) {
                            status = Status.OK;
                        } else {
                            status = Status.MISS;
                        }

                        responseMap = new HashMap<>(point.toMap());
                        responseMap.put(Parameter.RESULT.toString(), status.toString());
                    } catch (ValidationException e) {
                        isNotValid = true;
                        responseMap = new HashMap<>();
                        responseMap.put(Parameter.RESULT.toString(), Status.ERROR.toString());
                        responseMap.put(Parameter.ERROR.toString(), e.getMessage());
                    }
                    break;

                default:
                    isNotValid = true;
                    responseMap = new HashMap<>();
                    responseMap.put(Parameter.RESULT.toString(), Status.ERROR.toString());
                    responseMap.put(Parameter.ERROR.toString(), "Unsupported request method");
                    break;
            }

            responseMap.put(Parameter.EXECUTION_TIME.toString(), ((System.nanoTime() - startTime) / Math.pow(10, 9)) + " s");
            responseMap.put(Parameter.CURRENT_TIME.toString(), LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)));
            String response = JsonManager.toJson(responseMap);
            if (isNotValid) {
                System.out.format(ERROR, response.getBytes(StandardCharsets.UTF_8).length, response);
                System.err.format(ERROR, response.getBytes(StandardCharsets.UTF_8).length, response);
            } else {
                System.out.format(RESPONSE, response.getBytes(StandardCharsets.UTF_8).length, response);
                System.err.format(RESPONSE, response.getBytes(StandardCharsets.UTF_8).length, response);
            }

            System.out.flush();
            System.err.println("Request ended");
        }

        System.err.println("FastCGI terminating.");
    }
}
