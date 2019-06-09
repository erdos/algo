package io.github.erdos.algo.transducers;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("WeakerAccess")
public final class Transducers {

    public static <X, Y> Transducer<X, Y> map(Function<Y, X> fn) {

        return new Transducer<X, Y>() {
            @Override
            public <A> Reducer<A, Y> transform(Reducer<A, X> rf) {
                return (accumulator, item) -> rf.reduce(accumulator, fn.apply(item));
            }
        };
    }

    public static <X> Transducer<X, X> filter(Predicate<X> pred) {
        return new Transducer<X, X>() {
            @Override
            public <A> Reducer<A, X> transform(Reducer<A, X> rf) {
                return (accumulator, item) -> {
                    if (pred.test(item)) {
                        return rf.reduce(accumulator, item);
                    } else {
                        return accumulator;
                    }
                };
            }
        };
    }

    public static <X> Transducer<X, X> remove(Predicate<X> pred) {
        return filter(pred.negate());
    }

    public static <X> Transducer<X, X> identity() {
        return new Transducer<X, X>() {
            @Override
            public <A> Reducer<A, X> transform(Reducer<A, X> reducer) {
                return reducer;
            }
        };
    }

    public static <T, C extends Collection<T>, S> C into(C target, Transducer<T, S> tr, Reducible<S> source) {
        return source.reduce(target, tr.transform(Reducer.intoCollection()));
    }

    public static <T, C extends Collection<T>, S> C into(C target, Transducer<T, S> tr, Collection<S> source) {
        return into(target, tr, Reducible.of(source));
    }

    public static <S, T> Set<T> intoSet(Transducer<T, S> tr, Collection<S> source) {
        return into(new HashSet<>(), tr, source);
    }

    public static <S, T> List<T> intoList(Transducer<T, S> tr, Collection<S> source) {
        return into(new ArrayList<>(), tr, source);
    }
}
