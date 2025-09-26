package sadnex.web.fcgi;

import com.fastcgi.FCGIInterface;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import io.prometheus.metrics.config.EscapingScheme;
import io.prometheus.metrics.expositionformats.PrometheusTextFormatWriter;
import io.prometheus.metrics.model.snapshots.Unit;
import sadnex.web.data.Result;
import sadnex.web.exception.ValidationException;
import sadnex.web.metric.PrometheusMetrics;
import sadnex.web.util.JsonManager;
import sadnex.web.util.Validator;

import java.io.ByteArrayOutputStream;
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
    FCGIInterface fcgi;
    ResponseSupplier responseSupplier;
    PrometheusMetrics metrics;

    public RequestHandler() {
        this.fcgi = new FCGIInterface();
        this.responseSupplier = new ResponseSupplier();
        this.metrics = new PrometheusMetrics();
    }

    public void start() {
        System.err.println("FastCGI starting.");

        while (fcgi.FCGIaccept() >= 0) {
            metrics.getRequests().inc();

            String method = FCGIInterface.request.params.getProperty("REQUEST_METHOD");
            String uri = FCGIInterface.request.params.getProperty("REQUEST_URI");
            System.err.printf("Request method: %s URI: %s%n", method, uri);

            long startTime = System.nanoTime();
            HttpStatusCode status = HttpStatusCode.OK;
            Map<ResponseBodyKey, Object> responseMap = new HashMap<>();
            boolean sendResponse = true;

            try {
                if ("GET".equalsIgnoreCase(method) && "/metrics".equals(uri)) {
                    handleMetrics();
                    sendResponse = false;
                }
                else if ("POST".equalsIgnoreCase(method) && "/api".equals(uri)) {
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
                    } catch (ValidationException e) {
                        status = HttpStatusCode.BAD_REQUEST;
                        responseMap.put(ResponseBodyKey.RESULT, Result.ERROR);
                        responseMap.put(ResponseBodyKey.ERROR, e.getMessage());
                    } catch (JsonSyntaxException e) {
                        status = HttpStatusCode.BAD_REQUEST;
                        responseMap.put(ResponseBodyKey.RESULT, Result.ERROR);
                        responseMap.put(ResponseBodyKey.ERROR, "Invalid request format");
                    }
                }
                else {
                    status = HttpStatusCode.METHOD_NOT_ALLOWED;
                    responseMap.put(ResponseBodyKey.RESULT, Result.ERROR);
                    responseMap.put(ResponseBodyKey.ERROR, "Unsupported request method");
                }
            } catch (Exception e) {
                System.err.println("Unexpected error: " + e.getMessage());
                e.printStackTrace(System.err);
                status = HttpStatusCode.INTERNAL_SERVER_ERROR;
                responseMap.clear();
                responseMap.put(ResponseBodyKey.RESULT, Result.ERROR);
                responseMap.put(ResponseBodyKey.ERROR, "Internal server error");
            } finally {
                double executionTime = Unit.nanosToSeconds(System.nanoTime() - startTime);
                metrics.getRequestDuration().observe(executionTime);

                if (status.getCode() >= 400 && status.getCode() <= 499) {
                    metrics.getClientErrors().inc();
                }

                if (sendResponse) {
                    responseMap.put(ResponseBodyKey.EXECUTION_TIME, executionTime + " s");
                    responseMap.put(ResponseBodyKey.CURRENT_TIME, LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)));

                    String response = JsonManager.toJson(responseMap);
                    sendResponse(status, ContentType.JSON, response);
                }

                System.err.println("Request ended");
            }
        }

        System.err.println("FastCGI terminating.");
    }

    private String readBody() throws IOException {
        int bodyLength = 0;
        try {
            bodyLength = Integer.parseInt(FCGIInterface.request.params.getProperty("CONTENT_LENGTH"));
        } catch (NumberFormatException e) {
            System.err.println("Could not parse request content-length");
            return "";
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

    private void handleMetrics() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrometheusTextFormatWriter.builder().build().write(baos, metrics.getRegistry().scrape(), EscapingScheme.DEFAULT);

        sendResponse(HttpStatusCode.OK, ContentType.TEXT_PLAIN, baos.toString(StandardCharsets.UTF_8));
    }

    private void sendResponse(HttpStatusCode status, ContentType contentType, String body) {
        String response = responseSupplier.getResponse(status, contentType, body);
        System.out.print(response);
        System.out.flush();
    }
}
