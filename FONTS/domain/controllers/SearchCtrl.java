package domain.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import domain.DocumentInfo;
import domain.indexing.core.IndexingController;
import io.vavr.control.Either;

public class SearchCtrl {

    private static SearchCtrl instance = null;

    private IndexingController<Integer, Integer> indexingCtrl = new IndexingController<Integer, Integer>();

    /**
     * Constructor
    **/
    public SearchCtrl() {}

    public static SearchCtrl getInstance() {
        if (instance == null) {
            instance = new SearchCtrl();
        }
        return instance;
    }

    public List<DocumentInfo> searchSimilarDocuments(String titleName, String authorName, Integer k) {
        Integer docId = DocumentCtrl.getInstance().getDocumentID(titleName, authorName);
        if(docId != null) {
            Either<String, HashMap<Integer, Double>> similar = indexingCtrl.querySimilarDocuments(docId);
            if (similar.isLeft()) {
                return null;
            }
            else {
                LinkedHashMap<Integer, Double> sortedMap = new LinkedHashMap<>();
                ArrayList<Double> list = new ArrayList<Double>();
                for (Entry<Integer, Double> entry : similar.get().entrySet()) {
                    list.add(entry.getValue());
                }
                Collections.sort(list);

                int numDocs;
                if (list.size() < k) {
                    numDocs = list.size();
                }
                else numDocs = k;

                for (int i = list.size()-1; i >= list.size() - numDocs; --i) {
                    Double num = list.get(i);
                    for (Entry<Integer, Double> entry : similar.get().entrySet()) {
                        if(entry.getValue().equals(num)) {
                            sortedMap.put(entry.getKey(), num);
                        }
                    }
                }
                List<DocumentInfo> docInfo = new ArrayList<>(); 
                sortedMap.forEach((id, value) -> {
                    docInfo.add(DocumentCtrl.getInstance().getDocument(id).getInfo());
                });

                return docInfo;
            } 
        }

        return null;
    }

    public Set<Integer> booleanQuery(String boolExpName) {
        return null;
    }


    
    public void addDocument(Integer docId, Iterable<String> content) {
        indexingCtrl.addDocument(docId, content);
    }

    public void addSentence(Integer sentenceId, Iterable<String> content) {
        indexingCtrl.addSentence(sentenceId, content);
    }
    
    public void removeDocument(Integer docId) {
        indexingCtrl.removeSentence(docId);
    }

    public void removeSentence(Integer sentenceId) {
        indexingCtrl.removeSentence(sentenceId);
    }
    
}
