import domain.controllers.DomainCtrl;
import domain.DocumentInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.HashMap;

public class ConsoleCtrl extends PresentationCtrl{
    
    /**
     * Attributes
     */

    Set<Integer> documents = new HashSet<Integer>();

    String[] logo = {   "\n",
        "___  ___                                __ _     _    _               _ ",
		"|  \\/  |                               / _| |   | |  | |             | |",
		"| .  . | __ _  ___ _ __ ___  ___  ___ | |_| |_  | |  | | ___  _ __ __| |",
		"| |\\/| |/ _` |/ __| '__/ _ \\/ __|/ _ \\|  _| __| | |/\\| |/ _ \\| '__/ _` |",
		"| |  | | (_| | (__| | | (_) \\__ \\ (_) | | | |_  \\  /\\  / (_) | | | (_| |",
		"\\_|  |_/\\__,_|\\___|_|  \\___/|___/\\___/|_|  \\__|  \\/  \\/ \\___/|_|  \\__,_|",
		"\n"
    };

    String[] mainMenuOptions = {   
                        "_____________________________________________________",
                        "Choose the type of operation:",
                        "   1   Add information to the System",
                        "   2   Make a Search in the System",
                        "   0   Exit the program",
                        ""
    };

    String[] addMenuOptions = {
                        "_____________________________________________________",
                        "_____________________Add Menu________________________",
                        "Choose the type of operation:",
                        "   1   Add a Document in the System",
                        "   2   Add a Boolean Expression in the System",
                        "   3   Delete Document from the System",
                        "   4   Delete Boolean Espression from the System",
                        "   0   Exit Add Menu",
                        ""
    };
                    
    String[] searchMenuOptions = {
                        "_____________________________________________________",
                        "____________________Search Menu______________________",
                        "Choose the type of operation:",
                        "   1   List all the Documents with his ID, Title and Author",
                        "   2   List all the Titles",
                        "   3   List all the Authors",
                        "   4   List all the Boolean expresions",
                        "   5   List all the Document's Titles of an Author",
                        "   6   List all the Document's Author of a Title",
                        "   7   List all the Documents of a Author",
                        "   8   List all the Documents of a Title",
                        "   9   List all the Documents of a Title and an Author",
                        "   10  Show Docuement Content",
                        "   0   Exit Search Menu",
                        ""
    };
                
    Scanner terminalIn = new Scanner(System.in);
    
    private DomainCtrl domain;
    
    /**
     * Constructor
    **/
    public ConsoleCtrl() {
        domain = DomainCtrl.getInstance();
    }

    /**
     * Private Functions
    **/
    private void exit() {
        printCmd("\n");
        printCmd("Closing the program");
        System.exit(0);
    }

    private int getInputAsInt(int min, int max) {

        while (true) {
            try {
                int num = terminalIn.nextInt();

                while (min > num || num > max) {
                    printCmd("Please, enter a valid option (number between " + min + " and " + max + ")");
                    num = terminalIn.nextInt();
                }

                return num;

            } catch (Exception e) {
                terminalIn.next();
                printCmd("It should be a number between " + min + " and " + max);
            }
        }
    }

    private String getInputAsLine() {
        return terminalIn.nextLine();
    }

    private void optionError() {
        printCmd("Please enter a valid option (number)\n");
    }

    private void printCmd(String s) {
        System.out.println(s);
    }

    /**
     * Print Functions
    **/
    private void printMainMenu() {
        for (String m : mainMenuOptions){
            printCmd(m);
        }
    }

    private void printLogo() {
        for (String l : logo){
            printCmd(l);
        }
    }

    private void printAddMenu() {
        for (String a : addMenuOptions) {
            printCmd(a);
        }
    }

    private void printSearchMenu() {
        for (String s : searchMenuOptions) {
            System.out.println(s);
        }
    }

    /**
     * Public Functions
    **/

    /**
     * Menus
    **/
    public void launchPresentation() {

        printLogo();
        
        while (true) {

            printMainMenu();
            Integer command = getInputAsInt(0, 2);
            printCmd("");
            switch (command) {
                case 1:
                    addMenu();
                    break;
                case 2:
                    searchMenu();
                    break;
                case 0:
                    exit();
                    break;
                default:
                    optionError();
                    break;   
            }
        }
    }
    
