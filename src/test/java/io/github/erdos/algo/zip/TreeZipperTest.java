package io.github.erdos.algo.zip;

import org.junit.jupiter.api.Test;

import java.util.*;

import static io.github.erdos.algo.zip.TreeZipper.zipper;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings({"ArraysAsListWithZeroOrOneArgument", "OptionalGetWithoutIsPresent", "WeakerAccess"})
class TreeZipperTest {

    @Test
    public void testEmptyOnRoot() {
        TestTree data = new TestTree("a", new TestTree("b"));
        TreeZipper<TestTree> zipper = zipper(data, FACTORY);

        assertTrue(zipper.down().isPresent());
        assertFalse(zipper.left().isPresent());
        assertFalse(zipper.right().isPresent());
        assertFalse(zipper.up().isPresent());

        assertEquals("a", zipper.node().node);
    }

    @Test
    public void testEmptyOnRoot2() {
        TestTree data = new TestTree("a", new TestTree("b"));
        TreeZipper<TestTree> zipper = zipper(data, FACTORY);

        TreeZipper<TestTree> down = zipper.down().get();
        assertFalse(down.left().isPresent());
        assertFalse(down.right().isPresent());

        assertTrue(down.up().isPresent());

        assertEquals("b", down.node().node);
        assertEquals("a", down.up().get().node().node);
    }

    @Test
    public void testUpDownIdempotent() {
        TestTree data = new TestTree("a", new TestTree("b", new TestTree("c")));
        TreeZipper<TestTree> zipper = zipper(data, FACTORY);

        TreeZipper<TestTree> down = zipper.down().get();
        TreeZipper<TestTree> down2 = zipper.down().get().down().get();

        assertEquals(down.up().get(), zipper);
        assertEquals(down.up().get().node(), zipper.node());

        // assertEquals(down2.up().get(), down);
        assertEquals(down2.up().get().node(), down.node());
    }

    @Test
    public void testFullCycle() {
        TestTree data = new TestTree("a", new TestTree("b1"), new TestTree("b2", new TestTree("c")));
        TreeZipper<TestTree> a = zipper(data, FACTORY);

        TreeZipper<TestTree> b1 = a.down().get();
        assertEquals("b1", b1.node().node);

        TreeZipper<TestTree> b2 = b1.right().get();
        assertEquals("b2", b2.node().node);

        TreeZipper<TestTree> b2up = b2.up().get();

        assertEquals(a, b2up);
    }

    @Test
    public void testEmptyLeft() {
        TestTree data = new TestTree("a", new TestTree("b", new TestTree("c")));
        TreeZipper<TestTree> zipper = zipper(data, FACTORY);

        assertFalse(zipper.down().get().left().isPresent());
        assertFalse(zipper.down().get().down().get().left().isPresent());
    }

    @Test
    public void testEmptyRight() {
        TestTree data = new TestTree("a", new TestTree("b", new TestTree("c")));
        TreeZipper<TestTree> zipper = zipper(data, FACTORY);

        assertFalse(zipper.down().get().right().isPresent());
        assertFalse(zipper.down().get().down().get().right().isPresent());
    }

    @Test
    public void testLeftmostAndRightmost() {
        TestTree data = new TestTree("a", new TestTree("b1"), new TestTree("b2"), new TestTree("b3"));
        TreeZipper<TestTree> zipper = zipper(data, FACTORY);

        assertEquals("b1", zipper.down().get().leftmost().node().node);
        assertEquals("b3", zipper.down().get().rightmost().node().node);
        assertEquals("b1", zipper.down().get().rightmost().leftmost().node().node);
    }

    @Test
    public void testRight() {
        TestTree data = new TestTree("a", new TestTree("b1"), new TestTree("b2"), new TestTree("b3"));
        TreeZipper<TestTree> zipper = zipper(data, FACTORY);

        assertEquals("b1", zipper.down().get().node().node);
        assertEquals("b2", zipper.down().get().right().get().node().node);
        assertEquals("b3", zipper.down().get().right().get().right().get().node().node);
        assertFalse(zipper.down().get().right().get().right().get().right().isPresent());
    }

    @Test
    public void testLeft() {
        TestTree data = new TestTree("a", new TestTree("b1"), new TestTree("b2"), new TestTree("b3"));
        TreeZipper<TestTree> zipper = zipper(data, FACTORY);

        assertEquals("b1", zipper.down().get().node().node);
        assertEquals("b1", zipper.down().get().right().get().left().get().node().node);
        assertFalse(zipper.down().get().left().isPresent());
    }

    public static final TreeZipper.Factory<TestTree> FACTORY = new TreeZipper.Factory<TestTree>() {
        @Override
        public Iterable<TestTree> children(TestTree node) {
            return node.children;
        }

        @Override
        public TestTree factory(TestTree prototype, Iterable<TestTree> newChildren) {

            final List<TestTree> ch = new LinkedList<>();
            newChildren.forEach(ch::add);

            return new TestTree(prototype.node, ch);
        }
    };

    public static class TestTree {
        final String node;
        final Collection<TestTree> children;

        TestTree(String node, TestTree... children) {
            this.node = node;
            this.children = asList(children);
        }

        TestTree(String node, Collection<TestTree> children) {
            this.node = node;
            this.children = children;
        }


        TestTree(String node) {
            this.node = node;
            this.children = Collections.emptyList();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestTree testTree = (TestTree) o;
            return Objects.equals(node, testTree.node) &&
                    Objects.equals(children, testTree.children);
        }

        @Override
        public int hashCode() {
            return Objects.hash(node, children);
        }

        @Override
        public String toString() {
            return "TestTree{" +
                    "node='" + node + '\'' +
                    ", children=" + children +
                    '}';
        }
    }

    @Test
    public void fromIterableTest() {
        List<String> items = asList("a", "b", "c");
        TreeZipper.Cons<String> cons = TreeZipper.Cons.fromIterable(items);

        assertEquals("a", cons.head);
        assertEquals("b", cons.tail.head);
        assertEquals("c", cons.tail.tail.head);
        assertNull(cons.tail.tail.tail);
    }
}