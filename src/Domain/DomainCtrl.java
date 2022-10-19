package Domain;

import java.util.HashSet;
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

     /**
     * File Managment Functions
     */

    //pre: the filePath is the path of an existing file
    //exc: the file is already in the system
    //post: the file is loaded into the system
    //      returns the docID of the new docuemnt
    public Integer loadFile(String filePath) {
        System.out.println("de moment el fitcher no s'ha carregat XD");
        return -1;
    }


    public Set<Integer> loadFileSet(Set<String> files) {
        Set<Integer> ids = new HashSet<>();
        for (String filePath : files) {
            ids.add(loadFile(filePath));
        }
        return ids;
    }

    public Set<Integer> loadFolder(String dir) {
        Set<Integer> ids = new HashSet<>();

        return ids;
    }


    //pre: doc is a document of the system and has ven modified
    //post: the doc is saved into his file
    public void saveFile(Document doc) {
        String path;
        try {
            path = doc.getPath();

        } catch(NullPointerException e) {
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




    public Set<Document> getDocs() {
        return DocumentSet.getInstance().getAll();
    }
}