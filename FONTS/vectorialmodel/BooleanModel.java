package vectorialmodel;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import helpers.Lists;

import static helpers.Maps.*;

public class BooleanModel<SentenceId> {

    private Index<SentenceId> index;

    private BooleanModel(Index<SentenceId> index) {
        this.index = index;
    }

    /**
     * 
     * @param <SentenceId>
     * @return an empty BooleanModel
     */
    public static <SentenceId> BooleanModel<SentenceId> empty() {
        return new BooleanModel<SentenceId>(Index.empty());
    }

    public static <SentenceId> BooleanModel<SentenceId> of(Index<SentenceId> index) {
        return new BooleanModel<SentenceId>(index);
    }

    public static <SentenceId> BooleanModel<SentenceId> of(Map<SentenceId, List<String>> collection) {
        return new BooleanModel<SentenceId>(
            Index.of(collection)
        );
    }

    /**
     * 
     * @param term term to search
     * @return ids of sentences containing term
     */
    public Set<SentenceId> queryTerm(String term) {
        return index.postingList(term).keySet();
    }
    
    /**
     * 
     * @param set collection of terms to search
     * @return ids of sentences containing all the terms in set
     */
    public Set<SentenceId> querySet(Collection<String> set) {
        return querySetStream(set).collect(Collectors.toSet());
    }

    public Stream<SentenceId> querySetStream(Collection<String> set) {
        var Ds = set.stream().map(this::queryTerm).toList(); // sets of docids
        var Dmin = Ds.stream().min(Comparator.comparing(Set::size)).orElseGet(Set::of); // smallest set
        return Dmin.stream()
                .filter(d -> Ds.stream().allMatch(Di -> Di.contains(d)));
    }

    /**
     * @param sequence sequence of terms to search
     * @return ids of sentences containing all the terms of the sequence in order
     */
    public Set<SentenceId> querySequence(List<String> sequence) {
        return querySequenceStream(sequence).collect(Collectors.toSet());
    }

    public Stream<SentenceId> querySequenceStream(List<String> sequence) {
        var Ds = sequence.stream().map(index::postingList).toList();
        
        if(Ds.isEmpty()) return Stream.empty();

        int m = Lists.minIndex(Ds, HashMap::size);
        var Dm = Ds.get(m);

        return Dm.entrySet()
            .stream().parallel()
            .map(value(
                (d, P) -> P.stream().parallel()
                 .filter(pos ->
                    Lists.forall(
                        Ds,
                        (i, Di) -> Di.get(d).contains(pos + i - m)
                    ))
            ))
            .filter(entry -> entry.getValue().count() > 0)
            .map(Map.Entry::getKey);
    }
    
    /**
     * 
     * @return set of all the sentences ids
     */
    public Set<SentenceId> all() {
        return index.directIndex.keySet();
    }
    
}
