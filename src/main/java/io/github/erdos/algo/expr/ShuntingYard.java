package io.github.erdos.algo.expr;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Turns a sequence of tokens in to an RPN sequence.
 */
public class ShuntingYard {

    public RPN run(TokenSequence tokenSequence) {
        Stack<Item> stack = new Stack<>();
        Queue<Item> output = new LinkedList<>();

        for (Item item : tokenSequence) {
            item.apply(new Item.ItemVisitor<Object>() {
                @Override
                public Object visitNumber(Number number) {
                    return stack.push(item);
                }

                @Override
                public Object visitVariable(String variableName) {
                    return stack.push(item);
                }

                @Override
                public Object visitOperator(Item.Operator op) {
                    // a stack tetejerol leszedjuk az operatorokat amig lehet.
                    // todo:
                    int precedence = op.precedence;

                    while (true) {
                        Boolean cont = stack.pop().apply(new Item.ItemVisitor<Boolean>() {
                            @Override
                            public Boolean visitNumber(Number number) {
                                return output.add(item);
                            }

                            @Override
                            public Boolean visitVariable(String variableName) {
                                return null;
                            }

                            @Override
                            public Boolean visitOperator(Item.Operator op) {
                                return null;
                            }

                            @Override
                            public Boolean visitOpenParen() {
                                return null;
                            }

                            @Override
                            public Boolean visitCloseParen() {
                                return null;
                            }
                        });

                    }
                    return null;
                }

                @Override
                public Object visitOpenParen() {
                    return stack.push(item);
                }

                @Override
                public Object visitCloseParen() {
                    return visitCloseParentheses(`stack, output);
                }
            });
        }

        return null;
    }

    protected Object visitCloseParentheses(Stack<Item> stack, Queue<Item> output) {
        while (true) {
            Item stackPop = stack.pop();
            Boolean cont = stackPop.apply(new Item.ItemVisitor<Boolean>() {
                @Override
                public Boolean visitNumber(Number number) {
                    output.add(stackPop);
                    return true;
                }

                @Override
                public Boolean visitVariable(String variableName) {
                    output.add(stackPop);
                    return true;
                }

                @Override
                public Boolean visitOperator(Item.Operator op) {
                    output.add(stackPop);
                    return true;
                }

                @Override
                public Boolean visitOpenParen() {
                    output.add(stackPop);
                    return false;
                }

                @Override
                public Boolean visitCloseParen() {
                    throw new IllegalStateException("Stack must not contain closing parentheses.");
                }
            });
            if (!cont) break;
        }
        return null;
    }

}
