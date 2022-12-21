package src.domain.controllers;

import src.domain.core.Document;
import src.domain.core.DocumentInfo;
import src.domain.expressions.ExpressionTreeNode;
import src.domain.indexing.core.IndexingController;

import static src.helpers.Functional.value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.Stream;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import io.vavr.control.Option;

@SuppressWarnings("deprecation")
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

    /**
     * 
     * @param titleName
     * @param authorName
     * @return either the info of the resultsing documents' info (sorted by similarity), or a string describing an error
     */

    // Either<err, id> -> CompletableFuture<Either<err, res>>
    public CompletableFuture<Either<String, List<DocumentInfo>>> similarDocumentsSearch(String titleName, String authorName) {
        var docId  = DocumentCtrl.getInstance().getDocumentID(titleName, authorName);
        if(docId == null)
            return CompletableFuture.supplyAsync(() -> Either.left("Document " + titleName + " (by " + authorName + ") doesn't exist"));

        return indexingCtrl.querySimilarDocuments(docId)
            .thenApply(
                maybeResult -> maybeResult.map(
                    result -> result.map(value
                    (
                        (id, sim) -> DocumentCtrl.getInstance()
                        .getDocument(id)
                        .getInfo()
                        .withSimilarity(sim)
                    ))
                )
                .map(HashMap::values)
                .map(Stream::toJavaList)
            );
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

    /**
     * 
     * @param query
     * @return either the info of the resultsing documents' info (sorted by similarity), or a string describing a query syntax error
     */
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
                .sortBy(DocumentInfo::getSimilarity)
                .reverse()
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
