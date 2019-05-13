package io.github.erdos.algo.graph;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class Dijkstra<N> {

    private final Factory<N> factory;

    public final static int QUEUE_INITIAL_CAPACITY = 16;

    public Dijkstra(Factory<N> factory) {
        this.factory = factory;
    }

    public DijkstraResult<N> weights(N source) {
        final Map<N, Integer> allDistances = new HashMap<>();
        final Map<N, N> previous = new HashMap<>();
        final PriorityQueue<N> queue = new PriorityQueue<>(QUEUE_INITIAL_CAPACITY, Comparator.comparing(allDistances::get));

        queue.add(source);
        allDistances.put(source, 0);

        while (!queue.isEmpty()) {
            final N node = queue.poll();
            System.out.println("Node is: " + node);
            // TODO: ez tul nagy, nem szep.
            final Integer nodeWeight = allDistances.get(node);

            final Map<N, Integer> neighbors = factory.neighbors(node);

            for (Map.Entry<N, Integer> entry : neighbors.entrySet()) {
                N neighbor = entry.getKey();
                int neighborDistance = entry.getValue();
                assert neighborDistance > 0;

                int neighborWeight = nodeWeight == null ? neighborDistance : nodeWeight + neighborDistance;
                if (allDistances.containsKey(neighbor)) {
                    Integer currentNeighborWeight = allDistances.get(neighbor);
                    if (currentNeighborWeight > neighborWeight) {
                        // ha talaltunk rovidebb utat, akkor frissitjuk az eddigit
                        allDistances.put(neighbor, neighborWeight);
                        previous.put(neighbor, node);
                        queue.add(neighbor);
                        // ha talaltunk rovidebb utat
                    }
                } else {
                    allDistances.put(neighbor, neighborWeight);
                    previous.put(neighbor, node);
                    queue.add(neighbor);
                }
            }
        }
        return new DijkstraResult<>(allDistances, previous);
    }

    // does not work!!
    private N nextStep(Map<N, Integer> weights, N source) {
        return factory.neighbors(source).entrySet().stream().min(Comparator.comparing(Map.Entry::getValue)).get().getKey();
    }

    public Iterator<N> path(N source, N target) {
        final DijkstraResult<N> result = weights(source);
        final Stack<N> path = new Stack<>();

        N current = target;
        path.add(target);
        while (current != source) {
            current = result.previous.get(current);
            path.add(current);
        }
        return path.iterator();
    }

    public List<N> pathList(N source, N target) {
        return pathStream(source, target).collect(Collectors.toList());
    }


    final static class DijkstraResult<N> {
        final Map<N, Integer> weights;
        final Map<N, N> previous;

        DijkstraResult(Map<N, Integer> weights, Map<N, N> previous) {
            this.weights = weights;
            this.previous = previous;
        }
    }

    public Spliterator<N> pathSpliterator(N source, N target) {
        final Iterator<N> p = path(source, target);
        return Spliterators.spliterator(p, Long.MAX_VALUE, 0);
    }

    public Stream<N> pathStream(N source, N target) {
        final Spliterator<N> spliterator = pathSpliterator(source, target);
        return StreamSupport.stream(spliterator, false);
    }

    @FunctionalInterface
    public interface Factory<N> {
        Map<N, Integer> neighbors(N node);
    }
}