package DomainLayer.Classes;

import java.time.LocalDateTime;
import java.util.List;

public class Document {

    private Integer docID;
    private Author  author;
    private Title  title;
    private String filePath;
    //private String fileName;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;
    
    //private enum Format {TXT, XML, PROP}
    //private Format format;

    public Document(int id) {
        docID = id;
    }

    public Document(int id, String t, String a, String p) {
        docID = id;
        author = new Author(a);
        title = new Title(t);
        filePath = p;
    }

    public Document(String titleName, String authorName, String path) {
        author = new Author(authorName);
        title = new Title(titleName);
    }

    public Document(Title t, Author a) {
        title = t;
        author = a;
    }

    /**
     * Public Functions
     */

    /**
     * Getters
     */
    public Author getAuthor() {
        return author;
    }

    public Title getTitle() {
        return title;
    }

    public String getPath() {
        return filePath;
    }

    public Integer getID() {
        return docID;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getModificationDate() {
        return modificationDate;
    }

    public DocumentInfo getInfo() {
        return new DocumentInfo(docID, title.getTitleName(), author.getAuthorName(), creationDate, modificationDate);
    }

    /**
     * Setters
     */
    public boolean setPath(String path) {
        if (filePath == null) {
            filePath = path;
            return true;
        }
        return false;
    }

    public boolean setDocumentID(Integer id) {
        if (docID == null) {
            docID = id;
            return true;
        }
        return false;
    }

    public void updateContent(List<String> content) {}

    
}
