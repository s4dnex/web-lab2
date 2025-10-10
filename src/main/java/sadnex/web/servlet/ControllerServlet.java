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
import sadnex.web.storage.AppContextPointStorage;
import sadnex.web.storage.PointStorage;

import java.io.IOException;
import java.util.Map;

@WebServlet("/controller")
public class ControllerServlet extends HttpServlet {
    private PointStorage pointStorage;
    private RequestReader requestReader;
    private ResponseWriter responseWriter;

    @Override
    public void init() throws ServletException {
        super.init();
        pointStorage = new AppContextPointStorage(getServletContext());
        requestReader = new RequestReader();
        responseWriter = new ResponseWriter();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sessionId = request.getSession().getId();
        request.setAttribute("points", pointStorage.getAll(sessionId));
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
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

        request.getSession().setAttribute("pointMap", json);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/check");
        dispatcher.forward(request, response);
    }
}