package vectorialmodel;

import java.util.stream.Collectors;

import helpers.Functional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Index<DocId> {

    HashMap<String, HashMap<DocId, int[]>> invertedIndex; // String -> docId -> [position]
    HashMap<DocId, HashMap<String, int[]>> directIndex;   // docId -> String -> [position]
    
    private Index(HashMap<String, HashMap<DocId, int[]>> invertedIndex, HashMap<DocId, HashMap<String, int[]>> directIndex) {
        this.invertedIndex = invertedIndex;
        this.directIndex = directIndex;
    }

    public static <DocId> Index<DocId> empty() {
        return null;
    }

    // TODO: construct inverted and direct files
    public static <DocId> Index<DocId> of(Map<DocId, List<String>> collection) {
        return null;
    }

    /**
     * @param String String whose posting list we want to retrieve
     * @return a mapping docId -> [positions] forall documents containing the String parameter
     */
    public HashMap<DocId, int[]> postingList(String String) {
        return invertedIndex.get(String);
    }

    /**
     * @param docId id of the documents whose String frequencies we wish to retrieve
     * @return a mapping String -> [positions] forall Strings of the document identified by docId
     */
    public HashMap<String, int[]> termPositions(DocId docId) {
        return directIndex.get(docId);
    }

    /**
     * @param docId id of the documents whose String frequencies we wish to retrieve
     * @return a mapping String -> [positions] forall Strings of the document identified by docId
     */
    public HashMap<String, Integer> termFrequencies(DocId docId) {
        return directIndex.get(docId).entrySet()
        .stream().parallel()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> entry.getValue().length,
            Functional::firstKey,
            HashMap::new
        ));
    }
}
