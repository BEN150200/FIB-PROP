package helpers;

import java.util.function.Function;

import io.vavr.collection.HashMap;

public class Maths {
    public static double log2(double x) {
        return Math.log(x) / Math.log(2);
    }

    public static double square(double x) {
        return x*x;
    }

    public static Function<Double, Double> divideBy(double d) {
        return x -> x / d;
    }

    public static double round(double value, int decimals) {
        double power = Math.pow(10, decimals);
        return Math.round(value*power)/power;
    }

    public static <T> Double norm(HashMap<T, Double> vector) {
        return Math.sqrt(
            vector.values().map(Maths::square).sum().doubleValue()
        );
    }

    public static <T> HashMap<T, Double> normalized(HashMap<T, Double> vector) {
        var norm = Maths.norm(vector);
        return vector.mapValues(Maths.divideBy(norm));
    }
}
