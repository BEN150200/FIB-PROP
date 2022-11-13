package src.helpers;

import java.security.InvalidParameterException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Lists {
    /**
     * 
     * @param <T>
     * @param list
     * @return a mapping of the elements of the list to their frequencies (on the list)
     */
    public static <T> Map<T, Long> frequencies(Stream<T> stream) {
        return stream
            .collect(
                Collectors.groupingBy(
                    Function.identity(),
                    Collectors.counting()
            ));
    }

    /**
     * 
     * @param <T> list element type
     * @param list
     * @return max frequency of elements in list
     */
    public static <T> Long maxFrequency(Iterable<T> iterable) {
        return Lists.frequencies(StreamSupport.stream(iterable.spliterator(), true))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getValue)
                .orElse(0L);
    }

    /**
     * 
     * @param <T> type of the list elements
     * @param <U> comparable key to sort by
     * @param list non-empty list whose minimum element's index we want
     * @param lens function to extract the ordering key
     * @return the
     */
    public static <T> int minIndex(List<T> list, Function<T, Integer> lens) {
        if(list.isEmpty()) throw new InvalidParameterException("Empty list provided");
        return IntStream.range(0, list.size()).boxed()
                        .min(Comparator.comparing(lens.compose(list::get)))
                        .get();
    }

    /**
     * 
     * @param <T>
     * @param list
     * @param predicate a function (index, element) -> boolean
     * @return true <-> the predicate holds for all (index, element) in list
     */
    public static <T> boolean forall(List<T> list, BiPredicate<Integer, T> predicate) {
        return IntStream
            .range(0, list.size())
            .allMatch(i -> predicate.test(i, list.get(i)));
    }
}
