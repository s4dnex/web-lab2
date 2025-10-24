package sadnex.web.servlet;

import sadnex.web.model.Point;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/result")
public class ResultServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Point current = (Point) request.getSession().getAttribute("currentPoint");

        request.setAttribute("currentPoint", current);

        request.getRequestDispatcher("/result.jsp").forward(request, response);
    }
}