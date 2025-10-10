package sadnex.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sadnex.web.model.Point;

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