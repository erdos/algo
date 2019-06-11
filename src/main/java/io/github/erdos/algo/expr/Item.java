package io.github.erdos.algo.expr;

interface Item {
    <R> R apply(ItemVisitor<R> visitor);

    interface ItemVisitor<R> {
        R visitNumber(Number number);

        R visitVariable(String variableName);

        R visitOperator(Operator op);

        R visitOpenParen();

        R visitCloseParen();
    }

    enum Operator {
        PLUS(2), MINUS(2), TIMES(3), DIVIDE(4), POW(5),
        // MOD(precedence),
        GTE(-9), LTE(-9), LT(-9), GT(-9),
        NEQ(-10), EQ(-10),
        AND(-20), OR(-21),
        OPEN(-999),
        CLOSE(-998),
        NOT(6, false), NEG(6, false);

        public final int precedence;
        public final boolean leftAssociative;

        Operator(int precedence) {
            this.precedence = precedence;
            this.leftAssociative = true;
        }

        Operator(int precedence, boolean leftAssociative) {
            this.precedence = precedence;
            this.leftAssociative = leftAssociative;
        }

    }
}
