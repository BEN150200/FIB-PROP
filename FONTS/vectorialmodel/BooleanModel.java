package vectorialmodel;

import java.util.Collection;
import java.util.Collections;

import io.vavr.Tuple;
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
     * @param sentenceId
     * @param content
     * @return a new BooleanModel with the document inserted. If it previously existed, it is replaced
     */
    public BooleanModel<SentenceId> insert(SentenceId sentenceId, Iterable<String> content) {
        var newIndex = this.index.insert(sentenceId, content);
        return BooleanModel.of(newIndex);
    }

    /**
     * 
     * @param sentenceId
     * @return a new BooleanModel with the sentenceId (and its term occurrences) removed (if it existed)
     */
    public BooleanModel<SentenceId> remove(SentenceId sentenceId) {
        var newIndex = this.index.remove(sentenceId);
        return BooleanModel.of(newIndex);
    }

    /**
     * 
     * @param term term to search
     * @return ids of sentences containing term
     */
    public java.util.Set<SentenceId> queryTerm(String term) {
        return queryTermVavr(term).toJavaSet();
    }

    private Set<SentenceId> queryTermVavr(String term) {
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

    @SuppressWarnings("deprecation")
    public Set<SentenceId> querySetVavr(Collection<String> set) {
        var Ds = io.vavr.collection.Array.ofAll(set).map(this::queryTermVavr);
        var Dmin = Ds.<Integer>minBy(Set::size).getOrElse(HashSet::empty);
        return Dmin.filter(d -> Ds.forAll(Di -> Di.contains(d)));
    }

    /**
     * @param sequence sequence of terms to search
     * @return ids of sentences containing all the terms of the sequence in order
     */
    @SuppressWarnings("deprecation") // forall is getting de-deprecated!
    public java.util.Set<SentenceId> querySequence(java.util.List<String> sequence) {

        var Ds = io.vavr.collection.Array.ofAll(sequence).map(index::postingList);

        return Ds.minBy(HashMap::size).map(
            Dmin -> Dmin.map((d, P) -> Tuple.of(d,
                    P.filter(
                        pos -> Ds.zipWithIndex()
                        .forAll(t -> t._1.containsKey(d) &&
                                     t._1.get(d).get().contains(pos + t._2 - Ds.indexOf(Dmin)))
                    ))
                )
                .filterValues(HashSet::nonEmpty)
                .keySet().toJavaSet()
        )
        .getOrElse(Collections::emptySet);
    }
    
    /**
     * 
     * @return set of all the sentences ids
     */
    public java.util.Set<SentenceId> all() {
        return index.directIndex.keySet().toJavaSet();
    }
    
}