    public void addMenu() {
        
        while (true) {

            printAddMenu();
            Integer command = getInputAsInt(0, 4);
            printCmd("");
            switch (command) {
                case 1:
                    addDocument();
                    break;
                 
                case 2:
                    addBooleanExpresion();
                    break;
                case 3:
                    deleteDocument();
                    break;
                case 4:
                    deleteBooleanExpression();
                    break;
                case 0:
                    return;
                default:
                    optionError();
                    break;      
            }
        }
    }
    
    private void searchMenu() {

        while (true) {

            printSearchMenu();
            Integer command = getInputAsInt(0, 10);
            printCmd("");
            
            switch (command) {
                case 1:
                    searchAllDocuments();
                    break;
                case 2:
                    searchAllTitles();
                    break;
                case 3:
                    searchAllAuthors();
                    break;
                case 4:
                    searchAllBooleanExpresions();
                    break;
                case 5:
                    searchAllAuthorTitles();
                    break;
                case 6:
                    searchAllTitleAuthors();
                    break;
                case 7:
                    searchAllAuthorDocs();
                    break;
                case 8:
                    searchAllTitleDocs();
                    break;
                case 9:
                    searchDocsByTitleAndAuthor();
                case 10:
                    showDocument();
                case 0:
                    return;
                default:
                    optionError();
                    break;
            }
        }
    }

    /**
     * Add Functions
    **/
    public void addDocument() {
        getInputAsLine();

        printCmd("-Enter Title name:");
        String titleName = getInputAsLine();

        printCmd("\n -Enter Author name:");
        String authorName = getInputAsLine();

        printCmd("\n -Enter the Document content: ( CTRL+D to finish)");
        Scanner contentReader = new Scanner(System.in);
        List<String> content = new ArrayList<String>();

        while (true) {
            try {
                String s = contentReader.nextLine();
                content.add(s);
            }
            catch (NoSuchElementException eof) { //if there are no more elements cach the exception
                printCmd("");
                printCmd("End of the document");
                //contentReader.close();
                break;
            }
        }
        if (domain.addDocument(titleName, authorName, content)) {
            printCmd("Document added successfully");
        }
        else printCmd("The Document identified by the Title '" + titleName + "' and the Author '" + authorName + "'is already in the System");
    }

    /*
    public void addAuthor() {
        getInputAsLine();
        printCmd("-Enter Author name:");
        String authorName = getInputAsLine();
        if (domain.addAuthor(authorName)) {
            printCmd("Author added successfully");
        }
        else printCmd("This Author is already in the System");
    }

    public void addTitle() {
        getInputAsLine();
        printCmd("-Enter Title name:");
        String titleName = getInputAsLine();
        if (domain.addTitle(titleName)) {
            printCmd("Title added successfully");
        }
        else printCmd("This Title is already in the System");
    }
    */

    public void addBooleanExpresion() {
        getInputAsLine();
        printCmd("-Enter the Boolean Expresion Name:");
        String boolExpName = getInputAsLine();
        printCmd("-Enter a Boolean Expresion:");
        String boolExp = getInputAsLine();
        if (domain.addBooleanExpresion(boolExpName, boolExp)) {
            printCmd("Title added successfully");
        }
        else printCmd("This Title is already in the System");
    }

    public void deleteDocument() {
        getInputAsLine();

        printCmd("-Enter Title name:");
        String titleName = getInputAsLine();

        printCmd("\n -Enter Author name:");
        String authorName = getInputAsLine();
        domain.deleteDocument(titleName, authorName);

    }

    public void deleteBooleanExpression() {
        getInputAsLine();
        printCmd("-Enter the Boolean Expresion Name:");
        String boolExpName = getInputAsLine();
        domain.deleteBooleanExpression(boolExpName);
    }
    /**
     * Search Functions
    **/
    public void searchAllDocuments() {
        ArrayList<DocumentInfo> info = domain.getAllDocumentsInfo();
        if (info.isEmpty()) printCmd("There are no Documents in the System");
        else {
            for (DocumentInfo documentInfo : info) {
                documentInfo.printCMD();
            }
        }

    }

