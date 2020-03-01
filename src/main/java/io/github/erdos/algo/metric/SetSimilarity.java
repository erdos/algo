package io.github.erdos.algo.metric;

import java.util.Set;

public enum SetSimilarity implements Metric<Set<?>, Double> {
    JACCARD {
        @Override
        public Double distance(Set<?> first, Set<?> second) {
            if (first.isEmpty() && second.isEmpty()) return 1.0d;
            long isect = intersectionSize(first, second);
            return 1.0 - ((double) isect) / (first.size() + second.size() - isect);        }
    },

    SORENSEN_DICE {
        @Override
        public Double distance(Set<?> first, Set<?> second) {
            if (first.isEmpty() && second.isEmpty()) return 1.0d;
            return 1.0 - 2d * intersectionSize(first, second) / (first.size() + second.size());
        }
    };

    private static long intersectionSize(Set first, Set second) {
        if (first.size() > second.size()) {
            Set tmp = second;
            second = first;
            first = tmp;
        }

        return first.stream().filter(second::contains).count();
    }
}
