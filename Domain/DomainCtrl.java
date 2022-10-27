package Domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.xml.catalog.CatalogFeatures.Feature;

public class DomainCtrl {

    /**
     * Attributes
     */
    private static DomainCtrl instance = null;
    
    /**
     * Constructor
     */
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
     */
    public boolean addAuthor(String aName) {
        return AuthorCtrl.getInstance().addAuthor(aName);
    }

    public boolean addTitle(String title) {
        return TitleCtrl.getInstance().addTitle(title);
    }

    public boolean addDocument(String a, String t, List<String> contnet) {
        DocumentCtrl.getInstance().exists(a, t);
        
        return false;
    }

    public Set<Document> getDocs() {
        return DocumentCtrl.getInstance().getAll();
    }






     /**
     * File Managment Functions
     */

    //pre: the filePath is the path of an existing file
    //exc: the file is already in the system
    //post: the file is loaded into the system
    //      returns the docID of the new docuemnt
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


    /**
     * Export Functions
     */
    public void exportTXT(Document doc, String path, String name) {
        System.out.println("Funcio no implementada XD");
    }

    public void exportXML(Document doc, String path, String name) {
        System.out.println("Funcio no implementada XD");
    }

    
}