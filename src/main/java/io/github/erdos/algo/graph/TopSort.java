package io.github.erdos.algo.graph;

import java.util.function.Function;
import java.util.stream.Stream;

public final class TopSort {

    public static <G> Stream<G> topSort(G root, Function<G, Iterable<G>> getParents) {
        // TODO: set fail on revisit to true
        return DepthFirstSearch.ofAllChildren(getParents).walk(root);
    }
}
