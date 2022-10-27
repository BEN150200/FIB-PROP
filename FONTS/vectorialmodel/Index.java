package vectorialmodel;

import java.util.stream.Collectors;

import helpers.Functional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class Index<DocId> {

    public HashMap<String, HashMap<DocId, Set<Integer>>> invertedIndex; // String -> docId -> [position]
    public HashMap<DocId, HashMap<String, Set<Integer>>> directIndex;   // docId -> String -> [position]
    
    private Index(HashMap<String, HashMap<DocId, Set<Integer>>> invertedIndex, HashMap<DocId, HashMap<String, Set<Integer>>> directIndex) {
        this.invertedIndex = invertedIndex;
        this.directIndex = directIndex;
    }

    public static <DocId> Index<DocId> empty() {
        return null;
    }

    public static Function<Map.Entry<String, Set<Integer>>, Set<Integer>> positions = Map.Entry::getValue;

    // TODO: construct inverted and direct files
    public static <DocId> Index<DocId> of(Map<DocId, List<String>> collection) {
        return null;
    }

    /**
     * @param term term whose posting list we want to retrieve
     * @return a mapping docId -> [positions] forall documents containing the String parameter
     */
    public HashMap<DocId, Set<Integer>> postingList(String term) {
        return invertedIndex.get(term);
    }
    
    /**
     * @param docId id of the documents whose term frequencies we wish to retrieve
     * @return a mapping term -> [positions] forall terms of the document identified by docId
     */
    public HashMap<String, Set<Integer>> termsPositions(DocId docId) {
        return directIndex.get(docId);
    }

    /**
     * @param docId id of the documents whose term frequencies we wish to retrieve
     * @return a mapping term -> [positions] forall terms of the document identified by docId
     */
    public HashMap<String, Integer> termsFrequencies(DocId docId) {
        return directIndex.get(docId).entrySet()
        .stream().parallel()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            positions.andThen(Set::size),
            Functional::firstKey,
            HashMap::new
        ));
    }
}
