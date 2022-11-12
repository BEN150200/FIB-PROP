package helpers;

import java.util.function.Function;

import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import io.vavr.collection.Traversable;

public class Sets {
    public static <T, U> HashSet<U> unionFold(Traversable<T> xs, Function<T, Set<U>> lens) {
        return xs.foldLeft(
            HashSet.<U>empty(),
            (set, x) -> set.union(lens.apply(x))
        );
    }
}
