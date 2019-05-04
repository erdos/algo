package io.github.erdos.algo.graph;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class DepthFirstSearch<N> {

    private final Function<N, Iterable<N>> children;
    private final Set<N> visited;
    private final boolean failOnRevisit;

    private final WalkOrder order = WalkOrder.INORDER;

    public static <N> DepthFirstSearch<N> ofChildren(Function<N, Iterable<N>> children) {
        return new DepthFirstSearch<>(children, true, false);
    }

    public static <N> DepthFirstSearch<N> ofAllChildren(Function<N, Iterable<N>> children) {
        return new DepthFirstSearch<>(children, false, false);
    }

    private DepthFirstSearch(Function<N, Iterable<N>> children, boolean distinct, boolean failOnRevisit) {
        this.children = children;
        visited = distinct ? new HashSet<>() : null;
        this.failOnRevisit = failOnRevisit;
    }

    private Spliterator<N> spliterator(N root) {
        if (visited == null) {
            // no nothing
        } else if (visited.contains(root)) {
            if (failOnRevisit) {
                throw new IllegalStateException("Node has already been visited!");
            } else {
                return Spliterators.emptySpliterator();
            }
        } else {
            visited.add(root);
        }

        final Stack<N> stack = new Stack<>();
        stack.add(root);

        return new Spliterator<N>() {
            public boolean tryAdvance(Consumer<? super N> consumer) {
                if (!stack.isEmpty()) {
                    final N top = stack.pop();
                    consumer.accept(top);

                    final LinkedList<N> reverse = new LinkedList<>();

                    for (N child : children.apply(top)) {

                        if (visited != null && visited.contains(child)) {
                            if (failOnRevisit) {
                                throw new IllegalStateException("");
                            }
                        } else {
                            if (visited != null) {
                                visited.add(child);
                            }
                            reverse.addFirst(child);
                        }
                    }
                    stack.addAll(reverse);
                    return true;
                } else {
                    return false;
                }
            }

            public Spliterator<N> trySplit() {
                // not supported
                return null;
            }

            public long estimateSize() {
                // not supported
                return Long.MAX_VALUE;
            }

            public int characteristics() {
                // also: immutable (?)
                return Spliterator.NONNULL + ((visited != null) ? DISTINCT : 0);
            }
        };
    }

    public Stream<N> walk(N root) {
        return StreamSupport.stream(spliterator(root), false);
    }
}
