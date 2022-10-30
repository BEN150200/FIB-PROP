package DomainLayer.Classes;

import java.time.LocalDateTime;

public class Document {

    private Integer docID;
    private Author  author;
    private Title  title;
    private String filePath;
    private String fileName;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;
    
    private enum Format {TXT, XML, PROP}
    private Format format;

    public Document(int id) {
        docID = id;
    }

    public Document(int id, String t, String a, String p) {
        docID = id;
        author = new Author(a);
        title = new Title(t);
        filePath = p;
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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getModificationDate() {
        return modificationDate;
    }

    /**
     * Setters
     */
    public void setPath(String p) {
        if (filePath == null) filePath = p;
    }

    
}
