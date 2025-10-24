package sadnex.web.servlet;

import sadnex.web.exception.ValidationException;
import sadnex.web.http.BodyKey;
import sadnex.web.http.HttpStatusCode;
import sadnex.web.http.ResponseWriter;
import sadnex.web.model.Point;
import sadnex.web.storage.InfinispanPointStorage;
import sadnex.web.storage.PointStorage;
import sadnex.web.util.Validator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        pointStorage = new InfinispanPointStorage();
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