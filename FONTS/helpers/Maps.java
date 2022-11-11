package helpers;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

public class Maps {
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

    public static <K> HashMap<K, Double> addingMerge(HashMap<K, Double> a, HashMap<K, Double> b) {
        return a.merge(b, Maths::add);
    }

    /**
     * 
     * @param <K1>
     * @param <K2>
     * @param <V>
     * @param key1
     * @param key2
     * @param map
     * @return the map with map[key1][key2] removed. If map[key1] is rendered empty, then key1 is removed
     */
    public static <K1, K2, V> HashMap<K1, HashMap<K2, V>> nestedRemove(K1 key1, K2 key2, HashMap<K1, HashMap<K2, V>> map) {
        var removedMap = map.computeIfPresent(
            key1,
            (__, nestedMap) -> nestedMap.remove(key2)
        )._2;

        return removedMap.get(key1)
            .filter(HashMap::isEmpty)
            .isDefined()
                ? removedMap.remove(key1)
                : removedMap;
    }
}
