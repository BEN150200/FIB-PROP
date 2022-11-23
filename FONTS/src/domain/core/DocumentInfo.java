package src.domain.core;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DocumentInfo {
    private Integer docID;
    private String title;
    private String author;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;

    private String path;
    private String fileName;
    private String extencion;
    private String content;

    private Double semblance;

    /*private void print(String s) {
        System.out.println(s);
    }*/

    public DocumentInfo(Integer docID, String titleName, String authorName, LocalDateTime creationDate, LocalDateTime modificationDate) {
        this.docID = docID;
        this.title = titleName;
        this.author = authorName;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
    }

    /*public void printCMDoneLine() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");  
        print("    " + docID + "    " + titleName + "    " + authorName + "    " + dtf.format(creationDate) + "    " + modificationDate);
    }

    public void printCMD() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");  
        print("Document:  " + docID );
        print("    -Title:  " + titleName);
        print("    -Author:  " + authorName);
        print("    -Dates:");
        print("        Creation: "+ dtf.format(creationDate) + "    Modification: " + dtf.format(modificationDate));
        print("\n");
    }*/
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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getModificationDate() {
        return modificationDate;
    }

    public Double getSemblance() {
        return semblance;
    }

    public void setSemblance(Double s) {
        semblance = s;
    }

}
