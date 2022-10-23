package DomainLayer.Classes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javafx.util.Pair;

public class DocumentSet {
    private static DocumentSet instance = null;
    private int usedID;

    private HashMap<Pair<String,String>, Document> documents;

    public static DocumentSet getInstance() {
        if (instance == null) {
            instance = new DocumentSet();
        }
        return instance;
    }

    public DocumentSet() {
        usedID = 0;
    }

    

    public Document getDocument(int id) {
        Document doc = documents.get(id);
        return doc;
    }

    public Set<Document> getAll() {
        return null;
    }

    public boolean exists() {

        return true;
    }



    

    
}
