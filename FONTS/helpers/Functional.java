package helpers;

import java.util.function.BiFunction;

public class Functional {
    public static BiFunction<Double, Double, Double> add = (x, y) -> x + y;
    public static <T> T firstKey(T k1, T k2) {
        return k1;
    }
}
