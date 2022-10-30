package DomainLayer.Classes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.text.StyledEditorKit.BoldAction;

import javafx.util.Pair;

public class DocumentCtrl {
    private static DocumentCtrl instance = null;
    private int usedID;

    private HashMap<Pair<String,String>, Integer> documentsID;
    private HashMap<Integer, Document> documents;

    public static DocumentCtrl getInstance() {
        if (instance == null) {
            instance = new DocumentCtrl();
        }
        return instance;
    }

    
    public DocumentCtrl() {
        usedID = 0;
    }

    /**
     * Public Functions
     */

    /**
     * Getters
     */
    
    public boolean existsDocumentID()
    
    public Integer getDocumentID(String t, String a) {
        return documentsID.get(new Pair<String,String>(t,a));
    }

    public Document getDocument(String t, String a) {
        return documents.get(documentsID.get(new Pair<String,String>(t,a)));
    }

    public Document getDocument(int id) {
        Document doc = documents.get(id);
        return doc;
    }

    public Set<Integer> getAllDocsIDs() {
        return new HashSet(documentsID.values());
    }


    public boolean exists(String a, String t) {

        return true;
    }

    /**
     * Setters
     */


    

    
}
