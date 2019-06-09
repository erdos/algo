package io.github.erdos.algo.transducers;

import java.util.Collection;
import java.util.function.Consumer;

@FunctionalInterface
public interface Reducer<A, X> {

    A reduce(A accumulator, X item);

    static <X, C extends Collection<X>> Reducer<C, X> intoCollection() {
        return (a, x) -> {
            a.add(x);
            return a;
        };
    }

    static <X> Reducer<? extends Consumer<X>, X> intoConsumer() {
        return (consumer, x) -> {
            consumer.accept(x);
            return consumer;
        };
    }
}
