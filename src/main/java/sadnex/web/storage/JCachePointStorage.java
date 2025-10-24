package sadnex.web.storage;

import sadnex.web.model.Point;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.naming.InitialContext;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class JCachePointStorage implements PointStorage {
    private final Cache<String, List<Point>> cache;

    public JCachePointStorage() {
        try {
            CacheManager cacheManager;

            try {
                InitialContext context = new InitialContext();
                cacheManager = (CacheManager) context.lookup("java:jboss/infinispan/container/web-lab2");
                System.out.println("Using Infinispan CacheManager from JNDI");
            } catch (Exception e) {
                System.out.println("JNDI lookup failed, using default caching provider: " + e.getMessage());

                cacheManager = Caching.getCachingProvider().getCacheManager();
            }

            Cache<String, List<Point>> existingCache = cacheManager.getCache("points", String.class, (Class<List<Point>>) (Class<?>) List.class);

            if (existingCache != null) {
                this.cache = existingCache;
                System.out.println("Using existing points cache");
            } else {
                MutableConfiguration<String, List<Point>> configuration =
                        new MutableConfiguration<String, List<Point>>()
                                .setTypes(String.class, (Class<List<Point>>) (Class<?>) List.class)
                                .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.MINUTES, 5)))
                                .setStoreByValue(false)
                                .setManagementEnabled(true)
                                .setStatisticsEnabled(true);

                this.cache = cacheManager.createCache("points", configuration);
                System.out.println("Created new pointsCache with TTL 30 minutes");
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize JCache: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Point> getAll(String id) {
        return cache.containsKey(id) ? cache.get(id) : new ArrayList<>();
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

