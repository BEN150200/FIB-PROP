package src.domain.controllers;

import src.domain.core.Document;
import src.domain.core.DocumentInfo;
import src.domain.expressions.ExpressionTreeNode;
import src.domain.indexing.core.IndexingController;

import static src.helpers.Functional.value;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.control.Either;

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

    public CompletableFuture<Either<String, List<DocumentInfo>>> similarDocumentsSearch(String titleName, String authorName) {
        var docId  = DocumentCtrl.getInstance().getDocumentID(titleName, authorName);
        if(docId == null)
            return CompletableFuture.supplyAsync(() -> Either.left("Document " + titleName + " (by " + authorName + ") doesn't exist"));

        return obtainDocuments(docId, indexingCtrl.querySimilarDocuments(docId));

    }

    private CompletableFuture<Either<String, List<DocumentInfo>>> obtainDocuments(int docId, CompletableFuture<Either<String, HashMap<Integer, Double>>> query) {
        return query.thenApply(
            maybeResult -> maybeResult.map(
                result -> result
                .filterKeys(id -> id != docId)
                .map(value
                (
                    (id, sim) -> DocumentCtrl.getInstance()
                    .getDocument(id)
                    .getInfo()
                    .withSimilarity(sim)
                ))
                .values()
                .sortBy(DocumentInfo::getSimilarity)
                .reverse()
                .toJavaList()
            )
        );
    }

    /**
     * 
     * @param boolExpName
     * @return the result of searching the stored expresison, or null if an expression with said name doesn't exist
     */
    public ArrayList<DocumentInfo> storedBooleanExpressionSearch (String boolExpName) {
        if(BooleanExpressionCtrl.getInstance().existsBooleanExpression(boolExpName)) {
            ExpressionTreeNode root = BooleanExpressionCtrl.getInstance().getSavedExpressionTree(boolExpName);
            return booleanExpressionSearch(root);
        }
        else return null;
    }

    /**
     * 
     * @param name
     * @return the result of searching the stored expresison (throws if it doesn't exist)
     */
    public ArrayList<DocumentInfo> savedBooleanExpressionSearch (String name) {
        ExpressionTreeNode root = BooleanExpressionCtrl.getInstance().getSavedExpressionTree(name);
        return booleanExpressionSearch(root);
    }

    /**
     * 
     * @param boolExp a raw boolean expression
     * @return the result of searching the expression
     * @throws Exception if the expression is sintactically incorrect
     */
    public ArrayList<DocumentInfo> booleanExpressionSearch (String boolExp) throws Exception {
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
    public CompletableFuture<Either<String, List<DocumentInfo>>> documentsByQuery(String query) {
        return obtainDocuments(-1, indexingCtrl.weightedQuery(query));
    }

    /**
     * adds a document to the models
     * @param docId
     * @param content
     */
    public void addDocument(Integer docId, Iterable<String> content) {
        indexingCtrl.addDocument(docId, content);
    }

    /**
     * adds a sentence to the models
     * @param sentenceId
     * @param content
     */
    public void addSentence(Integer sentenceId, Iterable<String> content) {
        indexingCtrl.addSentence(sentenceId, content);
    }
    
    /**
     * removes a document to the models
     * @param docId
     */
    public void removeDocument(Integer docId) {
        indexingCtrl.removeDocument(docId);
    }

    /**
     * removes a sentence to the models
     * @param sentenceId
     */
    public void removeSentence(Integer sentenceId) {
        indexingCtrl.removeSentence(sentenceId);
    }
    
    /**
     *  empties the models
     */
    public void clear(){
        indexingCtrl = new IndexingController<Integer, Integer>();
    }
}
