package io.github.erdos.algo.data;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings({"unused"})
public final class Tuple<L, R> {

    private final L left;
    private final R right;

    public Tuple(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft(){
        return left;
    }

    public R getRight() {
        return right;
    }

    public L getFirst() {
        return left;
    }

    public R getSecond() {
        return right;
    }

    public <X> Tuple<L, X> mapRight(Function<R, X> f) {
        return new Tuple<>(left, f.apply(right));
    }

    public <X> Tuple<X, R> mapLeft(Function<L, X> f) {
        return new Tuple<>(f.apply(left), right);
    }

    public <X> Tuple<X, R> mapToLeft(BiFunction<L, R, X> f) {
        return new Tuple<>(f.apply(left, right), right);
    }

    public <X> Tuple<L, X> mapToRight(BiFunction<L, R, X> f) {
        return new Tuple<>(left, f.apply(left, right));
    }

    public <X> Tuple<X, R> swapLeft(X newLeftValue) {
        return new Tuple<>(newLeftValue, right);
    }

    public <X> Tuple<L, X> swapRight(X newRightValue) {
        return new Tuple<>(left, newRightValue);
    }

    public <X, Y> Tuple<X, Y> mapToBoth(BiFunction<L, R, X> f1, BiFunction<L, R, Y> f2) {
        return new Tuple<>(f1.apply(left, right), f2.apply(left, right));
    }

    public static <L, R> Tuple<L, R> left(L left) {
        return new Tuple<>(left, null);
    }

    public static <L, R> Tuple<L, R> right(R right) {
        return new Tuple<>(null, right);
    }

    public <X, Y> Tuple<X, Y> map(Function<L, X> f1, Function<R, Y> f2) {
        return new Tuple<>(f1.apply(left), f2.apply(right));
    }

    public Tuple<R, L> flip() {
        return new Tuple<>(right, left);
    }

    public Map.Entry<L, R> toEntry() {
        return new AbstractMap.SimpleImmutableEntry<>(left, right);
    }

    @Override
    public String toString() {
        return "Tuple{" + left + ", " + right + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return Objects.equals(left, tuple.left) &&
                Objects.equals(right, tuple.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
