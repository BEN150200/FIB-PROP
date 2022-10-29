package helpers;

import java.security.InvalidParameterException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Maps {
    public static BiFunction<Double, Double, Double> add = (x, y) -> x + y;
    public static <T> T firstKey(T k1, T k2) {
        return k1;
    }

    public static <K, V, T> Function<Map.Entry<K, V>, Map.Entry<K, T>> value(BiFunction<K, V, T> mapper) {
        return entry -> Map.entry(entry.getKey(), mapper.apply(entry.getKey(), entry.getValue()));
    }
}
