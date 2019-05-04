package io.github.erdos.algo.fn;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Memoize<V> {

    static <T, U, R> BiFunction<T, U, R> memoize(BiFunction<T, U, R> fn) {
        return memoize(fn, simpleMap());
    }

    static <A, B> Function<A, B> memoize(Function<A, B> fn) {
        return memoize(fn, simpleMap());
    }

    static <A, B> Function<A, B> memoize(Function<A, B> fn, Memoize<B> memo) {
        return (k) -> memo.computeIfAbsent(k, () -> fn.apply(k));
    }

    static <V> Memoize<V> simpleMap() {
        final Map<Object, V> cache = new HashMap<>();
        return (k, v) -> cache.computeIfAbsent(k, __ -> v.get());
    }

    static <T, U, R> BiFunction<T, U, R> memoize(BiFunction<T, U, R> fn, Memoize<R> memo) {
        return (t, u) -> {
            final AbstractMap.SimpleImmutableEntry key = new AbstractMap.SimpleImmutableEntry<>(t, u);
            return (R) memo.computeIfAbsent(key, () -> fn.apply(t, u));
        };
    }

    V computeIfAbsent(Object key, Supplier<V> callable);
}
