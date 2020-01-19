package io.github.erdos.algo.metric;

@FunctionalInterface
public interface Metric<D, R extends Comparable<R>> {

    R distance(D firstItem, D secondItem);
}
