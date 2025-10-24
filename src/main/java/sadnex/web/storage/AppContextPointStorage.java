package sadnex.web.storage;

import sadnex.web.model.Point;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppContextPointStorage implements PointStorage {
    private final ServletContext servletContext;

    public AppContextPointStorage(ServletContext servletContext) {
        if (servletContext == null) {
            throw new NullPointerException("Servlet context is null");
        }

        this.servletContext = servletContext;
    }

    @Override
    public List<Point> getAll(String id) {
        Object attr = servletContext.getAttribute(id);
        if (attr == null) {
            return Collections.emptyList();
        }

        List<Point> points = (List<Point>) attr;
        return points;
    }

    @Override
    public void add(String id, Point point) {
        synchronized (servletContext) {
            Object attr = servletContext.getAttribute(id);
            if (attr == null) {
                List<Point> list = new ArrayList<>();
                list.add(point);
                servletContext.setAttribute(id, list);
                return;
            }

            List<Point> points = (List<Point>) attr;

            List<Point> newList = new ArrayList<>(points);
            newList.add(point);
            servletContext.setAttribute(id, newList);
        }
    }
}
