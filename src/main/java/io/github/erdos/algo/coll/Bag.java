package io.github.erdos.algo.coll;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class Bag<T> implements Collection<T> {

    private final Map<T, Integer> elems;
    private int size = 0;

    private Bag(Map<T, Integer> elems) {
        this.elems = elems;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return elems.containsKey(o);
    }

    @Override
    public Iterator<T> iterator() {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public Object[] toArray() {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public <T1> T1[] toArray(T1[] t1s) {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public boolean add(T t) {
        size++;
        if (elems.containsKey(t)) {
            elems.put(t, elems.get(t) + 1);
        } else {
            elems.put(t, 1);
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {

        if (elems.containsKey(o)) {
            size--;

            int count = elems.get(o);
            if (count == 1) {
                elems.remove(o);
            } else {
                elems.put((T) o, count - 1);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return collection.stream().allMatch(elems::containsKey);
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        throw new RuntimeException("Not implemented!");
    }

    @Override
    public void clear() {
        size = 0;
        elems.clear();
    }
}
