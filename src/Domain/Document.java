package Domain;

public class Document {

    private Integer docID;
    private Author  author = new Author(null);
    private Title  title = new Title(null);
    private String filePath = null;
    private String fileName;
    
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


    public String getPath() {
        return filePath;
    }

    public void setPath(String p) {
        if (filePath == null) filePath = p;
    }

    public String getTitle() {
        return title.getTitle();
    }
}
