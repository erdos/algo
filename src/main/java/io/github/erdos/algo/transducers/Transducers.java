package io.github.erdos.algo.transducers;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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

    public static <X, Y> Transducer<X, Y> concatMap(Function<Y, Reducible<X>> fn) {
        return new Transducer<X, Y>() {
            @Override
            public <A> Reducer<A, Y> transform(Reducer<A, X> rf) {
                return (accumulator, item) -> fn.apply(item).reduce(accumulator, rf);
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

    public static <S, T> Stream<T> intoStream(Transducer<T, S> tr, Collection<S> source) {
        return StreamSupport.stream(intoSpliterator(tr, source), false);
    }


    public static <S, T> Spliterator<T> intoSpliterator(Transducer<T, S> tr, Collection<S> source) {
        Iterator<S> so = source.iterator();
        throw new RuntimeException("Not implemented!");
        /*
        return new Spliterator<T>() {
            @Override
            public boolean tryAdvance(Consumer<? super T> consumer) {
                if (so.hasNext()) {
                    tr.transform(Reducer.intoConsumer())
                            .reduce(consumer, so.next());
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public Spliterator<T> trySplit() {
                return null;
            }

            @Override
            public long estimateSize() {
                return Long.MAX_VALUE;
            }

            @Override
            public int characteristics() {
                return 0;
            }
        };
        */
    }
}
