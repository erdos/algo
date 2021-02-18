package io.github.erdos.algo.graph;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

// TODO: tests for infinite graphs.
class DijkstraTest {

    @Test
    public void test1() {
        Dijkstra.Factory<Integer> rope = node -> {
            if (node < 10) {
                return Collections.singletonMap(node + 1, 1);
            } else {
                return Collections.emptyMap();
            }
        };

        List<Integer> result = new Dijkstra<>(rope).pathList(1, 4);
        assertEquals(asList(1, 2, 3, 4), result);
    }

    @Test
    public void testSmallRoute() {
        Dijkstra.Factory<Integer> rope = node -> {
            if (node < 10) {
                return map(node + 1, 2, node + 2, 3);
            } else {
                return Collections.emptyMap();
            }
        };

        List<Integer> result = new Dijkstra<>(rope).pathList(1, 5);
        assertEquals(asList(1, 3, 5), result);
    }

    private static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2) {
        Map<K, V> m = new HashMap<>();
        m.put(k1, v1);
        m.put(k2, v2);
        return m;
    }
}