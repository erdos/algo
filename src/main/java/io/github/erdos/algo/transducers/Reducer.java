package io.github.erdos.algo.transducers;

@FunctionalInterface
public interface Reducer<A, X> {

    A reduce(A accumulator, X item);
}
