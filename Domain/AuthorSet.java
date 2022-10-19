package Domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class AuthorSet {
    private static AuthorSet instance = null;
    Set<Author> authorSet = new HashSet<Author>();
    HashMap<String, Set<Document>> authorDocs = new HashMap<String, Set<Document>>();

    public static AuthorSet getInstance() {
        if (instance == null) {
            instance = new AuthorSet();
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
    public boolean add(String a, Document d) {
        boolean exsistsT = authorDocs.containsKey(a);
        if (exsistsT) {
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
}
