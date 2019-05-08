package io.github.erdos.algo.zip;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static io.github.erdos.algo.zip.TreeZipper.zipper;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings({"ArraysAsListWithZeroOrOneArgument", "OptionalGetWithoutIsPresent"})
class TreeZipperTest {

    @Test
    public void testEmptyOnRoot() {
        TestTree data = new TestTree("a", asList(new TestTree("b", asList())));
        TreeZipper<TestTree> zipper = zipper(data, FACTORY);

        assertTrue(zipper.down().isPresent());
        assertFalse(zipper.left().isPresent());
        assertFalse(zipper.right().isPresent());
        assertFalse(zipper.up().isPresent());

        assertEquals("a", zipper.node().node);
    }

    @Test
    public void testEmptyOnRoot2() {
        TestTree data = new TestTree("a", asList(new TestTree("b", asList())));
        TreeZipper<TestTree> zipper = zipper(data, FACTORY);

        TreeZipper<TestTree> down = zipper.down().get();
        assertFalse(down.left().isPresent());
        assertFalse(down.right().isPresent());
        assertFalse(down.up().isPresent());

        assertEquals("b", down.node().node);
    }

    @Test
    public void testUpDownIdempotent() {
        TestTree data = new TestTree("a", asList(new TestTree("b", asList(new TestTree("c", asList())))));
        TreeZipper<TestTree> zipper = zipper(data, FACTORY);

        TreeZipper<TestTree> down = zipper.down().get();
        TreeZipper<TestTree> down2 = zipper.down().get().down().get();

        assertEquals(down.up().get(), zipper);
        assertEquals(down2.up().get(), down);
    }

    @Test
    public void testEmptyLeft() {
        TestTree data = new TestTree("a", asList(new TestTree("b", asList(new TestTree("c", asList())))));
        TreeZipper<TestTree> zipper = zipper(data, FACTORY);

        assertFalse(zipper.down().get().left().isPresent());
        assertFalse(zipper.down().get().down().get().left().isPresent());

    }

    @Test
    public void testEmptyRight() {
        TestTree data = new TestTree("a", asList(new TestTree("b", asList(new TestTree("c", asList())))));
        TreeZipper<TestTree> zipper = zipper(data, FACTORY);

        assertFalse(zipper.down().get().right().isPresent());
        assertFalse(zipper.down().get().down().get().right().isPresent());
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

        TestTree(String node, Collection<TestTree> children) {
            this.node = node;
            this.children = children;
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
    }
}