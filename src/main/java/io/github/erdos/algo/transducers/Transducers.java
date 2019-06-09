package io.github.erdos.algo.transducers;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("WeakerAccess")
public final class Transducers {

    public static <A, X, Y> Transducer<A, X, Y> map(Function<Y, X> fn) {
        return rf -> (Reducer<A, Y>) (accumulator, item) -> rf.reduce(accumulator, fn.apply(item));
    }

    public static <A, X> Transducer<A, X, X> filter(Predicate<X> pred) {
        return rf -> (a, x) -> {
            if (pred.test(x)) {
                return rf.reduce(a, x);
            } else {
                return a;
            }
        };
    }

    public static <A, X> Transducer<A, X, X> remove(Predicate<X> pred) {
        return filter(pred.negate());
    }

    public static <A, X> Transducer<A, X, X> identity() {
        return rf -> rf;
    }

    public static <T, C extends Collection<T>, S, X> C into(C target, Transducer<X, T, S> tr, Collection<S> source) {
        Reducer<X, T> reducer0 = (a, e) -> {
            target.add(e);
            return a;
        };

        Reducer<?, S> reducer = tr.transform(reducer0);

        source.forEach(item -> {
            reducer.reduce(null, item);
        });

        return target;
    }

    public static <X, S, T> Set<T> intoSet(Transducer<X, T, S> tr, Collection<S> source) {
        return into(new HashSet<>(), tr, source);
    }

    public static <X, S, T> List<T> intoList(Transducer<X, T, S> tr, Collection<S> source) {
        return into(new ArrayList<>(), tr, source);
    }
}
