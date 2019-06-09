package io.github.erdos.algo.transducers;

@FunctionalInterface
public interface Transducer<X, Y> {

    <A> Reducer<A, Y> transform(Reducer<A, X> reducer);

    // TODO: methods of different count of args!
    default <Z> Transducer<X, Z> comp(Transducer<Y, Z> transducer) {
        final Transducer<X, Y> self = this;
        return new Transducer<X, Z>() {
            @Override
            public <A> Reducer<A, Z> transform(Reducer<A, X> rf) {
                return transducer.transform(self.transform(rf));
            }
        };
    }
}
