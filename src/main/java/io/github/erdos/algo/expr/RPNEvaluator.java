package io.github.erdos.algo.expr;

import java.util.Map;
import java.util.Stack;

public class RPNEvaluator {

    public RPNEvaluator(Map<String, Number> bindings) {
        this.bindings = bindings;
    }

    private final Map<String, Number> bindings;

    public Number evaluate(RPN input) {
        Stack<Number> stack = new Stack<>();
        for (RPN.Item item : input) {
            item.apply(new RPN.ItemVisitor<Void>() {
                @Override
                public Void visitNumber(Number number) {
                    stack.push(number);
                    return null;
                }

                @Override
                public Void visitVariable(String variableName) {
                    Number number = bindings.get(variableName);
                    if (number == null) {
                        throw new IllegalStateException("Unexpected variable: " + variableName);
                    } else {
                        stack.push(number);
                    }
                    return null;
                }

                @Override
                public Void visitOperator(RPN.Operator op) {
                    return null;
                }
            });
        }
        if (stack.size() > 1) {
            throw new IllegalStateException("");
        } else if (stack.size() < 1) {
            throw new IllegalStateException("");
        } else {
            return stack.pop();
        }
    }
}
