package io.github.erdos.algo.fn;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface TriFunction<A,B,C,R> {
    R invoke(A firstArg, B secondArg, C thirdArg);

    default BiFunction<B, C, R> partial(A firstArg) {
        return (b, c) -> TriFunction.this.invoke(firstArg, b, c);
    }

    default Function<C, R> partial(A firstArg, B secondArg) {
        return c -> TriFunction.this.invoke(firstArg, secondArg, c);
    }

    default Supplier<R> partial(A a, B b, C c) {
        return () -> TriFunction.this.invoke(a, b, c);
    }
}