    public void searchAllAuthors() {
        getInputAsLine();
        printCmd("-If you whant to search by prefix enter the Author prefix, else press Enter:");
        String authorName = getInputAsLine();

        ArrayList<String> authors = domain.getAllAuthors(authorName);
        if (authors.isEmpty()) printCmd("There are no Authors in the System");
        else {
            printCmd("____Authors____");
            for (String string : authors) {
                System.out.println(string);
            }
        }
    }

    public void searchAllTitles() {
        getInputAsLine();
        printCmd("-If you whant to search by prefix enter the Title prefix, else press Enter:");
        String titleName = getInputAsLine();

        ArrayList<String> titles = domain.getAllTitles(titleName);
        if (titles.isEmpty()) printCmd("There are no Titles in the System");
        else {
            printCmd("____Titles____");
            for (String string : titles) {
                printCmd(string);
            }
        }
    }

    
    public void searchAllBooleanExpresions() {
        printCmd("____Boolean Expresions____");
        HashMap<String,String> expresions = domain.getAllBooleanExpresions();
        for (String string : expresions.keySet()) {
            printCmd(string + "    " + expresions.get(string)); 
        }
    }

    public void searchAllAuthorTitles() {
        getInputAsLine();
        printCmd("-Enter Author name or a prefix:");
        String authorName = getInputAsLine();

        ArrayList<String> titles = domain.getAllAuthorTitles(authorName);
        if(titles.isEmpty()) printCmd("There are no Titles in the System with this Author");
        else {
            printCmd("____Titles of " + authorName + "____");
            for (String string : titles) {
                printCmd(string);
            }
        }
    }

    public void searchAllTitleAuthors() {
        getInputAsLine();
        printCmd("-Enter Title name or a prefix:");
        String titleName = getInputAsLine();

        ArrayList<String> authors = domain.getAllTitleAuthors(titleName);
        if (authors.isEmpty()) printCmd("There are no Authors in the System with this Title");
        else {    
            printCmd("____Authors of " + titleName + "____");
            for (String string : authors) {
                printCmd(string);
            }
        }
    }

    public void searchAllAuthorDocs() {
        getInputAsLine();
        printCmd("-Enter Author name or a prefix of the name:");
        String authorName = getInputAsLine();
        printCmd("");
        ArrayList<DocumentInfo> titles = domain.getAllAuthorDocuments(authorName);
        printCmd("____Documents of " + authorName + "____");
        for (DocumentInfo documentInfo : titles) {
            documentInfo.printCMD();
        }
    }

    public void searchAllTitleDocs() {
        getInputAsLine();
        printCmd("-Enter Title name or a prefix of the name:");
        String titleName = getInputAsLine();
        printCmd("");
        printCmd("____Documents of " + titleName + "____");
        ArrayList<DocumentInfo> authors = domain.getAllTitleDocuments(titleName);
        for (DocumentInfo documentInfo : authors) {
            documentInfo.printCMD();
        }
    }

    public void searchDocsByTitleAndAuthor() {
        getInputAsLine();
        printCmd("-Enter Title name or a prefix of the name:");
        String titleName = getInputAsLine();
        printCmd("-Enter Author name or a prefix of the name:");
        String authorName = getInputAsLine();
        printCmd("");
        printCmd("____Documents of " + titleName + " and " + authorName + "____");
        ArrayList<DocumentInfo> docsInfo = domain.getDocsByTitleAndAuthor(titleName, authorName);
        for (DocumentInfo documentInfo : docsInfo) {
            documentInfo.printCMD();
        }
    }

    public void showDocument() {
        getInputAsLine();
        printCmd("-Enter the Document ID:");
        Integer docID = getInputAsInt(1, 2^31-1);
        ArrayList<String> content = domain.getDocumentContent(docID);
        getInputAsLine();
        printCmd("");
        if(content != null) {
            printCmd("- Title: " + content.get(0));
            printCmd("- Author: " + content.get(1));
            printCmd("");
            content.remove(0);
            content.remove(1);
            for (String sentence : content) {
                printCmd(sentence);
            }
        }
        else printCmd("There in no document with this ID in the System");
        
    }
    



}
