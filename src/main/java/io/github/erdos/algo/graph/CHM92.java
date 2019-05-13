package io.github.erdos.algo.graph;

import java.util.function.Function;

/**
 * Generating minimal perfect hash functions.
 * <p>
 * Sources:
 * <p>
 * [1] http://ilan.schnell-web.net/prog/perfect-hash/algo.html
 * [2] http://ilan.schnell-web.net/prog/perfect-hash/
 */
public class CHM92 {

    <T> Function<T, Integer> makeHashFn(Function<T, Integer> hash1, Function<T, Integer> hash2, T[] items) {

        int n = items.length;
        hash1 = x -> hash1.apply(x) % n;
        hash2 = x -> hash2.apply(x) % n;

        return null;
    }

    boolean isGraphAcyclic() {
        return false;
    }
}
