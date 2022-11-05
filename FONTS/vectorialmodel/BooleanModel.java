package vectorialmodel;

import java.util.Collection;
import java.util.Collections;

import io.vavr.Tuple;
import io.vavr.collection.Array;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
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
     * @param term term to search
     * @return ids of sentences containing term
     */
    public java.util.Set<SentenceId> queryTerm(String term) {
        return queryTermVavr(term).toJavaSet();
    }
    
    /**
     * 
     * @param term
     * @return a vavr Set of the sentenceIds containing term
     */
    public Set<SentenceId> queryTermVavr(String term) {
        return index.postingList(term).keySet();
    }
    
    /**
     * 
     * @param set collection of terms to search
     * @return ids of sentences containing all the terms in set
     */
    public java.util.Set<SentenceId> querySet(Collection<String> set) {
        return querySetVavr(set).toJavaSet();
    }

    /**
     * @cost O(L*S), where S == set.size(), L == min{ doc frequency foreach term in set }
     * @param set
     * @return a vavr Set of the sentenceIds containing all the terms in set
     */
    @SuppressWarnings("deprecation")
    public Set<SentenceId> querySetVavr(Collection<String> set) {
        var Ds = Array.ofAll(set).map(this::queryTermVavr);
        return Ds.minBy(Set::size) // compare all using the smallest set
            .map(Dmin ->  Dmin.filter(d -> Ds.forAll(Di -> Di.contains(d))))
            .getOrElse(HashSet::empty);
    }

    /**
     * @cost O(L*S), where S == sequence.size(), L == min{ doc frequency foreach term in sequence }
     * @param sequence sequence of terms to search
     * @return ids of sentences containing all the terms of the sequence in order
     */
    @SuppressWarnings("deprecation") // forall is getting de-deprecated!
    public java.util.Set<SentenceId> querySequence(java.util.List<String> sequence) {

        var postingLists = Array.ofAll(sequence).map(index::postingList);

        return postingLists.minBy(HashMap::size).map(
            minList -> {
                int i = postingLists.indexOf(minList);
                return minList.map((d, P) -> Tuple.of(d,
                    P.filter(
                        pos -> postingLists.zipWithIndex() // tuples (_1: postingList, _2: index)
                        .forAll(t -> t._1.get(d).getOrElse(HashSet::empty).contains(pos + t._2 - i))
                    ))
                )
                .filterValues(HashSet::nonEmpty)
                .keySet().toJavaSet();
            }
        )
        .getOrElse(Collections::emptySet);
    }
    
    /**
     * 
     * @return set of all the sentences ids
     */
    public java.util.Set<SentenceId> all() {
        return this.index.allDocIds().toJavaSet();
    }
    
}
