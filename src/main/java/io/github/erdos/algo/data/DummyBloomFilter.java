package io.github.erdos.algo.data;

import java.util.function.Function;

public final class DummyBloomFilter<T> {
    private long contents = 0;
    private final Function<T, Long> hashFunction;

    public DummyBloomFilter(Function<T, Long> hashFunction) {
        this.hashFunction = hashFunction;
    }

    public void add(T elem) {
        long hash = hashFunction.apply(elem);
        if (hash == 0) hash = Long.MAX_VALUE;
        contents = contents | hash;
    }

    public boolean contains(T elem) {
        long hash = hashFunction.apply(elem);
        if (hash == 0) hash = Long.MAX_VALUE;
        return (contents & hash) == hash;
    }
}
