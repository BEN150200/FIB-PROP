package DomainLayer.Classes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javafx.util.Pair;

public class DocumentCtrl {
    private static DocumentCtrl instance = null;
    private int usedID;

    private HashMap<Pair<String,String>, Document> documents;

    public static DocumentCtrl getInstance() {
        if (instance == null) {
            instance = new DocumentCtrl();
        }
        return instance;
    }

    public DocumentCtrl() {
        usedID = 0;
    }

    

    public Document getDocument(int id) {
        Document doc = documents.get(id);
        return doc;
    }

    public Set<Document> getAll() {
        return null;
    }

    public boolean exists(String a, String t) {

        return true;
    }



    

    
}
