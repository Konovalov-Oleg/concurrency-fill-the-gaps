package course.concurrency.m3_shared.collections;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class RestaurantService {

    private final Map<String, Restaurant> restaurantMap = new ConcurrentHashMap<>() {{
        put("A", new Restaurant("A"));
        put("B", new Restaurant("B"));
        put("C", new Restaurant("C"));
    }};

    private final Map<String, Integer> stat = new ConcurrentHashMap<>();

    public Restaurant getByName(String restaurantName) {
        addToStat(restaurantName);
        return restaurantMap.get(restaurantName);
    }

    public void addToStat(String restaurantName) {
        stat.compute(restaurantName, (k, v) -> v == null ? 1 : ++v);
    }

    public Set<String> printStat() {
        return stat
                .entrySet()
                .stream()
                .map(entry -> new StringBuilder()
                        .append(entry.getKey())
                        .append(" - ")
                        .append(entry.getValue())
                        .toString())
                .collect(Collectors.toSet());
    }
}