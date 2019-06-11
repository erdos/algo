package io.github.erdos.algo.transducers;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.stream.Stream;

public interface Reducible<E> {

    <A> A reduce(A accumulator, Reducer<A, E> reducer);

    static <X> Reducible<X> of(X[] input) {
        return new Reducible<X>() {
            @Override
            public <A> A reduce(A accumulator, Reducer<A, X> reducer) {
                for (X x : input) {
                    accumulator = reducer.reduce(accumulator, x);
                }
                return accumulator;
            }
        };
    }

    static <X> Reducible<X> of(Enumeration<X> input) {
        return new Reducible<X>() {
            @Override
            public <A> A reduce(A accumulator, Reducer<A, X> reducer) {
                while (input.hasMoreElements()) {
                    X x = input.nextElement();
                    accumulator = reducer.reduce(accumulator, x);
                }
                return accumulator;
            }
        };
    }

    static <X> Reducible<X> of(Stream<X> input) {
        return new Reducible<X>() {
            @Override
            public <A> A reduce(A accumulator, Reducer<A, X> reducer) {
                //noinspection unchecked
                final A[] acc = (A[]) new Object[]{accumulator};

                input.forEach(item -> acc[0] = reducer.reduce(acc[0], item));
                return acc[0];
            }
        };
    }

    // TODO: check that it can not iterate twice on same object!
    static <X> Reducible<X> of(Iterator<X> iter) {
        return new Reducible<X>() {
            @Override
            public <A> A reduce(A accumulator, Reducer<A, X> reducer) {
                A acc = accumulator;
                while (iter.hasNext()) {
                    X x = iter.next();
                    acc = reducer.reduce(acc, x);
                }
                return acc;
            }
        };
    }

    static <X> Reducible<X> of(Iterable<X> coll) {
        return new Reducible<X>() {
            @Override
            public <A> A reduce(A accumulator, Reducer<A, X> reducer) {
                Iterator<X> iter = coll.iterator();
                A acc = accumulator;
                while (iter.hasNext()) {
                    X x = iter.next();
                    acc = reducer.reduce(acc, x);
                }
                return acc;
            }
        };
    }
}
