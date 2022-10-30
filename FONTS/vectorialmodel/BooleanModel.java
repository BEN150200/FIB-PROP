package vectorialmodel;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import helpers.Lists;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;

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

    public static <SentenceId> BooleanModel<SentenceId> of(java.util.Map<SentenceId, java.util.Collection<String>> collection) {
        return new BooleanModel<SentenceId>(
            Index.of(
                HashMap.ofAll(collection)
            )
        );
    }

    /**
     * 
     * @param term term to search
     * @return ids of sentences containing term
     */
    public Set<SentenceId> queryTerm(String term) {
        return index.postingList(term).keySet().toJavaSet();
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

        var Ds = io.vavr.collection.Array.ofAll(sequence).map(index::postingList);
        
        if(Ds.isEmpty()) return Collections.emptySet();

        var Dm = Ds.minBy(HashMap::size).get();
        int m = Ds.indexOf(Dm);

        return Dm.map((d, P) -> Tuple.of(d,
                    P.filter(
                        pos -> Ds.zipWithIndex()
                        .forAll(t -> t._1.get(d).map(positions -> positions.contains(pos + t._2 - m)).getOrElse(false))
                    ))
                )
                .filterValues(HashSet::nonEmpty)
                .keySet().toJavaSet();
    }
    
    /**
     * 
     * @return set of all the sentences ids
     */
    public Set<SentenceId> all() {
        return index.directIndex.keySet().toJavaSet();
    }
    
}
