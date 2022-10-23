package DomainLayer.Interfaces;

import java.util.Set;
import DomainLayer.Classes.Author;

public interface AuthorController {
    public Boolean existsAuthor(String authorName);

    public Author getAuthor(String authorName);

    public void addAuthor(Author author);

    public void deleteAuthor(String authorName);

    public Set<Author> getAllAuthors();
}