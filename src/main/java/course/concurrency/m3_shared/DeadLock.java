package course.concurrency.m3_shared;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DeadLock {

    private static final ConcurrentHashMap<String, String> map =
            new ConcurrentHashMap<>(Map.of("key1", "value1", "key2", "value2"));

    public static void main(String[] args) {
        Thread thread1 = new Thread(() ->
                map.compute("key1", (key, val) -> map.computeIfAbsent("key2", k -> "value2")));
        Thread thread2 = new Thread(() ->
                map.computeIfAbsent("key2", key -> map.compute("key1", (k, v) -> "value1")));

        thread1.start();
        thread2.start();
    }
}