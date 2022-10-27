package vectorialmodel;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import helpers.Functional;

public class BooleanModel<SentenceId> {

    Index<SentenceId> index;

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
        var Ds = set.stream().map(this::queryTerm).toList();
        var D = Ds.stream().min(Comparator.comparing(Set::size)).orElseGet(Set::of);
        return D.stream()
                .filter(d -> Ds.stream().filter(Di -> !Di.equals(D))
                                .allMatch(Di -> Di.contains(d)))
                .collect(Collectors.toSet());
    }

    /**
     * @param sequence sequence of terms to search
     * @return ids of sentences containing all the terms of the sequence in order
     */
    public Set<SentenceId> querySequence(List<String> sequence) {
        var Ds = sequence.stream().map(index::postingList).toList();
        var m = IntStream.range(0, Ds.size()).boxed()
                        .min(Comparator.comparing(i -> Ds.get(i).size()))
                        .orElse(-1);

        if(m == -1) return Set.<SentenceId>of();

        var Dm = Ds.get(m);

        return Dm.entrySet()
            .stream().parallel()
            .map(entry -> {
                var d = entry.getKey();
                var P = entry.getValue();

                return Map.entry
                (
                    d,
                    P.stream().parallel()
                        .filter(pos -> Ds.stream().filter(Di -> !Di.equals(Dm))
                                                .allMatch(Di -> Di.get(d).contains(pos + Ds.indexOf(Di) - m))
                        )
                        .collect(Collectors.toSet())
                );
            })
            .filter(entry -> !entry.getValue().isEmpty())
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
    }
    
    /**
     * 
     * @return set of all the sentences ids
     */
    public Set<SentenceId> all() {
        return index.directIndex.keySet();
    }
    
}
