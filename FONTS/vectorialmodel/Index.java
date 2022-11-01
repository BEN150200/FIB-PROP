package vectorialmodel;


import java.util.stream.Collectors;

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

    public static <DocId> Index<DocId> empty() {
        return new Index<DocId>(
            HashMap.empty(),
            HashMap.empty()
        );
    }

    public static <DocId> Index<DocId> of(Map<DocId, Iterable<String>> collection) {
        return collection.foldLeft(
            Index.<DocId>empty(),
            Index::insert
        );
    }

    /**
     * @param term term whose posting list we want to retrieve
     * @return a mapping docId -> [positions] forall documents containing the String parameter
     */
    public HashMap<DocId, HashSet<Integer>> postingList(String term) {
        return invertedIndex.get(term).orNull();
    }
    
    /**
     * @param docId id of the documents whose term frequencies we wish to retrieve
     * @return a mapping term -> [positions] forall terms of the document identified by docId
     */
    public HashMap<String, HashSet<Integer>> termsPositions(DocId docId) {
        return directIndex.get(docId).orNull();
    }

    /**
     * @param docId id of the documents whose term frequencies we wish to retrieve
     * @return a mapping term -> [positions] forall terms of the document identified by docId
     */
    public HashMap<String, Integer> termsFrequencies(DocId docId) {
        return directIndex.get(docId).map(
                document ->
                    document.mapValues(HashSet::size)).orNull();
    }

    public Index<DocId> insert(Tuple2<DocId, Iterable<String>> document) {
        return this.insert(document._1, document._2);
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

    private HashMap<String, HashMap<DocId, HashSet<Integer>>> invertedWithout(DocId docId) {
        return this.invertedIndex.removeAll(
            this.directIndex.get(docId)
                .map(HashMap::keySet)
                .getOrElse(HashSet::empty)
        );
    }

    /**
     * 
     * @param docId
     * @return a new index with docId (and its term occurrences) removed (if it existed)
     */
    public Index<DocId> remove(DocId docId) {
        return new Index<DocId>(
            this.invertedWithout(docId),
            this.directIndex.remove(docId)
        );
    }
    
    public static <DocId> String print(Index<DocId> index) {
        return "InvertedIndex {\n%s\n},\nDirectIndex {\n%s\n}".formatted(
            index.invertedIndex.mkString("\t", "\n\t", ""),
            index.directIndex.mkString("\t", "\n\t", "")
        );
    }
}