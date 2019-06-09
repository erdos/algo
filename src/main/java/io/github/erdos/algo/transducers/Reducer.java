package io.github.erdos.algo.transducers;

import java.util.Collection;

@FunctionalInterface
public interface Reducer<A, X> {

    A reduce(A accumulator, X item);

    static <X, C extends Collection<X>> Reducer<C, X> intoCollection() {
        return (a, x) -> {
            a.add(x);
            return a;
        };
    }
}
