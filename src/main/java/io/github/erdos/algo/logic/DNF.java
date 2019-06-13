package io.github.erdos.algo.logic;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toSet;

/**
 * Disjunctive normal form.
 */
public final class DNF<A> {

	private final Set<ConjuctiveClause<A>> clauses;

	public DNF(Set<ConjuctiveClause<A>> clauses) {this.clauses = clauses;}

	public DNF<A> or(ConjuctiveClause<A> c) {
		Set<ConjuctiveClause<A>> cls = new HashSet<>(clauses);
		cls.add(c);
		return new DNF<>(cls);
	}

	public DNF<A> or(Literal<A> c) {
		Set<ConjuctiveClause<A>> cls = new HashSet<>(clauses);
		cls.add(ConjuctiveClause.conjunction(c));
		return new DNF<>(cls);
	}

	public DNF<A> and(Literal<A> literal) {
		Set<ConjuctiveClause<A>> newClauses = clauses.stream().map(c -> c.and(literal)).collect(toSet());
		return new DNF<>(newClauses);
	}

	public DNF<A> and(ConjuctiveClause<A> literal) {
		Set<ConjuctiveClause<A>> newClauses = clauses.stream().map(c -> c.and(literal)).collect(toSet());
		return new DNF<>(newClauses);
	}

	public DNF<A> or(DNF<A> other) {
		Set<ConjuctiveClause<A>> newClauses = new HashSet<>(clauses);
		newClauses.addAll(other.clauses);
		return new DNF<>(newClauses);
	}

	public DNF<A> negate() {
		Set<ConjuctiveClause<A>> newClauses = clauses.stream().map(ConjuctiveClause::negateLiterals).collect(toSet());
		return new DNF<>(newClauses);
	}

	public boolean eval(Predicate<A> predicate) {
		return clauses.stream().anyMatch(x->x.eval(predicate));
	}

	public CNF<A> toCNF() {
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DNF<?> dnf = (DNF<?>) o;
		return clauses.equals(dnf.clauses);
	}

	@Override
	public int hashCode() {
		return Objects.hash(clauses);
	}
}
