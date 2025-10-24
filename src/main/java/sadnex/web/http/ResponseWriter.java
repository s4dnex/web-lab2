package sadnex.web.http;

import sadnex.web.model.Result;
import sadnex.web.util.JsonManager;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResponseWriter {
    private final JsonManager jsonManager = new JsonManager();

    public void sendJsonError(HttpServletResponse httpServletResponse, HttpStatusCode code, String cause) throws IOException {
        httpServletResponse.setContentType(ContentType.JSON.toString());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setStatus(code.getCode());

        Map<String, Object> map = new HashMap<>();
        map.put(BodyKey.RESULT.toString(), Result.ERROR.toString());
        map.put(BodyKey.ERROR.toString(), cause);
        String json = jsonManager.toJson(map);
        httpServletResponse.getWriter().write(json);
    }

    public void sendJsonResponse(HttpServletResponse httpServletResponse, HttpStatusCode code, Map<String, Object> jsonMap) throws IOException {
        httpServletResponse.setContentType(ContentType.JSON.toString());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setStatus(code.getCode());

        String json = jsonManager.toJson(jsonMap);
        httpServletResponse.getWriter().write(json);
    }
}
