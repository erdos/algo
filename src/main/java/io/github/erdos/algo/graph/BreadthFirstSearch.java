package io.github.erdos.algo.graph;

import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class BreadthFirstSearch<N> {

    private Spliterator<N> spliterator(N root) {
        throw new IllegalStateException("Not implemented");
    }

    public Stream<N> walk(N root) {
        return StreamSupport.stream(spliterator(root), false);
    }
}
