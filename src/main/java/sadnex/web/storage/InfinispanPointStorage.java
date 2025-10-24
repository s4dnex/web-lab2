package sadnex.web.storage;

import org.infinispan.Cache;
import sadnex.web.model.Point;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InfinispanPointStorage implements PointStorage {
    @Resource(lookup = "java:jboss/infinispan/container/web-lab2/points")
    private Cache<String, List<Point>> cache;

    @Override
    public List<Point> getAll(String id) {
        List<Point> points = cache.get(id);
        return points != null ? new ArrayList<>(points) : Collections.emptyList();
    }

    @Override
    public void add(String id, Point point) {
        List<Point> points = cache.get(id);
        if (points == null) {
            points = new ArrayList<>();
        } else {
            points = new ArrayList<>(points);
        }
        points.add(point);
        cache.put(id, points);
    }
}