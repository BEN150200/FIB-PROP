package src.domain.core;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import src.enums.Format;

public class DocumentInfo {
    private Integer docID;
    private String title;
    private String author;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;
    private ArrayList<String> content;

    private String path;
    private Format fileFormat;

    private String fileName;

    private Double similarity;

    /*private void print(String s) {
        System.out.println(s);
    }*/

    public DocumentInfo(Integer docID, String titleName, String authorName, LocalDateTime creationDate, LocalDateTime modificationDate, ArrayList<String> content, String path, Format fileFormat, String fileName) {
        this.docID = docID;
        this.title = titleName;
        this.author = authorName;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.content = content;
        this.path = path;
        this.fileFormat = fileFormat;
        this.fileName = fileName;
        /*
        if (path.lastIndexOf("/") != path.length()-1) {
            System.out.println(path.lastIndexOf("/"));
            System.out.println(path.lastIndexOf(path.length()-1));
            fileName = path.substring(path.lastIndexOf("/") + 1);
        }
        else if (path.lastIndexOf("\\") != path.length()-1) {
            System.out.println(path.lastIndexOf("\\"));
            System.out.println(path.lastIndexOf(path.length()-1));

            fileName = path.substring(path.lastIndexOf("\\") + 1);
        }

         */
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

    public ArrayList<String> getContent(){
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

        if (s == 0.0) similarity = 100.0;
        else similarity = s*100;

    }

}
