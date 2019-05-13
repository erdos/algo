package io.github.erdos.algo.graph;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
}