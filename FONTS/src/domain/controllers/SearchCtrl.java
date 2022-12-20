package src.domain.controllers;

import src.domain.core.Document;
import src.domain.core.DocumentInfo;
import src.domain.expressions.ExpressionTreeNode;
import src.domain.indexing.core.IndexingController;

import static src.helpers.Functional.value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import io.vavr.collection.HashSet;
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

    public ArrayList<DocumentInfo> similarDocumentsSearch(String titleName, String authorName, Integer k) {
        Integer docId = DocumentCtrl.getInstance().getDocumentID(titleName, authorName);
        if(docId != null) {
            Either<String, HashMap<Integer, Double>> similar = indexingCtrl.querySimilarDocuments(docId);
            if (similar.isLeft()) {
                return null;
            }
            else {
                if (k == 0) k = 5; //default
                LinkedHashMap<Integer, Double> sortedMap = new LinkedHashMap<>();
                ArrayList<Double> list = new ArrayList<Double>();
                for (Entry<Integer, Double> entry : similar.get().entrySet()) {
                    if (entry.getKey() != docId) list.add(entry.getValue());
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
                ArrayList<DocumentInfo> docsInfo = new ArrayList<>(); 
                sortedMap.forEach((id, value) -> {
                    DocumentInfo docInf = DocumentCtrl.getInstance().getDocument(id).getInfo();
                    docInf.setSimilarity(value);
                    docsInfo.add(docInf);
                });

                return docsInfo;
            } 
        }

        return null;
    }

    public ArrayList<DocumentInfo> storedBooleanExpressionSearch (String boolExpName) {
        if(BooleanExpressionCtrl.getInstance().existsBooleanExpression(boolExpName)) {
            ExpressionTreeNode root = BooleanExpressionCtrl.getInstance().getSavedExpressionTree(boolExpName);
            return booleanExpressionSearch(root);
        }
        else return null;
    }

    public ArrayList<DocumentInfo> savedBooleanExpressionSearch (String name) {
        ExpressionTreeNode root = BooleanExpressionCtrl.getInstance().getSavedExpressionTree(name);
        return booleanExpressionSearch(root);
    }
    public ArrayList<DocumentInfo> tempBooleanExpressionSearch (String boolExp) throws Exception {
        ExpressionTreeNode root = BooleanExpressionCtrl.getInstance().createExpressionTree(boolExp);
        return booleanExpressionSearch(root);
    }

    private ArrayList<DocumentInfo> booleanExpressionSearch(ExpressionTreeNode root) {
        HashSet<Integer> sentencesID = indexingCtrl.booleanQuery(root);
        java.util.HashSet<Document> docs = new java.util.HashSet<Document>();

        sentencesID.forEach(sentenceID -> {
            docs.addAll(DocumentCtrl.getInstance().getDocuments(
                SentenceCtrl.getInstance().sentenceById(sentenceID).getAllDocsID()));
        });
        ArrayList<DocumentInfo> docsInfo = new ArrayList<>();
        docs.forEach(doc -> {
            docsInfo.add(doc.getInfo());
        });
        return docsInfo;
    }

    @SuppressWarnings("deprecation")

    public Either<String, ArrayList<DocumentInfo>> documentsByQuery(String query) {
        return indexingCtrl
            .weightedQuery(query)
            .map(
                results
                ->
                results.map(value
                (
                    (docId, similarity) -> DocumentCtrl.getInstance()
                        .getDocument(docId)
                        .getInfo().withSimilarity(similarity)
                ))
                .values()
                .collect(Collectors.toCollection(ArrayList::new))
            );
    }

    public void addDocument(Integer docId, Iterable<String> content) {
        indexingCtrl.addDocument(docId, content);
    }

    public void addSentence(Integer sentenceId, Iterable<String> content) {
        indexingCtrl.addSentence(sentenceId, content);
    }
    
    public void removeDocument(Integer docId) {
        indexingCtrl.removeDocument(docId);
    }

    public void removeSentence(Integer sentenceId) {
        indexingCtrl.removeSentence(sentenceId);
    }
    
    public void clear(){
        indexingCtrl = new IndexingController<Integer, Integer>();
    }
}
