package io.github.erdos.algo.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DummyBloomFilterTest {

    @Test
    public void testEmpty() {
        DummyBloomFilter<Long> bf = new DummyBloomFilter<>(x -> x);
        assertFalse(bf.contains(0L));
        assertFalse(bf.contains(1L));
        assertFalse(bf.contains(2L));
        assertFalse(bf.contains(3L));
    }

    @Test
    public void testContains1() {
        DummyBloomFilter<Long> bf = new DummyBloomFilter<>(x -> x);
        bf.add(1L);
        assertFalse(bf.contains(0L));
        assertTrue(bf.contains(1L));
        assertFalse(bf.contains(2L));
        assertFalse(bf.contains(3L));
    }
}