package io.github.erdos.algo.fn;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MemoizeTest {

    @Test
    void testMemoize1() {
        List<String> result = new LinkedList<>();
        Consumer<String> consumer = result::add;

        Consumer<String> memoized = Memoize.memoize(consumer);

        memoized.accept("a");
        memoized.accept("b");
        memoized.accept("b");
        memoized.accept("c");

        assertEquals(asList("a", "b", "c"), result);
    }
}