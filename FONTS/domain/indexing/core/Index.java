package domain.indexing.core;

import java.util.function.Function;

import helpers.Maps;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import io.vavr.collection.Stream;
import io.vavr.control.Option;

public class Index<DocId> {

    public HashMap<String, HashMap<DocId, HashSet<Integer>>> invertedIndex; // String -> docId -> [position]
    public HashMap<DocId, HashMap<String, HashSet<Integer>>> directIndex;   // docId -> String -> [position]
    
    public Index(HashMap<String, HashMap<DocId, HashSet<Integer>>> invertedIndex, HashMap<DocId, HashMap<String, HashSet<Integer>>> directIndex) {
        this.invertedIndex = invertedIndex;
        this.directIndex = directIndex;
    }

    /**
     * 
     * @param <DocId>
     * @return an empty Index
     */
    public static <DocId> Index<DocId> empty() {
        return new Index<DocId>(
            HashMap.empty(),
            HashMap.empty()
        );
    }

    /**
     * @implNote invertedIndex computed using MapReduce paradigm:
     *      1. MAP: compute partial inverted indices for each document,
     *      2. REDUCE: merge all partial indices
     * @param <DocId>
     * @param collection
     * @return a new Index representing the given collection
     */
    public static <DocId> Index<DocId> of(HashMap<DocId, Iterable<String>> collection) {
        var directIndex = collection.mapValues(Index::termsPositions);

        var invertedIndex = directIndex.map(
            (docId, termsPositions) -> Tuple.of(docId, termsPositions.foldLeft(
                HashMap.<String, HashMap<DocId, HashSet<Integer>>>empty(),
                (inverted, termPositions) -> inverted.merge(
                    HashMap.of(termPositions._1, HashMap.of(docId, termPositions._2)),
                    Maps::unionMerge
                )
            ))
        )
        .values().reduceOption(Maps::nestedUnionMerge).getOrElse(HashMap::empty);

        return new Index<DocId>(invertedIndex, directIndex);
    }

    private static HashMap<String, HashSet<Integer>> termsPositions(Iterable<String> content) {
        return Stream.ofAll(content)
            .zipWithIndex()
            .foldLeft(
                HashMap.<String, HashSet<Integer>>empty(),
                (positions, termIndex) -> positions.merge(
                    HashMap.of(termIndex._1, HashSet.of(termIndex._2)),
                    HashSet::union
                )
            );
    }

    /**
     * @param term term whose posting list we want to retrieve
     * @return a mapping docId -> [positions] forall documents containing the String parameter (or empty, if there are none)
     */
    public HashMap<DocId, HashSet<Integer>> postingList(String term) {
        return this.invertedIndex.get(term).getOrElse(HashMap::empty);
    }

    /**
     * 
     * @param term
     * @return ids of documents containing term
     */
    public Set<DocId> documents(String term) {
        return this.postingList(term).keySet();
    }
    
    /**
     * 
     * @param term
     * @return the number of documents containing term
     */
    public int documentFrequency(String term) {
        return this.documents(term).size();
    }

    /**
     * 
     * @param docId
     * @return true iff this contains the given docId
     */
    public boolean contains(DocId docId) {
        return this.directIndex.containsKey(docId);
    }

    /**
     * @param docId id of the documents whose term frequencies we wish to retrieve
     * @return if present, a mapping term -> [positions] forall terms of the document identified by docId. Throws if docId doesn't exist! (an empty document is still a document)
     */
    public Option<HashMap<String, HashSet<Integer>>> termsPositions(DocId docId) {
        return this.directIndex.get(docId);
    }

    /**
     * @param docId id of the documents whose term frequencies we wish to retrieve
     * @return if present, a mapping term -> [positions] forall terms of the document identified by docId. Throws if docId doesn't exist! (an empty document is still a document)
     */
    public Option<Set<String>> terms(DocId docId) {
        return this.termsPositions(docId).map(HashMap::keySet);
    }

