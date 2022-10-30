package vectorialmodel;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import helpers.Maps;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
        return new Index<DocId>(
            new HashMap<String, HashMap<DocId, Set<Integer>>>(),
            new HashMap<DocId, HashMap<String, Set<Integer>>>()
        );
    }

    public static Function<Map.Entry<String, Set<Integer>>, Set<Integer>> positions = Map.Entry::getValue;

    public static <DocId> Index<DocId> of(Map<DocId, List<String>> collection) {
        Index<DocId> index = Index.empty();
        for(var entry : collection.entrySet())
            index.insert(entry.getKey(), entry.getValue());
        return index;
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
            Maps::firstKey,
            HashMap::new
        ));
    }


    public void insert(DocId docId, List<String> content) {
        // remove if previously existing
        if(this.directIndex.containsKey(docId)) {
            for(var entry : directIndex.get(docId).entrySet()) {
                var term = entry.getKey();
                this.invertedIndex.get(term).remove(docId);
            }
        }

        // compute positions
        var termPositions = new HashMap<String, Set<Integer>>();
        for(int i = 0; i < content.size(); i++) {
            var term = content.get(i);
            if(termPositions.containsKey(term))
                termPositions.get(term).add(i);
            else {
                var positions = new HashSet<Integer>();
                positions.add(i);
                termPositions.put(term, positions);
            }
        }

        this.directIndex.put(docId, termPositions);

        // add to inverted file
        for(var entry : termPositions.entrySet()) {
            var term = entry.getKey();
            var positions = entry.getValue();
            
            if(this.invertedIndex.containsKey(term))
                this.invertedIndex.get(term).put(docId, positions);
            else {
                var posting = new HashMap<DocId, Set<Integer>>();
                posting.put(docId, positions);
                this.invertedIndex.put(term, posting);
            }
        }

        
    }

    public static <DocId> String print(Map.Entry<DocId, Set<Integer>> posting) {
        return "(" + posting.getKey() + ", " + posting.getValue() + ")";
    }

    public static <DocId> String print(Map<?, ? extends Map<?, Set<Integer>>> invertedIndex) {
        return invertedIndex.entrySet().stream()
                .map(entry ->
                    "%s : [%s]".formatted(
                        entry.getKey(),
                        entry.getValue().entrySet().stream()
                            .map(Index::print)
                            .collect(Collectors.joining(", "))
                    )
                )
                .collect(Collectors.joining(",\n"));
    }


    public static <DocId> String print(Index<DocId> index) {
        return "InvertedIndex {\n\t%s\n},\nDirectIndex {\n\t%s\n}".formatted(
            print(index.invertedIndex)
                .replace("\n", "\n\t"),
                print(index.directIndex)
                .replace("\n", "\n\t")
        );
    }
}
