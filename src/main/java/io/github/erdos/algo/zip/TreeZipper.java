package io.github.erdos.algo.zip;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

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

        assert current != null;
        assert left == null || left.head != current;
        assert right == null || right.head != current;
        assert up == null || up.head != current;
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

        for (Optional<TreeZipper<N>> rr = right.right(); rr.isPresent(); rr = right.right()) {
            right = rr.get();
        }
        return right;
    }

    /**
     * Returns the zipper on the leftmost sibling. Returns this when there are no left siblings.
     */
    public TreeZipper<N> leftmost() {
        TreeZipper<N> left = this;

        for (Optional<TreeZipper<N>> rr = left.left(); rr.isPresent(); rr = left.left()) {
            left = rr.get();
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
            TreeZipper<N> result = new TreeZipper<>(right.head, left2, right.tail, up, factory);
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

            final LinkedList<N> newChildren = new LinkedList<>();

            for (Cons<N> item = left; item != null; item = item.tail) {
                newChildren.addFirst(item.head);
            }
            newChildren.addLast(current);
            for (Cons<N> item = right; item != null; item = item.tail) {
                newChildren.addLast(item.head);
            }

            final N parent = factory.factory(up.head, newChildren);

            // TODO: parent shall be modified so it contains current node and others.

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

    public Iterable<N> ups() {
        return () -> new Iterator<N>() {
            Cons<N> ups = up;

            @Override
            public boolean hasNext() {
                return ups != null;
            }

            @Override
            public N next() {
                try {
                    return ups.head;
                } finally {
                    ups = ups.tail;
                }
            }
        };
    }

    /**
     * Returns the zipper for the root of the tree.
     */
    public TreeZipper<N> upmost() {
        TreeZipper<N> left = this;
        Optional<TreeZipper<N>> r = up();
        while (r.isPresent()) {
            left = r.get();
            r = left.up();
        }
        return left;
    }

    /**
     * Returns the content of the root node.
     */
    public N root() {
        return upmost().node();
    }

    /**
     * Returns the zipper for the first child node of the current node if any.
     */
    public Optional<TreeZipper<N>> down() {
        Iterator<N> ch = factory.children(current).iterator();
        if (ch.hasNext()) {

            final N firstChild = ch.next();

            Cons<N> rights = Cons.fromIterator(ch);

            return Optional.of(new TreeZipper<>(firstChild, null, rights, new Cons<>(current, this.up), factory));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Edits the content of the current node.
     */
    public TreeZipper<N> edit(Function<N, N> edit) {
        final N edited = edit.apply(this.current);
        return new TreeZipper<>(edited, left, right, up, factory);
    }

    /**
     * Replaces the content of the node in the current tree.
     */
    public TreeZipper<N> replace(N newContent) {
        return edit(__ -> newContent);
    }

    /**
     * Remoces current node from the tree and moves to the parent node if any.
     */
    public Optional<TreeZipper<N>> removeAndUp() {
        // removes current node and goes up to parent
        if (up == null) {
            return Optional.empty();
        } else {
            //
            throw new RuntimeException("Not impled!");
        }
    }

    /**
     * Returns the zipper for the next node on depth-first walk.
     */
    public Optional<TreeZipper<N>> next() {
        throw new RuntimeException("Not impled!");
    }

    /**
     * Returns the zipper for the previous node on depth-first walk.
     */
    public Optional<TreeZipper<N>> previous() {
        throw new RuntimeException("Not impled!");
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

    @Override
    public String toString() {
        return "<zip: " + current.toString() + ">";
    }

    final static class Cons<M> {
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
            final LinkedList<M> stack = new LinkedList<>();

            // we need to reverse source
            while (source.hasNext()) {
                stack.addFirst(source.next());
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

        default T factory(T prototype, Iterable<T> newChildren) {
            throw new IllegalStateException("Modification of node is not supported!");
        }
    }
}
