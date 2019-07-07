package io.github.erdos.algo.psq;

import java.util.Map;

/**
 * https://www.cs.ox.ac.uk/ralf.hinze/publications/ICFP01.pdf
 */
public class PrioritySearchQueue<T> {

    private final T winner;
    private final LNode<T> tree;
    private final Integer maxKey;

    private PrioritySearchQueue(T winner, LNode<T> tree, Integer maxKey) {
        this.winner = winner;
        this.tree = tree;
        this.maxKey = maxKey;
    }

    public static <T> PrioritySearchQueue<T> build(T value) {
        return new PrioritySearchQueue<>(value, null, key(value));
    }

    static <T> int key(T value) {
       return value.hashCode();
    }

    static <T> int priority(T value) {
        return value.hashCode();
    }

    public static <T> PrioritySearchQueue<T> up(PrioritySearchQueue<T> a, PrioritySearchQueue<T> b) {
        if (a == null) {
            return b;
        } else if (b == null) {
            return a;
        } else if (priority(a.winner) <= priority(b.winner)) {
            return new PrioritySearchQueue<>(a.winner, new LNode<>(b.winner, a.tree, a.maxKey, b.tree), b.maxKey);
        } else {
            return new PrioritySearchQueue<>(b.winner, new LNode<>(a.winner, a.tree, a.maxKey, b.tree), b.maxKey);
        }
    }

    public static <T> PrioritySearchQueue<T> fromMap(Map<T, Long> map) {
        // TODO: build
        return null;
    }

    public T peek() {
        return winner;
    }

    public PrioritySearchQueue<T> pop() {
        return tree.secondBest(maxKey);
    }

    public PrioritySearchQueue<T> delMin() {
        return null;
    }

    private static final class LNode<T> {
        private final T loser;
        private final Integer splitKey;
        private final LNode<T> left;
        private final LNode<T> right;

        LNode(T loser, LNode<T> left, Integer splitKey, LNode<T> right) {
            this.loser = loser;
            this.splitKey = splitKey;
            this.left = left;
            this.right = right;
        }

        private PrioritySearchQueue<T> secondBest(int m) {
            if (key(loser) < splitKey) {
                PrioritySearchQueue<T> aa = new PrioritySearchQueue<>(loser, left, splitKey);
                if (right == null) {
                    return aa;
                } else {
                    return up(aa, right.secondBest(m));
                }
            } else {
                PrioritySearchQueue<T> bb = new PrioritySearchQueue<>(loser, right, m);
                if (left == null) {
                    return bb;
                } else {
                    return up(left.secondBest(splitKey), bb);
                }
            }
        }

    }

    public Integer getMaxKey() {
        return maxKey;
    }
}
