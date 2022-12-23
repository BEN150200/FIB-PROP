package src.domain.indexing.booleanmodel;

import src.domain.expressions.ExpressionTreeNode;
import src.domain.indexing.core.Index;

import static src.helpers.Functional.*;

import java.util.Arrays;
import java.util.Collection;

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
     * @return a new empty model
     */
    public static <SentenceId> BooleanModel<SentenceId> empty() {
        return new BooleanModel<SentenceId>(Index.empty());
    }

    /**
     * 
     * @return a new model associated to the given index
     */
    public static <SentenceId> BooleanModel<SentenceId> of(Index<SentenceId> index) {
        return new BooleanModel<SentenceId>(index);
    }

    /**
     * 
     * @return a new model with the sentence and its content inserted
     */
    public BooleanModel<SentenceId> insert(SentenceId sentenceId, Iterable<String> content) {
        return BooleanModel.of(this.index.insert(sentenceId, content));
    }

    /**
     * @return a new model with the sentence removed
     */
    public BooleanModel<SentenceId> remove(SentenceId sentenceId) {
        return BooleanModel.of(this.index.remove(sentenceId));
    }

    private HashSet<SentenceId> queryTerm(String term) {
        return (HashSet<SentenceId>) this.index.postingList(term).keySet();
    }

    @SuppressWarnings("deprecation")
    private HashSet<SentenceId> querySet(Collection<String> set)
    {
        //System.out.println("corxetes   " + set.toString());
        var sets = List.ofAll(set).map(this::queryTerm);
        return sets
            .minBy(Set::size)
            .map
            (
                Dmin -> Dmin.filter
                (
                    d -> sets.forAll(Di -> Di.contains(d))
                )
            )
            .getOrElse(HashSet::empty);
    }

    @SuppressWarnings("deprecation")
    private HashSet<SentenceId> querySequence(Iterable<String> sequence)
    {
        var postingLists = List.ofAll(sequence).map(index::postingList);

        return postingLists
            .zipWithIndex()
            .minBy(pair((list, idx) -> list.size()))
            .map(
                pair((minList, i)
                ->
                (HashSet<SentenceId>)
                minList
                .map(
                    value((id, positions)
                    -> 
                    positions.filter(
                        pos
                        ->
                        postingLists
                            .zipWithIndex()
                            .forAll(
                                pairP((list, j)
                                ->
                                list.get(id)
                                .getOrElse(HashSet::empty)
                                .contains(pos + j - i)))
                    ))
                )
                .filterValues(HashSet::nonEmpty)
                .keySet()
            ))
            .getOrElse(HashSet::empty);
    }

    private HashSet<SentenceId> all() {
        return this.index.allDocIds();
    }

    private Collection<String> parse(String terms) {
        return Arrays.asList(terms.substring(1, terms.length()-1).split(" "));
    } 

    /**
     * 
     * @param root the root of an expression tree to search
     * @return a set of the sentence ids matching the expression
     */
    public HashSet<SentenceId> query(ExpressionTreeNode root) {
        if(root == null) return HashSet.empty();
        
        var value = root.getValue();

        switch (value.charAt(0)) {
            case '&':
                return query(root.getLeft()).intersect(query(root.getRight()));
            case '|':
                return query(root.getLeft()).union(query(root.getRight()));
            case '!':
                return all().diff(query(root.getRight()));
            case '{':
                return querySet(parse(value));
            case '"':
                return querySequence(parse(value));
            default:
                return queryTerm(value);
        }
    }
}
