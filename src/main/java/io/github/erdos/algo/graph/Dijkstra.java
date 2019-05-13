package io.github.erdos.algo.graph;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class Dijkstra<N> {

    private final Factory<N> factory;

    public final static int QUEUE_INITIAL_CAPACITY = 16;

    public Dijkstra(Factory<N> factory) {
        this.factory = factory;
    }

    public Map<N, Integer> weightsMap(N source, N target) {
        final Map<N, Integer> allDistances = new HashMap<>();
        final PriorityQueue<N> queue = new PriorityQueue<>(QUEUE_INITIAL_CAPACITY, Comparator.comparing(allDistances::get));

        queue.add(source);

        while (!queue.isEmpty()) {
            final N node = queue.poll();
            final Integer nodeWeight = allDistances.get(node);

            assert nodeWeight != null;

            final Map<N, Integer> neighbors = factory.neighbors(node);

            for (Map.Entry<N, Integer> entry : neighbors.entrySet()) {
                N neighbor = entry.getKey();
                int neighborDistance = entry.getValue();
                int neighborWeight = nodeWeight + neighborDistance;
                if (allDistances.containsKey(neighbor)) {
                    Integer currentNeighborWeight = allDistances.get(neighbor);
                    if (currentNeighborWeight > neighborWeight) {
                        // ha talaltunk rovidebb utat, akkor frissitjuk az eddigit
                        allDistances.put(neighbor, neighborWeight);
                        queue.add(neighbor);
                        // ha talaltunk rovidebb utat
                    }
                } else {
                    allDistances.put(neighbor, neighborWeight);
                    queue.add(neighbor);
                }
            }
        }
        return allDistances;
    }

    // does not work!!
    private N nextStep(Map<N, Integer> weights, N source) {
        return factory.neighbors(source).entrySet().stream().min(Comparator.comparing(Map.Entry::getValue)).get().getKey();
    }

    public Iterator<N> path(N source, N target) {
        final Map<N, Integer> weights = weightsMap(source, target);

        return new Iterator<N>() {
            N current = source;
            @Override
            public boolean hasNext() {
                return current != target;
            }

            @Override
            public N next() {
                current = nextStep(weights, current);
                return current;
            }
        };
    }

    public Spliterator<N> pathSpliterator(N source, N target) {
        final Iterator<N> p = path(source, target);
        return Spliterators.spliterator(p, Long.MAX_VALUE, 0);
    }

    public Stream<N> pathStream(N source, N target) {
        final Spliterator<N> spliterator = pathSpliterator(source, target);
        return StreamSupport.stream(spliterator, false);
    }

    public interface Factory<N> {
        Map<N, Integer> neighbors(N node);
    }
}