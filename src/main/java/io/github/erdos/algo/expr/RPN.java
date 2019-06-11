package io.github.erdos.algo.expr;

import java.util.Iterator;

public final class RPN implements Iterable<Item> {

    private final Iterable<Item> items;

    public RPN(Iterable<Item> items) {
        this.items = items;
    }

    @Override
    public Iterator<Item> iterator() {
        return items.iterator();
    }

}
