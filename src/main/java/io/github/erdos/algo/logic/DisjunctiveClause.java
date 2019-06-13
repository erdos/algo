package io.github.erdos.algo.logic;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

// disjuction of literals: A or B or C or !D ...
public final class DisjunctiveClause<A> {

	private final Set<Literal<A>> literals;

	private DisjunctiveClause(Set<Literal<A>> literals) {this.literals = literals;}

	public static <A> DisjunctiveClause<A> disjunction(Literal<A>... literals) {
		return new DisjunctiveClause<>(new HashSet<>(asList(literals)));
	}

	public DisjunctiveClause<A> or(Literal<A> literal) {
		Set<Literal<A>> newLiterals = new HashSet<>(literals);
		newLiterals.add(literal);
		return new DisjunctiveClause<>(newLiterals);
	}

	public DisjunctiveClause<A> or(Literal<A>... orLiterals) {
		Set<Literal<A>> newLiterals = new HashSet<>(literals);
		newLiterals.addAll(Arrays.asList(orLiterals));
		return new DisjunctiveClause<>(newLiterals);
	}

	public DisjunctiveClause<A> or(Collection<Literal<A>> orLiterals) {
		Set<Literal<A>> newLiterals = new HashSet<>(literals);
		newLiterals.addAll(orLiterals);
		return new DisjunctiveClause<>(newLiterals);
	}

	public DisjunctiveClause<A> or(DisjunctiveClause<A> orLiterals) {
		return or(orLiterals.literals);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DisjunctiveClause<?> clause = (DisjunctiveClause<?>) o;
		return literals.equals(clause.literals);
	}

	@Override
	public int hashCode() {
		return literals.hashCode();
	}

	public DisjunctiveClause<A> negateLiterals() {
		Set<Literal<A>> newLiterals = literals.stream().map(Literal::negate).collect(Collectors.toSet());
		return new DisjunctiveClause<>(newLiterals);
	}

	public boolean eval(Predicate<A> predicate) {
		return literals.stream().anyMatch(x->x.eval(predicate));
	}
}
