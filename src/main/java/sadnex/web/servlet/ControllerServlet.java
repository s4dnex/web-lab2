package sadnex.web.servlet;

import com.google.gson.reflect.TypeToken;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sadnex.web.http.BodyKey;
import sadnex.web.http.ContentType;
import sadnex.web.util.JsonManager;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

@WebServlet("/controller")
public class ControllerServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getContentType() == null || !request.getContentType().equalsIgnoreCase(ContentType.JSON.toString())) {
            return;
        }

        int contentLength = request.getContentLength();
        if (contentLength <= 0) {
            return;
        }

        var buffer = request.getInputStream().readAllBytes();
        String requestBody = new String(buffer);

        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> json = JsonManager.fromJson(requestBody, type);
        if (json == null) return;

        if (!json.containsKey(BodyKey.X.toString()) || !json.containsKey(BodyKey.Y.toString()) || !json.containsKey(BodyKey.R.toString())) {
            return;
        }

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/area/check");
        dispatcher.forward(request, response);
    }
}