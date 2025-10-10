package sadnex.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sadnex.web.exception.ValidationException;
import sadnex.web.http.BodyKey;
import sadnex.web.http.HttpStatusCode;
import sadnex.web.http.ResponseWriter;
import sadnex.web.model.Point;
import sadnex.web.storage.AppContextPointStorage;
import sadnex.web.storage.PointStorage;
import sadnex.web.util.Validator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/check")
public class AreaCheckServlet extends HttpServlet {
    private PointStorage pointStorage;
    private ResponseWriter responseWriter;
    private Validator validator;

    @Override
    public void init() throws ServletException {
        super.init();
        pointStorage = new AppContextPointStorage(getServletContext());
        responseWriter = new ResponseWriter();
        validator = new Validator();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<BodyKey, Object> responseMap = new HashMap<>();

        Map<String, Object> json = (Map<String, Object>) request.getSession().getAttribute("pointMap");

        Point point;
        try {
            point = validator.validatePoint(json);
            pointStorage.add(request.getSession().getId(), point);
            responseMap.putAll(point.toMap());
        } catch (ValidationException e) {
            responseWriter.sendJsonError(response, HttpStatusCode.BAD_REQUEST, e.getMessage());
            return;
        }

        request.getSession().setAttribute("currentPoint", point);
        response.setStatus(HttpStatusCode.FOUND.getCode());
        String redirectUrl = request.getContextPath() + "/result";
        response.sendRedirect(redirectUrl);
    }
}