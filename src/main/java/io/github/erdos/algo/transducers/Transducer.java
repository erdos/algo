package io.github.erdos.algo.transducers;

@FunctionalInterface
public interface Transducer<A, X, Y> {

    Reducer<A, Y> transform(Reducer<A, X> reducer);

    // TODO: methods of different count of args!
    default <Z> Transducer<A, X, Z> comp(Transducer<A, Y, Z> transducer) {
        return rf -> transducer.transform(this.transform(rf));
    }
}
