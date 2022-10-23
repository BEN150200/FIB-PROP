package DomainLayer.Interfaces;

import java.util.Set;
import DomainLayer.Classes.Document;

public interface DocumentController {
    public Boolean existsDocument(String titleName, String authorName);

    public Document getDocument(String titleName, String authorName);

    public void addDocument(Document document);

    public void deleteDocument(String titleName, String authorName);

    public Set<Document> getAllDocuments();
}