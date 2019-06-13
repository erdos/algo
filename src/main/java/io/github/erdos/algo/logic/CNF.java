package io.github.erdos.algo.logic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toSet;

/**
 * Conjunctive normal form.
 */
public final class CNF<A> {

	private final Set<DisjunctiveClause<A>> clauses;

	public CNF(Set<DisjunctiveClause<A>> clauses) {
		this.clauses = Collections.unmodifiableSet(clauses);
	}

	public CNF<A> or(DisjunctiveClause<A> c) {
		Set<DisjunctiveClause<A>> cls = new HashSet<>(clauses);
		cls.add(c);
		return new CNF<>(cls);
	}

	public CNF<A> or(Literal<A> c) {
		Set<DisjunctiveClause<A>> cls = new HashSet<>(clauses);
		cls.add(DisjunctiveClause.disjunction(c));
		return new CNF<>(cls);
	}

	public CNF<A> and(Literal<A> literal) {
		return new CNF<>(clauses.stream().map(c -> c.or(literal)).collect(toSet()));
	}

	public CNF<A> and(DisjunctiveClause<A> literal) {
		return new CNF<>(clauses.stream().map(c -> c.or(literal)).collect(toSet()));
	}

	public CNF<A> negate() {
		return new CNF<>(clauses.stream().map(DisjunctiveClause::negateLiterals).collect(toSet()));
	}

	public boolean eval(Predicate<A> predicate) {
		return clauses.stream().allMatch(c -> c.eval(predicate));
	}

	public DNF toDNF() {
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CNF<?> cnf = (CNF<?>) o;
		return clauses.equals(cnf.clauses);
	}

	@Override
	public int hashCode() {
		return Objects.hash(clauses);
	}
}
