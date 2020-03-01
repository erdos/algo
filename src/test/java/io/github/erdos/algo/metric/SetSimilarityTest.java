package io.github.erdos.algo.metric;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SetSimilarityTest {

    @Test
    public void testJaccard() {
        assertEquals(0.0, SetSimilarity.JACCARD.distance(set(1.0), set(1.0)));
        assertEquals(0.5, SetSimilarity.JACCARD.distance(set(1.0), set(1.0, 2.0)));
        assertEquals(1.0, SetSimilarity.JACCARD.distance(set(1.0), set(2.0)));
        assertEquals(1.0, SetSimilarity.JACCARD.distance(set(1.0), set(2.0, 3.0)));
    }

    @Test
    public void testDiceSorensen() {
        assertEquals(0.0, SetSimilarity.SORENSEN_DICE.distance(set(1.0), set(1.0)));
        assertEquals(0.33333, SetSimilarity.SORENSEN_DICE.distance(set(1.0), set(1.0, 2.0)), 0.01);
        assertEquals(1.0, SetSimilarity.SORENSEN_DICE.distance(set(1.0), set(2.0)));
        assertEquals(1.0, SetSimilarity.SORENSEN_DICE.distance(set(1.0), set(2.0, 3.0)));
    }


    private static Set<Double> set(Double... args) {
        return new HashSet<>(Arrays.asList(args));
    }
}