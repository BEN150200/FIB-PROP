package DomainLayer.Classes;

import java.time.LocalDateTime;

public class DocumentInfo {
    private Integer docID;
    private String titleName;
    private String authorName;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;

    public DocumentInfo(Integer docID, String titleName, String authorName, LocalDateTime creationDate, LocalDateTime modificationDate) {
        this.docID = docID;
        this.titleName = titleName;
        this.authorName = authorName;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
    }

    public void printCMD() {
        System.out.println("    " + docID + "    " + titleName + "    " + authorName + "    " + creationDate + "    " + modificationDate);
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

}
