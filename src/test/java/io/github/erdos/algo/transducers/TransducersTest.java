package io.github.erdos.algo.transducers;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("WeakerAccess")
public class TransducersTest {

    @Test
    public void testMap() {
        Transducer<Integer, Integer> tr = Transducers.map(x -> x + 1);

        List<Integer> result = Transducers.intoList(tr, Arrays.asList(1, 2, 3, 4));

        assertEquals(Arrays.asList(2, 3, 4, 5), result);
    }

    @Test
    public void testTakeWhile() {
        Transducer<Integer, Integer> tr = Transducers.takeWhile(x -> x > 0);

        List<Integer> result = Transducers.intoList(tr, Arrays.asList(2, 1, -1, 2, -2, 4));

        assertEquals(Arrays.asList(2, 1), result);
    }

    @Test
    public void testComp() {
        Transducer<Integer, Integer> mf1 = Transducers.map(x -> x + 1);
        Transducer<Integer, Integer> mf2 = Transducers.map(x -> x * 2);

        Transducer<Integer, Integer> tr = mf1.comp(mf2);
        List<Integer> result = Transducers.intoList(tr, Arrays.asList(1, 2, 3, 4));

        assertEquals(Arrays.asList(3, 5, 7, 9), result);
    }

    @Test
    public void testFilter() {
        Transducer<Integer, Integer> even = Transducers.filter(x -> x % 2 == 0);

        List<Integer> result = Transducers.intoList(even, Arrays.asList(1, 2, 3, 4));

        assertEquals(Arrays.asList(2, 4), result);
    }


    @Test
    public void testRemove() {
        Transducer<Integer, Integer> even = Transducers.remove(x -> x % 2 == 0);

        List<Integer> result = Transducers.intoList(even, Arrays.asList(1, 2, 3, 4));

        assertEquals(Arrays.asList(1, 3), result);
    }

    @Test
    public void testIdentity() {
        Transducer<Integer, Integer> iden = Transducers.identity();

        List<Integer> result = Transducers.intoList(iden, Arrays.asList(1, 2, 3, 4));

        assertEquals(Arrays.asList(1, 2, 3, 4), result);
    }
}