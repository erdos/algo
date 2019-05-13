package io.github.erdos.algo.expr;

import java.util.Iterator;

public final class RPN implements Iterable<RPN.Item> {

    private final Iterable<RPN.Item> items;

    public RPN(Iterable<Item> items) {
        this.items = items;
    }

    @Override
    public Iterator<Item> iterator() {
        return items.iterator();
    }

    interface Item {
        <R> R apply(ItemVisitor<R> visitor);
    }

    interface ItemVisitor<R> {
        R visitNumber(Number number);

        R visitVariable(String variableName);

        R visitOperator(Operator op);
    }

    enum Operator {
        PLUS(2), MINUS(2), TIMES(3), DIVIDE(4), POW(5),
        // MOD(precedence),
        GTE(-9), LTE(-9), LT(-9), GT(-9),
        NEQ(-10), EQ(-10),
        AND(-20), OR(-21),
        OPEN(-999),
        CLOSE(-998),
        NOT(6);

        public final int precedence;

        Operator(int precedence) {
            this.precedence = precedence;
        }
    }
}
