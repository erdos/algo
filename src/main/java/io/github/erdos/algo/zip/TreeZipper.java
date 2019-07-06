package io.github.erdos.algo.zip;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@SuppressWarnings("WeakerAccess")
public final class TreeZipper<N> {

    private final Factory<N> factory;
    private final Node<N> node;

    static class Node<N> {
        private final Cons<N> left;
        private final Cons<N> right;

        private final Node<N> up;

        // current node, not null.
        private final N current;

        Node(N current, Cons<N> left, Cons<N> right, Node<N> up) {
            this.left = left;
            this.right = right;
            this.up = up;
            this.current = current;

            assert current != null;
            assert left == null || left.head != current;
            assert right == null || right.head != current;
            assert up == null || up.current != current;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?> node = (Node<?>) o;
            return Objects.equals(left, node.left) &&
                    Objects.equals(right, node.right) &&
                    Objects.equals(up, node.up) &&
                    Objects.equals(current, node.current);
        }

        @Override
        public int hashCode() {
            return Objects.hash(left, right, up, current);
        }
    }

    private TreeZipper(Node<N> node, Factory<N> factory) {
        this.node = node;
        this.factory = factory;
    }

    public static <X> TreeZipper<X> zipper(X root, Factory<X> factory) {
        return new TreeZipper<>(new Node<>(root, null, null, null), factory);
    }

    public N node() {
        return node.current;
    }

    /**
     * Returns the zipper on the closest left sibling if any.
     */
    public Optional<TreeZipper<N>> left() {
        if (node.left == null) {
            return Optional.empty();
        } else {
            Cons<N> right2 = new Cons<>(node.current, node.right);
            Node<N> result = new Node<>(node.left.head, node.left.tail, right2, node.up);
            return Optional.of(new TreeZipper<>(result, factory));
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
        if (node.right == null) {
            return Optional.empty();
        } else {
            Cons<N> left2 = new Cons<>(node.current, node.left);
            Node<N> result = new Node<>(node.right.head, left2, node.right.tail, node.up);
            return Optional.of(new TreeZipper<>(result, factory));
        }
    }

    public Iterable<N> lefts() {
        return () -> new Iterator<N>() {
            Cons<N> lefts = node.left;

            @Override
            public boolean hasNext() {
                return lefts != null;
            }

            @Override
            public N next() {
                if (lefts == null) {
                    throw new NoSuchElementException();
                }
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
            Cons<N> rights = node.right;

            @Override
            public boolean hasNext() {
                return rights != null;
            }

            @Override
            public N next() {
                if (rights == null) {
                    throw new NoSuchElementException();
                }
                try {
                    return rights.head;
                } finally {
                    rights = rights.tail;
                }
            }
        };
    }

    public Optional<TreeZipper<N>> up() {
        if (node.up == null) {
            return Optional.empty();
        } else {

            final LinkedList<N> newChildren = new LinkedList<>();

            for (Cons<N> item = node.left; item != null; item = item.tail) {
                newChildren.addFirst(item.head);
            }
            newChildren.addLast(node.current);
            for (Cons<N> item = node.right; item != null; item = item.tail) {
                newChildren.addLast(item.head);
            }

            final N parent = factory.factory(node.up.current, newChildren);

            if (node.up.up == null) {
            	Node<N> newNode = new Node<>(node.up.current, node.up.left, node.up.right, null);
                return Optional.of(new TreeZipper<>(newNode, factory));
            } else {
                final N grandparent = node.up.up.current;
                final Iterable<N> aunts = factory.children(grandparent);

                final Cons<N> rights2 = Cons.fromIterable(aunts);
                Node<N> newNode = new Node<>(parent, null, rights2, node.up.up);
                return Optional.of(new TreeZipper<>(newNode, factory));
            }
        }
    }


    public TreeZipper<N> insertRight(N item) {
        Cons<N> newLefts = new Cons<>(node.current, node.left);
        Node<N> newNode = new Node<>(item, newLefts, node.right, node.up);
        return new TreeZipper<>(newNode, factory);
    }


    public TreeZipper<N> insertLeft(N item) {
        Cons<N> newRights = new Cons<>(node.current, node.right);
        Node<N> newNode = new Node<>(item, node.left, newRights, node.up);
        return new TreeZipper<>(newNode, factory);
    }


    public Iterable<N> ups() {
        return () -> new Iterator<N>() {
            Node<N> ups = node.up;

            @Override
            public boolean hasNext() {
                return ups != null;
            }

            @Override
            public N next() {
                if (ups == null) {
                    throw new NoSuchElementException();
                }
                try {
                    return ups.current;
                } finally {
                    ups = ups.up;
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
        Iterator<N> ch = factory.children(node.current).iterator();
        if (ch.hasNext()) {

            final N firstChild = ch.next();

            Cons<N> rights = Cons.fromIterator(ch);

            Node<N> result = new Node<>(firstChild, null, rights, this.node);
            return Optional.of(new TreeZipper<>(result, factory));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Edits the content of the current node.
     */
    public TreeZipper<N> edit(Function<N, N> edit) {
        final N edited = edit.apply(node.current);
        Node<N> newNode = new Node<>(edited, node.left, node.right, node.up);
        return new TreeZipper<>(newNode, factory);
    }

    /**
     * Replaces the content of the node in the current tree.
     */
    public TreeZipper<N> replace(N newContent) {
        return edit(ignored -> newContent);
    }

    /**
     * Remoces current node from the tree and moves to the parent node if any.
     */
    public Optional<TreeZipper<N>> removeAndUp() {
        // removes current node and goes up to parent
        if (node.up == null) {
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
        return Objects.equals(node, that.node) && Objects.equals(factory, that.factory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(node, factory);
    }

    @Override
    public String toString() {
        return "<zip: " + node.current.toString() + ">";
    }

    static final class Cons<M> {
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cons<?> cons = (Cons<?>) o;
            return Objects.equals(head, cons.head) &&
                    Objects.equals(tail, cons.tail);
        }

        @Override
        public int hashCode() {
            return Objects.hash(head, tail);
        }
    }

    interface Factory<T> {
        Iterable<T> children(T node);

        default T factory(T prototype, Iterable<T> newChildren) {
            throw new IllegalStateException("Modification of node is not supported!");
        }
    }
}
