import DomainLayer.Classes.AuthorCtrl;
import DomainLayer.Classes.DocumentInfo;
import DomainLayer.Classes.DomainCtrl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
                        "   1   Add a document in the sistem",
                        "   2   Add an autor in the sistem",
                        "   3   Add a title in the sistem",
                        "   4   Add a Boolean expresion in the sistem",
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
                    addAuthor();
                    break;
                case 3:
                    addTitle();
                    break;
                case 4:
                    addBooleanExpresion();
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
            Integer command = getInputAsInt(0, 6);
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
                case 0:
                    return;
                default:
                    optionError();
                    break;
            }
        }
    }

    public void addDocument() {
        getInputAsLine();
        printCmd("-Enter Title name:");
        String titleName = getInputAsLine();
        printCmd("-Enter Author name:");
        String authorName = getInputAsLine();
        printCmd("-Enter the Document content: ( CTRL+D to finish)");
        Scanner contentReader = new Scanner(System.in);
        List<String> content = new ArrayList<String>();
        while (contentReader.hasNextLine()) {
            content.add(contentReader.nextLine());
        }
        //contentReader.close();
        boolean result = domain.addDocument(titleName, authorName, content);
        System.out.println(result);
    }

    /**
     * Add Functions
    **/
    public void addAuthor() {
        getInputAsLine();
        printCmd("-Enter Author name:");
        String authorName = getInputAsLine();
        System.out.println(domain.addAuthor(authorName));
    }

    public void addTitle() {
        getInputAsLine();
        printCmd("-Enter Title name:");
        String titleName = getInputAsLine();
        System.out.println(domain.addTitle(titleName));
    }

    public void addBooleanExpresion() {}

    /**
     * Search Functions
    **/
    public void searchAllDocuments() {
        ArrayList<DocumentInfo> info = domain.getAllDocumentsInfo();
        for (DocumentInfo documentInfo : info) {
            documentInfo.printCMD();
        }

    }

    public void searchAllAuthors() {
        printCmd("____Authors____");
        ArrayList<String> authors = domain.getAllAuthors();
        for (String string : authors) {
            System.out.println(string);
        }
    }

    public void searchAllTitles() {
        printCmd("____Titles____");
        ArrayList<String> titles = domain.getAllTitles();
        for (String string : titles) {
            printCmd(string);
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
        printCmd("-Enter Author name:");
        String authorName = getInputAsLine();
        ArrayList<String> titles = domain.getAllAuthorTitles(authorName);
        printCmd("____Titles of " + authorName + "____");
        for (String string : titles) {
            printCmd(string);
        }
    }

    public void searchAllTitleAuthors() {
        getInputAsLine();
        printCmd("-Enter Title name:");
        String titleName = getInputAsLine();
        printCmd("____Authors of " + titleName + "____");
        ArrayList<String> authors = domain.getAllTitleAuthors(titleName);
        for (String string : authors) {
            printCmd(string);
        }
    }

    



}
