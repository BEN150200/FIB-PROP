package src.helpers;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import io.vavr.Tuple;
import io.vavr.Tuple2;

public class Functional {
    /** Converts a BiFunction to a Function(Tuple2) */
    public static <A, B, T> Function<Tuple2<A, B>, T> pair(BiFunction<A, B, T> f) {
        return t -> f.apply(t._1, t._2);
    }

    /** Converts a BiFunction to a Prediacte(Tuple2) */
    public static <A, B, T> Predicate<Tuple2<A, B>> pairP(BiFunction<A, B, Boolean> f) {
        return t -> f.apply(t._1, t._2);
    }

    /** Converts a BiFunction to a Function(Tuple2 -> Tuple2) w/o modifying tuple::_1 */
    public static <A, B, T> BiFunction<A, B, Tuple2<A, T>> value(BiFunction<A, B, T> f) {
        return (k, v) -> Tuple.of(k, f.apply(k, v));
    }
}
