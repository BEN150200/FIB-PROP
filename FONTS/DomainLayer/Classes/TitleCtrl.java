package DomainLayer.Classes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TitleCtrl {
    private static TitleCtrl instance = null;
    Set<Title> titleSet = new HashSet<Title>();
    HashMap<String, Set<Document>> titleDocs = new HashMap<String, Set<Document>>();
    HashMap<String, Title> titles = new HashMap<String, Title>();

    public static TitleCtrl getInstance() {
        if (instance == null) {
            instance = new TitleCtrl();
        }
        return instance;
    }

    public boolean exists(String t) {

        for (Title title : titleSet) {
            if (t == title.getTitle()) return true;
        }
        return false;
    }

    //adds the document d to the title t
    //if the title t don't exists it is created
    //returns false if the title already have the document
    //else returns true
    public boolean addDocToTitle(String t, Document d) {
        boolean exsistsT = titleDocs.containsKey(t);
        if (exsistsT) {
            return titleDocs.get(t).add(d);
        }
        else {
            Title title = new Title(t);
            titleSet.add(title);
            Set<Document> docs = new HashSet<Document>();
            docs.add(d);
            titleDocs.put(t, docs);
            return true;
        }
    }

    public boolean addTitle(String t) {
        boolean exists = titleDocs.containsKey(t);
        if (!exists) {
            Title title = new Title(t);
            titleSet.add(title);
            Set<Document> docs = new HashSet<Document>();
            titleDocs.put(t, docs);
        }
        return exists;
    }
}
