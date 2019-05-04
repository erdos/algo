package io.github.erdos.algo.graph;

import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
class DepthFirstSearchTest {

    @Test
    void testEmptyWalk1() {
        final DepthFirstSearch<Object> dfs = DepthFirstSearch.ofChildren(__ -> emptyList());

        final List<Object> result = dfs.walk("root").collect(toList());
        assertEquals(singletonList("root"), result);

        final List<Object> result2 = dfs.walk("root2").collect(toList());
        assertEquals(singletonList("root2"), result2);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testWalkOrder() {
        final DepthFirstSearch<Object> dfs = DepthFirstSearch.ofChildren(x -> {
            if (x instanceof Iterable) return (Iterable) x;
            else return emptyList();
        });

        final List data = asList(1, 2, asList(3, asList(4)), 5);

        final List<Object> result = dfs.walk(data).collect(toList());
        assertEquals(asList(data, 1, 2, asList(3, asList(4)), 3, asList(4), 4, 5), result);
    }

    @Test
    void testWalkSkipVisited() {
        final DepthFirstSearch<Object> dfs = DepthFirstSearch.ofChildren(__ -> emptyList());

        final List<Object> result = dfs.walk("root").collect(toList());
        assertEquals(singletonList("root"), result);

        // will not visit already visited node again
        final List<Object> result2 = dfs.walk("root").collect(toList());
        assertTrue(result2.isEmpty());
    }
}