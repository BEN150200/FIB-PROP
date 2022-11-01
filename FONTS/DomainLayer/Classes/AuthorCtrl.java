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
    public boolean existsAuthor(String authorName) {
        return authors.containsKey(authorName);
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

    public Set<String> getAuthorsNamesPrefix(String prefix) {
        Set<String> authorsWithPrefix = new HashSet<String>();
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
