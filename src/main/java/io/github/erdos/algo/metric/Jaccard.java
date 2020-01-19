package io.github.erdos.algo.metric;

import java.util.Set;

public class Jaccard<T> implements Metric<Set<T>, Double> {

    @Override
    public Double distance(Set<T> first, Set<T> second) {
        if (first.isEmpty() && second.isEmpty()) return 1.0d;
        long isect = intersectionSize(first, second);
        return ((double) isect) / (first.size() + second.size() - isect);
    }

    private long intersectionSize(Set<T> first, Set<T> second) {
        if (first.size() > second.size()) {
            Set<T> tmp = second;
            second = first;
            first = tmp;
        }

        return first.stream().filter(second::contains).count();
    }
}
