package src.domain.core;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import src.enums.Format;

public class DocumentInfo {
    private Integer docID;
    private String title;
    private String author;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;
    private List<String> content;

    private String path;
    private Format fileFormat;

    private String fileName;

    private Double similarity = 0.0d;

    public DocumentInfo(Integer docID, String titleName, String authorName, LocalDateTime creationDate, LocalDateTime modificationDate, List<String> content, String path, Format fileFormat, String fileName) {
        this.docID = docID;
        this.title = titleName;
        this.author = authorName;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.content = content;
        this.path = path;
        this.fileFormat = fileFormat;
        this.fileName = fileName;
    }

    public DocumentInfo(
            Integer docID, String titleName, String authorName,
            LocalDateTime creationDate, LocalDateTime modificationDate,
            List<String> content, String path, Format fileFormat, String fileName, double similarity)
    {
        this.docID = docID;
        this.title = titleName;
        this.author = authorName;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.content = content;
        this.path = path;
        this.fileFormat = fileFormat;
        this.fileName = fileName;
        this.similarity = similarity;
    }

    public DocumentInfo withSimilarity(double similarity) {
        return new DocumentInfo(docID, title, author, creationDate, modificationDate, content, path, fileFormat, fileName, similarity);
    }

    public String toString(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return  title + "    " + author + "    " + dtf.format(creationDate) + "    " + dtf.format(modificationDate);
    }

    public Integer getId() {
        return docID;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public List<String> getContent(){
        return content;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getModificationDate() {
        return modificationDate;
    }

    public String getPath(){
        return path;
    }

    public Format getFormat(){
        return fileFormat;
    }

    public String getFileName() {
        return fileName;
    }

    public Double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Double s) {
        similarity = s;
    }

}
