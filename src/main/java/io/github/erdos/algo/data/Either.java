package io.github.erdos.algo.data;

import java.util.Optional;
import java.util.concurrent.Callable;
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

    public <X> X accept(Visitor<L, R, X> visitor) {
        if (this.left != null) {
            return visitor.left(left);
        } else {
            return visitor.right(right);
        }
    }

    public interface Visitor<L, R, X> {
        X left(L left);

        X right(R right);
    }
}
