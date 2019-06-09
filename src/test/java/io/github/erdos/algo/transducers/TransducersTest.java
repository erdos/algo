package io.github.erdos.algo.transducers;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("WeakerAccess")
class TransducersTest {

    @Test
    public void testMap() {
        Transducer<?, Integer, Integer> tr = Transducers.map(x -> x + 1);

        List<Integer> result = Transducers.intoList(tr, Arrays.asList(1, 2, 3, 4));

        assertEquals(Arrays.asList(2, 3, 4, 5), result);
    }

    @Test
    public void testFilter() {
        Transducer<?, Integer, Integer> even = Transducers.filter(x -> x % 2 == 0);

        List<Integer> result = Transducers.intoList(even, Arrays.asList(1, 2, 3, 4));

        assertEquals(Arrays.asList(2, 4), result);
    }


    @Test
    public void testRemove() {
        Transducer<?, Integer, Integer> even = Transducers.remove(x -> x % 2 == 0);

        List<Integer> result = Transducers.intoList(even, Arrays.asList(1, 2, 3, 4));

        assertEquals(Arrays.asList(1, 3), result);
    }

    @Test
    public void testIdentity() {
        Transducer<?, Integer, Integer> iden = Transducers.identity();

        List<Integer> result = Transducers.intoList(iden, Arrays.asList(1, 2, 3, 4));

        assertEquals(Arrays.asList(1, 2, 3, 4), result);
    }
}