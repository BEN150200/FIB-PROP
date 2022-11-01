package DomainLayer.Classes;

import java.util.HashMap;
import java.util.HashSet;

/* 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.xml.catalog.CatalogFeatures.Feature;
*/

import java.util.List;
import java.util.Set;


public class DomainCtrl {

    /**
     * Attributes
    **/
    private static DomainCtrl instance = null;
    
    /**
     * Constructor
    **/
    public DomainCtrl() {
        iniCtrlDomain();
    }

    private void iniCtrlDomain() {
        
    }

    public static DomainCtrl getInstance() {
        if (instance == null) {
            instance = new DomainCtrl();
        }
        return instance;
    }

    /**
     * Public Functions
    **/

    /**
     * Add Elements
    **/
    public boolean addAuthor(String authorName) {
        Author a = new Author(authorName);
        return AuthorCtrl.getInstance().addAuthor(a);
    }

    public boolean addTitle(String titleName) {
        Title t = new Title(titleName);
        return TitleCtrl.getInstance().addTitle(t);
    }

    public boolean addDocument(String titleName, String authorName, List<String> content) {
        if (!DocumentCtrl.getInstance().existsDocument(titleName, authorName)) {
            Title t = TitleCtrl.getInstance().getTitle(titleName);
            Author a = AuthorCtrl.getInstance().getAuthor(authorName);
            if (t == null) {
                t = new Title(titleName);
                TitleCtrl.getInstance().addTitle(t);
            }
            if (a == null) {
                a = new Author(authorName);
                AuthorCtrl.getInstance().addAuthor(a);
            }
            Document d = new Document(t, a);
            DocumentCtrl.getInstance().addDocument(d);
            Integer ID = d.getID();
            t.addDoc(ID);
            a.addDoc(ID);
            d.updateContent(content);
            return true;
        }
        return false;
    }

    /**
     * Search Elements
    **/
    public Set<DocumentInfo> getAllDocumentsInfo() {
        Set<Document> docs = DocumentCtrl.getInstance().getAllDocuments();
        Set<DocumentInfo> info = new HashSet<DocumentInfo>();
        for (Document d : docs) {
            info.add(d.getInfo());
        }
        return info;
    }

    public Set<String> getAllTitles() {
        return TitleCtrl.getInstance().getAllTitlesNames();
    }

    public Set<String> getAllAuthors() {
        return AuthorCtrl.getInstance().getAllAuthorsNames();
    }

    public Set<String> getAllAuthorTitles(String authorName) {
        Author author = AuthorCtrl.getInstance().getAuthor(authorName);
        if (author == null) System.out.println("The author is not in the System");
        else {
            Set<Integer> docsID = author.getAllDocsID();
            Set<Document> docs = DocumentCtrl.getInstance().getDocuments(docsID);
            Set<String> docsTitlesNames = new HashSet<String>();
            for (Document doc : docs) {
                docsTitlesNames.add(doc.getTitle().getTitleName());
            }
            return docsTitlesNames;
        }
        return null;
    }

    public Set<String> getAllTitleAuthors(String titleName) {
        Title title = TitleCtrl.getInstance().getTitle(titleName);
        if (title == null) System.out.println("The title is not in the System");
        else {
            Set<Integer> docsID = title.getAllDocsID();
            Set<Document> docs = DocumentCtrl.getInstance().getDocuments(docsID);
            Set<String> docsAuthorsNames = new HashSet<String>();
            for (Document doc : docs) {
                docsAuthorsNames.add(doc.getAuthor().getAuthorName());
            }
            return docsAuthorsNames;
        }
        return null;
    }

    public HashMap<String,String> getAllBooleanExpresions() {
        return null;
    }





    /**
     * File Managment Functions
    **/

    //pre: the filePath is the path of an existing file
    //exc: the file is already in the system
    //post: the file is loaded into the system
    //      returns the docID of the new docuemnt
    /* 
    public Boolean loadFile(String filePath) {
        //System.out.println("de moment el fitcher no s'ha carregat XD");
        try {
            String extension = "";

            int i = filePath.lastIndexOf('.');
            if (i > 0) {
                extension = filePath.substring(i+1);
            }
            if (extension != "txt") {
                System.out.println("File is not a .txt");
                return false;
            }

            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            String title;
            String author;
            if (scanner.hasNextLine()) {
                title = scanner.nextLine();
                if (scanner.hasNextLine()) author = scanner.nextLine(); 
            }
            //buscar si existeix el fitxer 
            return false;

        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
            return false;
        } 
    }
    */
    /* 
    public Set<Integer> loadFileSet(Set<String> files) {
        Set<Integer> ids = new HashSet<>();
        for (String filePath : files) {
            loadFile(filePath);
        }
        return ids;
    }

    public Set<Integer> loadFolder(String dir) {
        Set<Integer> ids = new HashSet<>();

        return ids;
    }


    //pre: doc is a document of the system and has ven modified
    //post: the doc is saved into his file
    public void saveFile(int docID) {
        String path;
        try {
            

        } 
        catch(NullPointerException e) {
            
        }

        System.out.println("Funcio no implementada XD");
    }
    */


    /**
     * Export Functions
     */
    /* 
    public void exportTXT(Document doc, String path, String name) {
        System.out.println("Funcio no implementada XD");
    }

    public void exportXML(Document doc, String path, String name) {
        System.out.println("Funcio no implementada XD");
    }
    */


    
}
