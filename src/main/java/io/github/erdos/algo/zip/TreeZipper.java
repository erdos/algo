package io.github.erdos.algo.zip;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Stack;

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

    /**
     * Returns the zipper on the closest left sibling if any.
     */
    public Optional<TreeZipper<N>> left() {
        if (left == null) {
            return Optional.empty();
        } else {
            Cons<N> right2 = new Cons<>(current, this.right);
            TreeZipper<N> result = new TreeZipper<>(left.head, left.tail, right2, up, factory);
            return Optional.of(result);
        }
    }

    /**
     * Returns the zipper on the rightmost sibling. Returns this when there are not right siblings.
     */
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

    /**
     * Returns the zipper on the leftmost sibling. Returns this when there are no left siblings.
     */
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

    /**
     * Returns the zipper on the closest right sibling if any.
     */
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
            final N parent = up.head;
            if (up.tail == null) {
                return Optional.of(new TreeZipper<>(parent, null, null, null, factory));
            } else {
                final N grandparent = up.tail.head;
                final Iterable<N> aunts = factory.children(grandparent);

                final Cons<N> rights2 = Cons.fromIterable(aunts);
                return Optional.of(new TreeZipper<>(parent, null, rights2, up.tail, factory));
            }
        }
    }
    
    public Optional<TreeZipper<N>> down() {
        Iterator<N> ch = factory.children(current).iterator();
        if (ch.hasNext()) {

            final N firstChild = ch.next();

            Cons<N> lefts = null; // TODO: impl

            Cons<N> rights = Cons.fromIterator(ch);

            return Optional.of(new TreeZipper<>(firstChild, lefts, rights, new Cons<>(current, this.up), factory));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreeZipper<?> that = (TreeZipper<?>) o;
        return Objects.equals(current, that.current);
    }

    @Override
    public int hashCode() {
        return Objects.hash(current);
    }

    private final static class Cons<M> {
        final M head;
        final Cons<M> tail;

        private Cons(M head, Cons<M> tail) {
            this.head = head;
            this.tail = tail;
        }

        // yeah so this could be better.
        static <M> Cons<M> fromIterable(Iterable<M> source) {
            return fromIterator(source.iterator());
        }

        static <M> Cons<M> fromIterator(Iterator<M> source) {
            final Stack<M> stack = new Stack<>();

            while (source.hasNext()) {
                stack.push(source.next());
            }

            Cons<M> tail = null;
            for (M m : stack) {
                tail = new Cons<>(m, tail);
            }
            return tail;
        }

    }

    interface Factory<T> {
        Iterable<T> children(T node);

        T factory(T prototype, Iterable<T> newChildren);
    }
}
