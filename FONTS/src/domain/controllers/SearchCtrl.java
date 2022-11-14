package src.domain.controllers;

import src.domain.DocumentInfo;
import src.domain.core.Document;
import src.domain.expressions.ExpressionTreeNode;
import src.domain.indexing.core.IndexingController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

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
                    docInf.setSemblance(value);
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

    public ArrayList<DocumentInfo> tempBooleanExpressionSearch (String boolExp) {
        ExpressionTreeNode root = BooleanExpressionCtrl.getInstance().createExpressionTree(boolExp);
        if (root != null) {
            return booleanExpressionSearch(root);
        }
        else return null;
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

    public ArrayList<DocumentInfo> documentsByQuery(String query) {
        Either<String, java.util.HashMap<Integer, Double>> resultsId = indexingCtrl.weightedQuery(query);
        ArrayList<Document> docs = DocumentCtrl.getInstance().getDocuments(resultsId.get().keySet());
        ArrayList<DocumentInfo> docsInfo = new ArrayList<DocumentInfo>();
        for(Document d: docs){
            docsInfo.add(d.getInfo());
        }
        return docsInfo;
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
