package sadnex.web.storage;

import sadnex.web.model.Point;

import java.util.List;

public interface PointStorage {
    List<Point> getAll(String id);

    void add(String id, Point point);
}