    /**
     * @param docId id of the documents whose term frequencies we wish to retrieve
     * @return if present, a mapping term -> [positions] forall terms of the document identified by docId. Throws if docId doesn't exist! (an empty document is still a document)
     */
    public Option<HashMap<String, Integer>> termsFrequencies(DocId docId) {
        return this.termsPositions(docId).map(tv -> tv.mapValues(HashSet::size));
    }

    /**
     * 
     * @param docId
     * @return if present, a mapping term -> document_frequency forall terms of the document
     */
    @SuppressWarnings("deprecation")
    public Option<HashMap<String, Integer>> termsDocumentFrequencies(DocId docId) {
        return this.terms(docId).map(terms -> (HashMap<String, Integer>) terms.toMap(
            term -> term,
            this::documentFrequency
        ));
    }

    
    /**
     * 
     * @return number of documents in the index
     */
    public int documentsCount() {
        return this.directIndex.size();
    }
    
    /**
     * 
     * @return Set of all docIds in the index
     */
    public HashSet<DocId> allDocIds() {
        return (HashSet<DocId>) this.directIndex.keySet();
    }
    
    /**
     * 
     * @param docId id of the document to be inserted
     * @param content content of the document to be inserted
     * @return a new index with the document inserted. If it previously existed, it is replaced
     */
    public Index<DocId> insert(DocId docId, Iterable<String> content) {

        var termsPositions = Index.termsPositions(content);
                    
        var newDirectIndex = this.directIndex.put(docId, termsPositions);
        
        // remove all terms of previous docId, if any
        var removedInverted = this.directIndex.get(docId).map(
            doc -> doc.keySet().foldLeft(
                this.invertedIndex,
                (inverted, term) -> Maps.nestedRemove(term, docId, inverted)
            )
        )
        .getOrElse(this.invertedIndex);
                        
                        
        var newInvertedIndex = termsPositions
            .foldLeft(
                removedInverted,
                (inverted, termPositions) -> inverted.merge(
                    HashMap.of(termPositions._1, HashMap.of(docId, termPositions._2)),
                    HashMap::merge
                )
            );
            
            return new Index<DocId>(newInvertedIndex, newDirectIndex);
    }

    /**
     * 
     * @param docIdContent tuple of docId and content to insert
     * @return a new index with the document inserted. If it previously existed, it is replaced
     */
    public Index<DocId> insert(Tuple2<DocId, Iterable<String>> docIdContent) {
        return this.insert(docIdContent._1, docIdContent._2);
    }

    public Index<DocId> insert(DocId docId, String... content) {
        return this.insert(docId, Stream.of(content));
    }

    /**
     * 
     * @param docId
     * @return a new index with docId (and its term occurrences) removed (if it existed)
     */
    public Index<DocId> remove(DocId docId) {
        return this.contains(docId)
            ? new Index<DocId>(
                this.directIndex.get(docId).map(
                    doc -> doc.keySet().foldLeft(
                        this.invertedIndex,
                        (inverted, term) -> Maps
                            .nestedRemove(term, docId, inverted)
                    )
                ).getOrElse(this.invertedIndex),
                this.directIndex.remove(docId)
            )
            : this;
    }

    @Override
    public String toString() {
        return Index.print(this);
    }

    public static <DocId> String print(Index<DocId> index) {
        return "InvertedIndex {\n" +
            index.invertedIndex.mkString("\t", "\n\t", "") +
            "\n},\nDirectIndex {\n" +
            index.directIndex.mkString("\t", "\n\t", "") +
            "\n}";
    }

    @Override
    @SuppressWarnings({"unchecked", "deprecation"})
    public boolean equals(Object obj) {
        if(!(this.getClass().isInstance(obj))) return false;

        var other = (Index<DocId>) obj;

        return this.directIndex.eq(other.directIndex) && this.invertedIndex.eq(other.invertedIndex);
    }
}