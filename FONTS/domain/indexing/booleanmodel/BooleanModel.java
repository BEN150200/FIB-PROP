package domain.indexing.booleanmodel;

import java.util.Collection;

import domain.indexing.core.Index;
import io.vavr.Tuple;
import io.vavr.collection.Array;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;


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

    /**
     * 
     * @param <SentenceId>
     * @param index
     * @return a new BooleanModel with the given index
     */
    public static <SentenceId> BooleanModel<SentenceId> of(Index<SentenceId> index) {
        return new BooleanModel<SentenceId>(index);
    }

    /**
     * 
     * @param <SentenceId>
     * @param collection
     * @return a new BooleanModel describing the given collection of sentences
     */
    public static <SentenceId> BooleanModel<SentenceId> of(java.util.Map<SentenceId, java.util.Collection<String>> collection) {
        return new BooleanModel<SentenceId>(
            Index.of(
                HashMap.ofAll(collection)
            )
        );
    }

    /**
     * 
     * @param sentenceId
     * @param content
     * @return a new BooleanModel with the document inserted. If it previously existed, it is replaced
     */
    public BooleanModel<SentenceId> insert(SentenceId sentenceId, Iterable<String> content) {
        return BooleanModel.of(this.index.insert(sentenceId, content));
    }

    /**
     * 
     * @param sentenceId
     * @return a new BooleanModel with the sentenceId (and its term occurrences) removed (if it existed)
     */
    public BooleanModel<SentenceId> remove(SentenceId sentenceId) {
        return BooleanModel.of(this.index.remove(sentenceId));
    }

    /**
     * 
     * @param term
     * @return a vavr Set of the sentenceIds containing term
     */
    public HashSet<SentenceId> queryTerm(String term) {
        return (HashSet<SentenceId>) this.index.postingList(term).keySet();
    }

    /**
     * @cost O(L*S), where S == set.size(), L == min{ doc frequency foreach term in set }
     * @param set
     * @return a vavr Set of the sentenceIds containing all the terms in set
     */
    @SuppressWarnings("deprecation")
    public HashSet<SentenceId> querySet(Collection<String> set) {
        var Ds = List.ofAll(set).map(this::queryTerm);
        return Ds.minBy(Set::size) // compare all using the smallest set
            .map(Dmin ->  Dmin.filter(d -> Ds.forAll(Di -> Di.contains(d))))
            .getOrElse(HashSet::empty);
    }

    /**
     * @cost O(L*S), where S == sequence.size(), L == min{ doc frequency foreach term in sequence }
     * @param sequence sequence of terms to search
     * @return ids of sentences containing all the terms of the sequence in order
     */
    @SuppressWarnings("deprecation")
    public HashSet<SentenceId> querySequence(Iterable<String> sequence) {
        var postingLists = List.ofAll(sequence).map(index::postingList);
        var minListIndex = postingLists.zipWithIndex().minBy(t -> t._1.size());
        return minListIndex.map(
            MI -> {
                var minList = MI._1;
                int i = MI._2;
                return (HashSet<SentenceId>) minList.map((d, P) -> Tuple.of(d,
                    P.filter(
                        pos -> postingLists.zipWithIndex() // tuples (_1: postingList, _2: index)
                        .forAll(t -> t._1.get(d).getOrElse(HashSet::empty).contains(pos + t._2 - i))
                    ))
                )
                .filterValues(HashSet::nonEmpty)
                .keySet();
            }
        )
        .getOrElse(HashSet::empty);
    }
    
    /**
     * 
     * @return set of all the sentences ids
     */
    public HashSet<SentenceId> all() {
        return this.index.allDocIds();
    }
    
}
