package io.github.erdos.algo.data;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings({"WeakerAccess", "unused"})
public final class Either<L, R> {

    private final L left;
    private final R right;

    private Either(L left, R right) {
        this.left = left;
        this.right = right;
        assert (left == null) != (right == null);
    }

    public static <L, R> Either<L, R> left(L left) {
        if (left == null) {
            throw new NullPointerException("Can not instantiate with null.");
        } else {
            return new Either<>(left, null);
        }
    }

    public static <L, R> Either<L, R> right(R right) {
        if (right == null) {
            throw new NullPointerException("Can not instantiate with null.");
        } else {
            return new Either<>(null, right);
        }
    }

    public static <X> Either<Exception, X> call(Callable<X> callable) {
        try {
            return Either.right(callable.call());
        } catch (Exception e) {
            return Either.left(e);
        }
    }

    public <X, Y> Either<X, Y> map(Function<L, X> f1, Function<R, Y> f2) {
        return accept(new Visitor<L, R, Either<X, Y>>() {
            @Override
            public Either<X, Y> left(L left) {
                return Either.left(f1.apply(left));
            }

            @Override
            public Either<X, Y> right(R right) {
                return Either.right(f2.apply(right));
            }
        });
    }

    public <X> Either<X, R> mapLeft(Function<L, X> f) {
        return map(f, Function.identity());
    }

    public <Y> Either<L, Y> mapRight(Function<R, Y> f) {
        return map(Function.identity(), f);
    }

    public Optional<L> maybeLeft() {
        return Optional.ofNullable(left);
    }

    public Optional<R> maybeRight() {
        return Optional.ofNullable(right);
    }

    public Either<R, L> flip() {
        return new Either<>(right, left);
    }

    public boolean isLeft() {
        return left != null;
    }

    public boolean isRight() {
        return right != null;
    }

    public Either<L, R> replace(Function<L, R> leftToRight, Function<R, L> rightToLeft) {
        return accept(new Visitor<L, R, Either<L, R>>() {
            @Override
            public Either<L, R> left(L left) {
                return Either.right(leftToRight.apply(left));
            }

            @Override
            public Either<L, R> right(R right) {
                return Either.left(rightToLeft.apply(right));
            }
        });
    }

    public void accept(Consumer<L> consumeLeft, Consumer<R> consumeRight) {
        if (this.left != null) {
            consumeLeft.accept(left);
        } else {
            consumeRight.accept(right);
        }
    }

    public <X> X accept(Function<L, X> applyLeft, Function<R, X> applyRight) {
        if (this.left != null) {
            return applyLeft.apply(left);
        } else {
            return applyRight.apply(right);
        }
    }

    public <X> X accept(Visitor<L, R, X> visitor) {
        return accept(visitor::left, visitor::right);
    }

    public interface Visitor<L, R, X> {
        X left(L left);

        X right(R right);
    }

    @Override
    public String toString() {
        if (left != null) {
            return "<Either:Left=" + left + ">";
        } else {
            return "<Either:Right=" + right + ">";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Either<?, ?> either = (Either<?, ?>) o;
        return Objects.equals(left, either.left) && Objects.equals(right, either.right);
    }

    @Override
    public int hashCode() {
        if (left == null) {
            return right.hashCode();
        } else {
            return left.hashCode();
        }
    }
}
