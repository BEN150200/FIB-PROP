package DomainLayer.Classes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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
    public boolean existsAuthorName(String authorName) {
        return authors.containsKey(authorName);
    }

    public boolean existsAuthor(Author a) {
        return authors.containsValue(a);
    }

    public Author getAuthor(String authorName) {
        return authors.get(authorName);
    }

    public Set<Author> getAllAuthors() {
        return new HashSet<Author>(authors.values());
    }

    public Set<String> getAllAuthorsNames() {
        return authors.keySet();
    }

    /**
     * Setters
    **/
    public boolean addAuthorName(String authorName) {
        boolean exists = authors.containsKey(authorName);
        if (!exists) {
            Author author = new Author(authorName);
            authors.put(authorName, author);
        }
        return exists;
    }

    public void addAuthor(Author a) {
        authors.put(a.getAuthorName(),a);
    }

    public boolean deleteAuthor(String authorName) {
        return false;
    }
}
