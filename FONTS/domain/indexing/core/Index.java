package domain.indexing.core;

import java.security.InvalidParameterException;
import java.util.function.Function;

import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;

public class Index<DocId> {

    public HashMap<String, HashMap<DocId, HashSet<Integer>>> invertedIndex; // String -> docId -> [position]
    public HashMap<DocId, HashMap<String, HashSet<Integer>>> directIndex;   // docId -> String -> [position]
    
    private Index(HashMap<String, HashMap<DocId, HashSet<Integer>>> invertedIndex, HashMap<DocId, HashMap<String, HashSet<Integer>>> directIndex) {
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
     * 
     * @param <DocId>
     * @param collection
     * @return a new Index representing the given collection
     */
    public static <DocId> Index<DocId> of(Map<DocId, Iterable<String>> collection) {
        return collection.foldLeft(
            Index.empty(),
            Index::insert
        );
    }

    /**
     * @param term term whose posting list we want to retrieve
     * @return a mapping docId -> [positions] forall documents containing the String parameter (or empty, if there are none)
     */
    public HashMap<DocId, HashSet<Integer>> postingList(String term) {
        return invertedIndex.get(term).getOrElse(HashMap::empty);
    }

    public Set<DocId> documents(String term) {
        return this.postingList(term).keySet();
    }
    
    /**
     * @param docId id of the documents whose term frequencies we wish to retrieve
     * @return a mapping term -> [positions] forall terms of the document identified by docId. Throws if docId doesn't exist! (an empty document is still a document)
     */
    public HashMap<String, HashSet<Integer>> termsPositions(DocId docId) {
        return directIndex.get(docId).getOrElseThrow(() -> new InvalidParameterException("DocId " + docId + " is not in the index"));
    }

    /**
     * @param docId id of the documents whose term frequencies we wish to retrieve
     * @return a mapping term -> [positions] forall terms of the document identified by docId. Throws if docId doesn't exist! (an empty document is still a document)
     */
    public Set<String> terms(DocId docId) {
        return this.termsPositions(docId).keySet();
    }

    /**
     * @param docId id of the documents whose term frequencies we wish to retrieve
     * @return a mapping term -> [positions] forall terms of the document identified by docId. Throws if docId doesn't exist! (an empty document is still a document)
     */
    public HashMap<String, Integer> termsFrequencies(DocId docId) {
        return this.termsPositions(docId).mapValues(HashSet::size);
    }

    public int documentFrequency(String term) {
        return this.invertedIndex.get(term).get().size();
    }

    /**
     * 
     * @param docId
     * @return a mapping term -> document_frequency forall terms of the document. Throws if docId doesn't exist! (an empty document is still a document)
     */
    @SuppressWarnings("deprecation") // de-deprecated, not released yet
    public HashMap<String, Integer> termsDocumentFrequencies(DocId docId) {
        return (HashMap<String, Integer>) this.terms(docId).toMap(
            Function.identity(),
            this::documentFrequency
        );
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
    public Set<DocId> allDocIds() {
        return this.directIndex.keySet();
    }
    
    /**
     * 
     * @param docId id of the document to be inserted
     * @param content content of the document to be inserted
     * @return a new index with the document inserted. If it previously existed, it is replaced
     */
    public Index<DocId> insert(DocId docId, Iterable<String> content) {
        // compute positions
        var termsPositions = List.ofAll(content)
            .zipWithIndex()
            .foldLeft(HashMap.<String, HashSet<Integer>>empty(),
                (positions, termIndex) -> positions.merge(
                    HashMap.of(termIndex._1, HashSet.of(termIndex._2)),
                    HashSet::union
                    )
                    );
                    
                    var newDirectIndex = this.directIndex.put(docId, termsPositions);
                    
                    // remove if previously existing
                    var removedInverted = this.directIndex.get(docId).map(
                        doc -> doc.keySet().foldLeft(this.invertedIndex, HashMap::remove)
                        )
                        .getOrElse(this.invertedIndex);
                        
                        
        // add to inverted file
        var newInvertedIndex = termsPositions
        .foldLeft(removedInverted, (inverted, termPositions) -> inverted.merge(
            HashMap.of(termPositions._1, HashMap.of(docId, termPositions._2)),
            HashMap::merge
            ));
            
            return new Index<DocId>(newInvertedIndex, newDirectIndex);
    }

    public Index<DocId> insert(Tuple2<DocId, Iterable<String>> docIdContent) {
        return this.insert(docIdContent._1, docIdContent._2);
    }

    /**
     * 
     * @param docId
     * @return a new index with docId (and its term occurrences) removed (if it existed)
     */
    public Index<DocId> remove(DocId docId) {
        return this.allDocIds().contains(docId)
            ? new Index<DocId>(
                this.invertedIndex.removeAll(this.terms(docId)),
                this.directIndex.remove(docId)
            )
            : this;
    }
    
    public static <DocId> String print(Index<DocId> index) {
        return "InvertedIndex {\n" +
            index.invertedIndex.mkString("\t", "\n\t", "") +
            "},\nDirectIndex {\n" +
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