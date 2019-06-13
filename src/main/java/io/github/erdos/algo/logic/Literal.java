package io.github.erdos.algo.logic;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public final class Literal<A> {
	private final A atom;
	private final boolean positive;

	private Literal(A atom, boolean positive) {
		this.atom = atom;
		this.positive = positive;

		if (atom == null) {
			throw new IllegalArgumentException("Atom can not be null!");
		}
	}

	public Literal<A> positive(A a) {
		return new Literal<>(a, true);
	}

	public Literal<A> negative(A a) {
		return new Literal<>(a, false);
	}

	public <R> R visit(Function<A, R> mapPositive, Function<A, R> mapNegative) {
		return positive ? mapPositive.apply(atom) : mapNegative.apply(atom);
	}

	public Literal<A> negate() {
		return new Literal<>(atom, !positive);
	}

	public A getAtom() {
		return atom;
	}

	public boolean isPositive() {
		return positive;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Literal<?> literal = (Literal<?>) o;
		return positive == literal.positive && Objects.equals(atom, literal.atom);
	}

	@Override
	public int hashCode() {
		return Objects.hash(atom, positive);
	}

	public boolean eval(Predicate<A> predicate) {
		return predicate.test(atom) == positive;
	}
}
