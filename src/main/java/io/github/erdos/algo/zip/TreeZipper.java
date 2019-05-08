package io.github.erdos.algo.zip;

import java.util.Iterator;
import java.util.Optional;

@SuppressWarnings("WeakerAccess")
public final class TreeZipper<N> {

    private final Cons<N> left;
    private final Cons<N> right;

    private final Cons<N> up;

    // current node, not null.
    private final N current;

    private final Factory<N> factory;

    private TreeZipper(N current, Cons<N> left, Cons<N> right, Cons<N> up, Factory<N> factory) {
        this.current = current;
        this.left = left;
        this.right = right;
        this.up = up;
        this.factory = factory;
    }

    public static <X> TreeZipper<X> zipper(X root, Factory<X> factory) {
        return new TreeZipper<>(root, null, null, null, factory);
    }

    public N node() {
        return current;
    }

    public Optional<TreeZipper<N>> left() {
        if (left == null) {
            return Optional.empty();
        } else {
            Cons<N> right2 = new Cons<>(current, this.right);
            TreeZipper<N> result = new TreeZipper<>(left.head, left.tail, right2, up, factory);
            return Optional.of(result);
        }
    }

    public TreeZipper<N> rightmost() {
        TreeZipper<N> right = this;
        Optional<TreeZipper<N>> r = right.right();
        while (r.isPresent()) {
            // empty
            right = r.get();
            r = right.right();
        }
        return right;
    }

    public TreeZipper<N> leftmost() {
        TreeZipper<N> left = this;
        Optional<TreeZipper<N>> r = left();
        while (r.isPresent()) {
            // empty
            left = r.get();
            r = left.left();
        }
        return left;
    }

    public Optional<TreeZipper<N>> right() {
        if (right == null) {
            return Optional.empty();
        } else {
            Cons<N> left2 = new Cons<>(current, left);
            TreeZipper<N> result = new TreeZipper<>(right.head, left2, right, up, factory);
            return Optional.of(result);
        }
    }

    public Iterable<N> lefts() {
        return () -> new Iterator<N>() {
            Cons<N> lefts = left;

            @Override
            public boolean hasNext() {
                return lefts != null;
            }

            @Override
            public N next() {
                try {
                    return lefts.head;
                } finally {
                    lefts = lefts.tail;
                }
            }
        };
    }

    public Iterable<N> rights() {
        return () -> new Iterator<N>() {
            Cons<N> rights = right;

            @Override
            public boolean hasNext() {
                return rights != null;
            }

            @Override
            public N next() {
                try {
                    return rights.head;
                } finally {
                    rights = rights.tail;
                }
            }
        };
    }

    public Optional<TreeZipper<N>> up() {
        if (up == null) {
            return Optional.empty();
        } else {
            throw new IllegalStateException("Not impled");
        }
    }

    public Optional<TreeZipper<N>> down() {
        Iterator<N> ch = factory.children(current).iterator();
        if (ch.hasNext()) {
            final N firstChild = ch.next();

            Cons<N> lefts = null;
            Cons<N> rights = null;
            return Optional.of(new TreeZipper<>(firstChild, lefts, rights, this.up, factory));
        } else {
            return Optional.empty();
        }
    }

    private final static class Cons<M> {
        final M head;
        final Cons<M> tail;

        private Cons(M head, Cons<M> tail) {
            this.head = head;
            this.tail = tail;
        }
    }

    interface Factory<T> {
        Iterable<T> children(T node);

        T factory(T prototype, Iterable<T> newChildren);
    }
}
