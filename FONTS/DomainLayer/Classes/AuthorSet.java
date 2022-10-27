package DomainLayer.Classes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class AuthorCtrl {
    private static AuthorCtrl instance = null;
    Set<Author> authorSet = new HashSet<Author>();
    HashMap<String, Set<Document>> authorDocs = new HashMap<String, Set<Document>>();
    HashMap<String, Author> authors = new HashMap<String, Author>();

    public static AuthorCtrl getInstance() {
        if (instance == null) {
            instance = new AuthorCtrl();
        }
        return instance;
    }

    public boolean exists(String a) {

        for (Author author : authorSet) {
            if (a == author.getAuthor()) return true;
        }
        return false;
    }
    

    //adds the document d to the title t
    //if the title t don't exists it is created
    //returns false if the title already have the document
    //else returns true
    public boolean addDocToAuthor(String a, Document d) {
        boolean existsA = authorDocs.containsKey(a);
        if (existsA) {
            return authorDocs.get(a).add(d);
        }
        else {
            Author author = new Author(a);
            authorSet.add(author);
            Set<Document> docs = new HashSet<Document>();
            docs.add(d);
            authorDocs.put(a, docs);
            return true;
        }
    }

    public boolean addAuthor(String a) {
        boolean exists = authorDocs.containsKey(a);
        if (!exists) {
            Author author = new Author(a);
            authorSet.add(author);
            Set<Document> docs = new HashSet<Document>();
            authorDocs.put(a, docs);
        }
        return exists;
    }
}
