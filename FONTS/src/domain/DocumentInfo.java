package src.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DocumentInfo {
    private Integer docID;
    private String titleName;
    private String authorName;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;
    private Double semblance;

    /*private void print(String s) {
        System.out.println(s);
    }*/

    public DocumentInfo(Integer docID, String titleName, String authorName, LocalDateTime creationDate, LocalDateTime modificationDate) {
        this.docID = docID;
        this.titleName = titleName;
        this.authorName = authorName;
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
        return  titleName + "    " + authorName + "    " + dtf.format(creationDate) + "    " + dtf.format(modificationDate);
    }

    public Integer id() {
        return docID;
    }

    public String title() {
        return titleName;
    }

    public String author() {
        return authorName;
    }

    public LocalDateTime creationDate() {
        return creationDate;
    }

    public LocalDateTime modificationDate() {
        return modificationDate;
    }

    public Double semblance() {
        return semblance;
    }

    public void setSemblance(Double s) {
        semblance = s;
    }

}
