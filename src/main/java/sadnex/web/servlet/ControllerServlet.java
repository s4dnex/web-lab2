package sadnex.web.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sadnex.web.http.BodyKey;
import sadnex.web.http.ContentType;
import sadnex.web.http.HttpStatusCode;
import sadnex.web.http.RequestReader;
import sadnex.web.http.ResponseWriter;

import java.io.IOException;
import java.util.Map;

@WebServlet("/controller")
public class ControllerServlet extends HttpServlet {
    private RequestReader requestReader;
    private ResponseWriter responseWriter;

    @Override
    public void init() throws ServletException {
        super.init();
        requestReader = new RequestReader();
        responseWriter = new ResponseWriter();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getContentType() == null || !request.getContentType().equalsIgnoreCase(ContentType.JSON.toString())) {
            responseWriter.sendJsonError(response, HttpStatusCode.BAD_REQUEST, "Unsupported content type");
            return;
        }

        int contentLength = request.getContentLength();
        if (contentLength <= 0) {
            responseWriter.sendJsonError(response, HttpStatusCode.BAD_REQUEST, "Could not read content length");
            return;
        }

        Map<String, Object> json = requestReader.readJsonBody(request);

        if (json == null) {
            responseWriter.sendJsonError(response, HttpStatusCode.BAD_REQUEST, "Could not parse request body");
            return;
        }

        if (!json.containsKey(BodyKey.X.toString()) || !json.containsKey(BodyKey.Y.toString()) || !json.containsKey(BodyKey.R.toString())) {
            responseWriter.sendJsonError(response, HttpStatusCode.BAD_REQUEST, "Missing point's coordinates in body");
            return;
        }

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/check");
        dispatcher.forward(request, response);
    }
}