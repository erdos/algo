package io.github.erdos.algo.logic;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

// conjuction of literals: A and !B and C and D ...
@SuppressWarnings({"WeakerAccess", "unused"})
public final class ConjuctiveClause<A> {

	private final Set<Literal<A>> literals;

	private ConjuctiveClause(Set<Literal<A>> literals) {this.literals = literals;}

	public static <A> ConjuctiveClause<A> conjunction(Literal<A>... literals) {
		return new ConjuctiveClause<>(new HashSet<>(asList(literals)));
	}

	public ConjuctiveClause<A> and(Literal<A> literal) {
		Set<Literal<A>> newLiterals = new HashSet<>(literals);
		newLiterals.add(literal);
		return new ConjuctiveClause<>(newLiterals);
	}

	public ConjuctiveClause<A> and(Literal<A>... andLiterals) {
		Set<Literal<A>> newLiterals = new HashSet<>(literals);
		newLiterals.addAll(asList(andLiterals));
		return new ConjuctiveClause<>(newLiterals);
	}

	public ConjuctiveClause<A> and(Collection<Literal<A>> andLiterals) {
		Set<Literal<A>> newLiterals = new HashSet<>(literals);
		newLiterals.addAll(andLiterals);
		return new ConjuctiveClause<>(newLiterals);
	}

	public ConjuctiveClause<A> and(ConjuctiveClause<A> andLiterals) {
		return and(andLiterals.literals);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ConjuctiveClause<?> clause = (ConjuctiveClause<?>) o;
		return literals.equals(clause.literals);
	}

	@Override
	public int hashCode() {
		return literals.hashCode();
	}

	public ConjuctiveClause<A> negateLiterals() {
		Set<Literal<A>> newLiterals = literals.stream().map(Literal::negate).collect(Collectors.toSet());
		return new ConjuctiveClause<>(newLiterals);
	}

	public boolean eval(Predicate<A> predicate) {
		return literals.stream().allMatch(x->x.eval(predicate));
	}
}
