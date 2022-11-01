package DomainLayer.Classes;

import java.util.HashMap;
import java.util.ArrayList;

public class AuthorCtrl {
    private static AuthorCtrl instance = null;
    HashMap<String, Author> authors = new HashMap<String, Author>();

    public static AuthorCtrl getInstance() {
        if (instance == null) {
            instance = new AuthorCtrl();
        }
        return instance;
    }

    /**
     * Public Functions
    **/

    /**
     * Getters
    **/
    public boolean existsAuthor(String authorName) {
        return authors.containsKey(authorName);
    }

    public Author getAuthor(String authorName) {
        return authors.get(authorName);
    }

    public ArrayList<Author> getAllAuthors() {
        return new ArrayList<Author>(authors.values());
    }

    public ArrayList<String> getAllAuthorsNames() {
        return new ArrayList<String>(authors.keySet());
    }

    public ArrayList<String> getAuthorsNamesPrefix(String prefix) {
        ArrayList<String> authorsWithPrefix = new ArrayList<String>();
        for (String author : authors.keySet()) {
            if (author.startsWith(prefix)) {
                authorsWithPrefix.add(author);
            }
        }
        return authorsWithPrefix;
    }

    /**
     * Setters
    **/

    public boolean addAuthor(Author a) {
        if (!existsAuthor(a.getAuthorName())){
            authors.put(a.getAuthorName(),a);
            return true;
        }
        return false;
    }

    public boolean deleteAuthor(String authorName) {
        if (existsAuthor(authorName)){
            authors.remove(authorName);
            return true;
        }
        return false;
    }
}
