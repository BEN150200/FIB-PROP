package helpers;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;

public class Maps {
    public static BiFunction<Double, Double, Double> add = (x, y) -> x + y;
    public static <T> T firstKey(T k1, T k2) {
        return k1;
    }

    public static <K, V, T> Function<Map.Entry<K, V>, Map.Entry<K, T>> value(BiFunction<K, V, T> mapper) {
        return entry -> Map.entry(entry.getKey(), mapper.apply(entry.getKey(), entry.getValue()));
    }

    public static <K, T> HashMap<K, HashSet<T>> unionMerge(HashMap<K, HashSet<T>> a, HashMap<K, HashSet<T>> b) {
        return a.merge(b, HashSet::union);
    }

    public static <K1, K2, T> HashMap<K1, HashMap<K2, HashSet<T>>> nestedUnionMerge(HashMap<K1, HashMap<K2, HashSet<T>>> a, HashMap<K1, HashMap<K2, HashSet<T>>> b) {
        return a.merge(b, Maps::unionMerge);
    }
}
