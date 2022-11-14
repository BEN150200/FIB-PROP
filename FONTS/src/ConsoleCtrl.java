package src;
import java.security.DrbgParameters.Reseed;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

import src.domain.DocumentInfo;
import src.domain.controllers.DomainCtrl;
import src.domain.controllers.SearchCtrl;


import java.util.HashMap;

public class ConsoleCtrl extends PresentationCtrl{
    
    /**
     * Attributes
     */

    Set<Integer> documents = new HashSet<Integer>();

    String[] logo = {   GREEN_BOLD_BRIGHT,
        "\n",
        "___  ___                                __ _     _    _               _ ",
		"|  \\/  |                               / _| |   | |  | |             | |",
		"| .  . | __ _  ___ _ __ ___  ___  ___ | |_| |_  | |  | | ___  _ __ __| |",
		"| |\\/| |/ _` |/ __| '__/ _ \\/ __|/ _ \\|  _| __| | |/\\| |/ _ \\| '__/ _` |",
		"| |  | | (_| | (__| | | (_) \\__ \\ (_) | | | |_  \\  /\\  / (_) | | | (_| |",
		"\\_|  |_/\\__,_|\\___|_|  \\___/|___/\\___/|_|  \\__|  \\/  \\/ \\___/|_|  \\__,_|",
		"\n",
        ANSI_RESET
    };

    String[] mainMenu = {  
                        "",
                        WHITE_UNDERLINED,
                        "                                                     ",
                        //"_____________________________________________________",
                        "                     Main Menu                       ",
                        RESET,
                        WHITE_BOLD,
                        "Choose the type of operation:",
                        "   1 - Add document",
                        "   2 - Open document",
                        "   3 - Manage boolean expressions",
                        "   4 - Search in the system",
                        "   0 - Exit",
                        WHITE_UNDERLINED,
                        "                                                     ",
                        RESET,
                        "",
    };

    String[] booleanExpressionsMenuHeader = {
                        "",
                        "_____________________________________________________",
                        "________________Boolean Expressions__________________",
                        ""
    };

    String[] booleanExpressionsMenuOptions = {
                        "",
                        "Choose the type of operation:",
                        "   1 - Add a new boolean expression",
                        "   2 - Modify an existing boolean expression",
                        "   3 - Delete an existing boolean expression",
                        "   0 - Return to Main Menu",
                        "_____________________________________________________",
                        ""
    };
                    
    String[] searchMenu = {
                        "",
                        "_____________________________________________________",
                        "____________________Search Menu______________________",
                        "Choose the type of operation:",
                        "   1 - Search authors",
                        "   2 - Search titles",
                        "   3 - List all documents",
                        "   4 - Documents by boolean expression",
                        "   5 - Documents by query",
                        "   0 - Return to Main Menu",
                        "_____________________________________________________",
                        ""
    };

    String[] documentHeader = {
                        "",
                        "______________________Document_______________________",
                        ""
    };

    String[] documentOptions = {
                        "",
                        "_________________Document options____________________",
                        "   1 - Modify content",
                        "   2 - Delete",
                        "   3 - Search similar documents",
                        "   0 - Exit",
                        "_____________________________________________________",
                        ""

    };

    String[] similarDocumentsHeader = {
                        "",
                        "_________________Similar Documents___________________",
                        ""
    };

    String[] similarDocumentsOptions = {
                        "",
                        "______________Similar Documents options______________",
                        "   1 - Sort results",
                        "   2 - Open one of the documents",
                        "   0 - Exit",
                        "_____________________________________________________",
                        ""
    };

    String[] authorsHeader = {
                        "",
                        "______________________Authors_______________________",
                        ""
    };

    String[] authorsOptions = {
                        "",
                        "__________________Authors options____________________",
                        "   1 - Sort results",
                        "   2 - Get the titles of one of the authors",
                        "   0 - Exit",
                        "_____________________________________________________",
                        ""
    };

    String[] titlesHeader = {
                        "",
                        "_______________________Titles________________________",
                        ""
    };

    String[] titlesOptions = {
                        "",
                        "___________________Titles options____________________",
                        "   1 - Sort results",
                        "   2 - Get the authors of one of the titles",
                        "   0 - Exit",
                        "_____________________________________________________",
                        ""
    };

    String[] titlesOfAnAuthorHeader = {
                        "",
                        "_______________________Titles________________________",
                        ""
    };

    String[] authorOfATitleHeader = {
                        "",
                        "_______________________Authors________________________",
                        ""
    };

    String[] optionStrings = {
                        "",
                        "_______________________Options________________________",
                        "   1 - Sort results",
                        "   2 - Open document",
                        "   0 - Exit",
                        ""
    };

    String[] sortingArrayMenu = {
                        "",
                        "__________________Sorting options____________________",
                        "   1 - Ascending order  (A -> Z)",
                        "   2 - Descending order (Z -> A)",
                        "   0 - Cancel sorting",
                        "_____________________________________________________",
                        ""
    };

    String[] sortingDocumentsMenu = {
                        "",
                        "__________________Sorting options____________________",
                        "   1 - Sort by title ascending                 (A -> Z)",
                        "   2 - Sort by title descending                (Z -> A)",
                        "   3 - Sort by author ascending                (A -> Z)",
                        "   4 - Sort by author descending               (Z -> A)",
                        "   5 - Sort by creation date ascending         (A -> Z)",
                        "   6 - Sort by creation date descending        (Z -> A)",
                        "   7 - Sort by last modified date ascending    (A -> Z)",
                        "   8 - Sort by last modified date descending   (Z -> A)",
                        "   0 - Cancel sorting",
                        "_____________________________________________________",
                        ""
    };

    String[] allDocumentsHeader = {
                        "",
                        "__________________System documents___________________",
                        ""
    };

    String[] queryDocumentsHeader = {
                        "",
                        "___________________Query documents___________________",
                        ""
    };

    
    Scanner terminalIn = new Scanner(System.in);
    
    private DomainCtrl domain;

    private String currentTitle;
    private String currentAuthor; 
    
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
        System.out.println("\n");
        System.out.println("Closing the program");
        System.exit(0);
    }

    private void printError(String error) {
        System.out.println(RED_BOLD_BRIGHT + error + RESET);
    }

    private void printCorrect(String message) {
        System.out.println(GREEN_BOLD_BRIGHT + message + RESET);
    }

    private void printEnter(String message) {
        System.out.println(BLACK_BOLD_BRIGHT + WHITE_BACKGROUND_BRIGHT+ message + RESET);
    }

    private int getInputAsInt(int min, int max, String message) {
        System.out.println(BLACK_BOLD_BRIGHT + WHITE_BACKGROUND_BRIGHT + message + RESET);
        Integer command;
        while(true){
            try{
                command = terminalIn.nextInt();
                if(min > command || max < command){
                    System.out.println(YELLOW_BOLD_BRIGHT + "Please enter a number from " + min + " to " + max + RESET);
                } else break;
            } catch(Exception e){
                terminalIn.nextLine();
                System.out.println(YELLOW_BOLD_BRIGHT + "Please enter a valid option (number)" + RESET);
            }
        }
        // this makes sure all line is consumed because nextInt() consumes only the number, not the "end of line"
        terminalIn.nextLine();
        System.out.println("");
        return command;
    }

    private void printToConsole(String[] m) {
        for(String s : m){
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

        printToConsole(logo);
        
        while (true) {
            printToConsole(mainMenu);
            Integer command = getInputAsInt(0, 4, "Enter an option number:");
            switch (command) {
                case 1:
                    addDocument();
                    break;
                case 2:
                    openDocumentByInput();
                    break;
                case 3:
                    booleanExpressionMenu();
                    break;
                case 4:
                    searchMenu();
                    break;
                case 0:
                    exit();
                    break;  
            }
        }
    }
    
    public void booleanExpressionMenu() {
        while (true) {
            printToConsole(booleanExpressionsMenuHeader);
            /*
            Obtenir i fer print de totes les expressions booleanes.
            */
            searchAllBooleanExpresions();
            printToConsole(booleanExpressionsMenuOptions);
            Integer command = getInputAsInt(0, 3,"Enter an option number:");
            switch (command) {
                case 1:
                    addBooleanExpresion();
                    break;
                case 2:
                    modifyBooleanExpression();
                    break;
                case 3:
                    deleteBooleanExpression();
                    break;
                case 0:
                    return;    
            }
        }
    }

    private void searchMenu() {
        while (true) {

        printToConsole(searchMenu);
        Integer command = getInputAsInt(0, 5, "Enter an option number:");
        switch (command) {
            case 1:
                authorsSearch();
                break;
            case 2:
                titlesSearch();
                break;
            case 3:
                allDocumentSearch();
                break;
            case 4:
                booleanExpressionSearch();
                break;
            case 5:
                System.out.println("Cerca per query");
                searchDocumentsByQuery();
                break;
            case 0:
                return;
        }
    }
}


    /**
     * Boolean Expresion Manager
    **/
    public void searchAllBooleanExpresions() {
        System.out.println("____Boolean Expresions____");
        HashMap<String,String> expresions = domain.getAllBooleanExpresions();
        if(expresions!=null){
            for (String string : expresions.keySet()) {
                System.out.println(string + "    " + expresions.get(string)); 
            }
        }
    }

    public void deleteBooleanExpression() {
        terminalIn.nextLine();
        printEnter("-Enter the Boolean Expresion Name:");
        String boolExpName = terminalIn.nextLine();
        if(!domain.deleteBooleanExpression(boolExpName)){
            printError("There was no saved expression with this name");
        }
        else printCorrect("Expression deleted");
    }

    public void modifyBooleanExpression(){
        //terminalIn.nextLine();
        printEnter("-Enter the Boolean Expresion Name:");
        String boolExpName = terminalIn.nextLine();
        if(!domain.existsBooleanExpression(boolExpName)) System.out.println(RED_BACKGROUND + "There is not a expression with this name" + RESET);
        else{
            printEnter("-Enter a Boolean Expresion:");
            String boolExp = terminalIn.nextLine();
            if (domain.addBooleanExpresion(boolExpName, boolExp)) {
                printCorrect("Title modified successfully");
            }
            else printError("The expression is not correct");
        }
    }

    public void addBooleanExpresion() {
        //terminalIn.nextLine();
        printEnter("-Enter the Boolean Expresion Name:");
        String boolExpName = terminalIn.nextLine();
        if(domain.existsBooleanExpression(boolExpName)) printError("There is already a expression with this name");
        else{
            printEnter("-Enter a Boolean Expresion:");
            String boolExp = terminalIn.nextLine();
            if (domain.addBooleanExpresion(boolExpName, boolExp)) {
                printCorrect("Title added successfully");
            }
            else printError("The expression is not correct");
        }
    }


    
    public ArrayList<String> getContentByInput(){
        printEnter("Enter the content of the document (enter *end* as the last line to finish): ");
        System.out.println("");
        ArrayList<String> content = new ArrayList<>();
        while(true){
            try {
                String s = terminalIn.nextLine();
                if(s.equals("*end*")) {
                    System.out.println("");
                    System.out.println(YELLOW_BOLD_BRIGHT + "End of the document" + RESET);
                    break;
                }
                content.add(s);
            }
            catch (NoSuchElementException eof) { //if there are no more elements catch the exception
                break;
            }
        }
        return content;
    }

    public void addDocument(){
        printEnter("Enter the name of the author: ");
        currentAuthor = terminalIn.nextLine();
        printEnter("Enter the title: ");
        currentTitle = terminalIn.nextLine();
        ArrayList<String> content = getContentByInput();
        if (domain.addDocument(currentTitle, currentAuthor, content)) {
            printCorrect("Document added successfully");
        }
        else printError("The Document with title '" + currentTitle + "' and author '" + currentAuthor + "' already exists");
    }

    public void openDocument(String titleName, String authorName){
        while(true){
            ArrayList<String> content = domain.getDocumentContent(titleName, authorName);
            if (content != null){
                printToConsole(documentHeader);
                System.out.println(WHITE_BOLD_BRIGHT + "Author: " + WHITE_BRIGHT+ authorName + RESET);
                System.out.println(WHITE_BOLD_BRIGHT +"Title:  "+ WHITE_BRIGHT + titleName + RESET);
                System.out.println("");
                for(String sentence: content){
                    System.out.println(sentence);
                }
                printToConsole(documentOptions);
                Integer command = getInputAsInt(0, 3, "Enter an option number:");
                switch(command){
                    case 1:
                        updateDocumentContent(titleName, authorName);
                        break;
                    case 2:
                        if(domain.deleteDocument(titleName, authorName)) printCorrect("Document deleted successfuly");
                        else printError("There has been an error when trying to delete the document");
                        return;
                    case 3:
                        similarDocumentsSearch(titleName, authorName);
                        currentAuthor = authorName;
                        currentTitle = titleName;
                        break;
                    case 0:
                        return;
                }
            } else {
                printError("The Document with title '" + titleName + "' and author '" + authorName + "' does not exist");
                return;
            }
        }

    }

    public void updateDocumentContent(String titleName, String authorName){
        ArrayList<String> content = getContentByInput();
        if(domain.updateDocument(titleName, authorName, content)){
            printCorrect("Document updated successfuly");
        } else printError("There has been an error when trying to update the document");
    }

    public void openDocumentByInput(){
        printEnter("Enter the name of the author: ");
        currentAuthor = terminalIn.nextLine();
        printEnter("Enter the title: ");
        currentTitle = terminalIn.nextLine();
        System.out.println("");
        openDocument(currentTitle, currentAuthor);
    }

    public void titlesOfAnAuthor(String authorName){
        ArrayList<String> titles = domain.getAllAuthorTitles(authorName);
        while(true){
            printToConsole(titlesOfAnAuthorHeader);
            for(int i = 0; i < titles.size(); ++i){
                System.out.println(WHITE_BOLD_BRIGHT + (i + 1) + " - " + WHITE_BRIGHT + titles.get(i) + RESET);
            }
            printToConsole(optionStrings);
            Integer command = getInputAsInt(0, 2, "Enter an option number:");
            switch(command){
                case 1:
                    titles = sortStringList(titles);
                    break;
                case 2:
                    currentTitle = titles.get(getInputAsInt(1, titles.size(), "Enter a title number:") - 1);
                    openDocument(currentTitle, currentAuthor);
                    return;
                case 0:
                    return;
                }
        }
    }

    public void authorsSearch(){
        printEnter("Enter an author name or a prefix:");
        String authorName = terminalIn.nextLine();
        ArrayList<String> authors = domain.getAllAuthors(authorName);
        if (authors.isEmpty()) printError("There are no authors in the system");
        else{
            while(true){
                printToConsole(authorsHeader);
                for(int i = 0; i < authors.size(); ++i){
                    System.out.println(WHITE_BOLD_BRIGHT + (i + 1) + " - " + WHITE_BRIGHT + authors.get(i) + RESET);
                }
                printToConsole(authorsOptions);
                Integer command = getInputAsInt(0, 2, "Enter an option number:");
                switch(command){
                    case 1:
                        authors = sortStringList(authors);
                        break;
                    case 2:
                        currentAuthor = authors.get(getInputAsInt(1, authors.size(), "Enter an author number:") - 1);
                        titlesOfAnAuthor(currentAuthor);
                        return;
                    case 0:
                        return;
                }
            }
        }
    }

    public void authorsOfATitle(String titleName){
        ArrayList<String> authors = domain.getAllTitleAuthors(titleName);
        while(true){
            printToConsole(authorOfATitleHeader);
            for(int i = 0; i < authors.size(); ++i){
                System.out.println(WHITE_BOLD_BRIGHT + (i + 1) + " - " + WHITE_BRIGHT + authors.get(i) + RESET);
            }
            printToConsole(optionStrings);
            Integer command = getInputAsInt(0, 2, "Enter an option number:");
            switch(command){
                case 1:
                    authors = sortStringList(authors);
                    break;
                case 2:
                    currentAuthor = authors.get(getInputAsInt(1, authors.size(), "Enter an author number:") - 1);
                    openDocument(currentTitle, currentAuthor);
                    return;
                case 0:
                    return;
            }
        }
    }
    
    public void titlesSearch(){
        printEnter("Enter a title name or a prefix:");
        String titleName = terminalIn.nextLine();
        ArrayList<String> titles = domain.getAllTitles(titleName);
        if (titles.isEmpty()) printError("There are no titles in the system");
        else{
            while(true){
                printToConsole(titlesHeader);
                for(int i = 0; i < titles.size(); ++i){
                     System.out.println(WHITE_BOLD_BRIGHT + (i + 1) + " - " + WHITE_BRIGHT + titles.get(i) + RESET);
                }
                printToConsole(titlesOptions);
                Integer command = getInputAsInt(0, 2, "Enter an option number:");
                switch(command){
                    case 1:
                        titles = sortStringList(titles);
                        break;
                    case 2:
                        currentTitle = titles.get(getInputAsInt(1, titles.size(), "Enter a title number:") - 1);
                        authorsOfATitle(currentTitle);
                        return;
                    case 0:
                        return;
                }
            }
        }
    }

    public void allDocumentSearch(){
        ArrayList<DocumentInfo> documentsInfo = domain.getAllDocumentsInfo();
        if(documentsInfo.isEmpty()) printError("There are no documents in the system");
        else {
            while(true){
                printToConsole(allDocumentsHeader);
                for(int i = 0; i < documentsInfo.size(); ++i){
                    System.out.println(WHITE_BOLD_BRIGHT + (i + 1) + " - " + WHITE_BRIGHT + documentsInfo.get(i).toString() + RESET);
                }
                printToConsole(optionStrings);
                Integer command = getInputAsInt(0, 2, "Enter an option number:");
                switch(command){
                    case 1:
                        documentsInfo = sortDocumentInfoList(documentsInfo);
                        break;
                    case 2:
                        int selectedDocument = getInputAsInt(1, documentsInfo.size(), "Enter a document number:");
                        DocumentInfo doc = documentsInfo.get(selectedDocument - 1);
                        currentAuthor = doc.author();
                        currentTitle = doc.title();
                        openDocument(currentTitle, currentAuthor);
                        return;
                    case 0:
                        return;
                }
            }
        }
    }

    /**
     * Complex Search
    **/
    private void similarDocumentsSearch(String titleName, String authorName){
        int k = getInputAsInt(0, 100,"Enter the number of documents showed:");
        ArrayList<DocumentInfo> similarDocuments = domain.getInstance().similarDocumentsSearch(titleName, authorName, k);
        if (similarDocuments == null) printError("No Similar Documents Found!!!");
        while(similarDocuments != null){
            for(int i = 0; i < similarDocuments.size(); ++i){
                System.out.println(WHITE_BOLD_BRIGHT + (i + 1) + " - " + WHITE_BRIGHT + similarDocuments.get(i).toString() + RESET);
            }
            printToConsole(similarDocumentsOptions);
            Integer command = getInputAsInt(0, 2, "Enter an option number:");
            switch (command) {
                case 1:
                    similarDocuments = sortDocumentInfoList(similarDocuments);
                    break;
                case 2:
                    if(similarDocuments.size() > 0){
                        int docNumber = getInputAsInt(1, similarDocuments.size(), "Enter the number of the document:");
                        currentTitle = similarDocuments.get(docNumber - 1).title();
                        currentAuthor = similarDocuments.get(docNumber - 1).author();
                        openDocument(currentTitle, currentAuthor);
                    } else {
                        printError("There are no documents to open");
                    }
                    return;
                case 0:
                    return;
            }
        }
    }
    
    public void booleanExpressionSearch(){
        System.out.println("_______________________Options________________________" );
        System.out.println("   1 - Use a saved expression");
        System.out.println("   2 - New expression");

        ArrayList<DocumentInfo> docsInfo = new ArrayList<>();

        Integer command = getInputAsInt(1,2, "Enter an option number:");
        if (command == 1) {
            printEnter("-Enter the Boolean Expresion Name:");
            String boolExpName = terminalIn.nextLine();
            docsInfo = domain.storedBooleanExpressionSearch(boolExpName);
            
            if (docsInfo == null) {
                printError("There is not a expression with this name");
                return;
            }
        }

        else {
            printEnter("-Enter the Boolean Expresion:");
            String boolExp = terminalIn.nextLine();
            docsInfo = domain.tempBooleanExpressionSearch(boolExp);
            if (docsInfo == null) {
                printError("Invalid expression");
                return;
            }
        }
        
        if (docsInfo.isEmpty()) {
            printError("There is not a Sentence in all the Documents that satisfy this expresion");
            return;
        }
        else {
            printToConsole(allDocumentsHeader);
            for(int i = 0; i < docsInfo.size(); ++i){
                System.out.println(WHITE_BOLD_BRIGHT + (i + 1) + " - " + WHITE_BRIGHT + docsInfo.get(i).toString() + RESET);
            }
        }
    }

    public void searchDocumentsByQuery(){
        printEnter("Enter a query:");
        String query = terminalIn.nextLine();
        if (query.isEmpty()){
            printError("The query entered is empty");
            return;
        }
        ArrayList<DocumentInfo> queryDocs = domain.documentsByQuery(query);
        if(queryDocs.isEmpty()){ 
            printError("There are no documents that satisfy the query");
            return;
        }
        while(true){
            printToConsole(queryDocumentsHeader);
            for(int i = 0; i < queryDocs.size(); ++i){
                System.out.println(WHITE_BOLD_BRIGHT + (i + 1) + " - " + WHITE_BRIGHT + queryDocs.get(i).toString() + RESET);
            }
            printToConsole(similarDocumentsOptions);
            Integer command = getInputAsInt(0, 2, "Enter an option number:");
            switch (command) {
                case 1:
                    queryDocs = sortDocumentInfoList(queryDocs);
                    break;
                case 2:
                    int docNumber = getInputAsInt(1, queryDocs.size(), "Enter the number of the document:");
                    currentTitle = queryDocs.get(docNumber - 1).title();
                    currentAuthor = queryDocs.get(docNumber - 1).author();
                    openDocument(currentTitle, currentAuthor);
                    return;
                case 0:
                    return;
            }
        }
    }


    /**
     * Sorting functions
    **/
    private ArrayList<String> sortStringList(ArrayList<String> l){
        printToConsole(sortingArrayMenu);
        Integer command = getInputAsInt(0, 2, "Enter an option number:");
        switch (command) {
            case 1:
                l.sort(Comparator.naturalOrder());
                break;
            case 2:
                l.sort(Comparator.reverseOrder());
                break;
            case 0:
                break;
            }
        return l;
    }

    private ArrayList<DocumentInfo> sortDocumentInfoList(ArrayList<DocumentInfo> docInfos){
        printToConsole(sortingDocumentsMenu);
        Integer command = getInputAsInt(0, 8, "Enter an option number:");
        switch (command) {
            case 1:
                docInfos.sort((doc1, doc2)->doc1.title().compareTo(doc2.title()));
                break;
            case 2:
                docInfos.sort((doc1, doc2)->doc2.title().compareTo(doc1.title()));
                break;
            case 3:
                docInfos.sort((doc1, doc2)->doc1.author().compareTo(doc2.author()));
                break;
            case 4:
                docInfos.sort((doc1, doc2)->doc2.author().compareTo(doc1.author()));
                break;
            case 5:
                docInfos.sort((doc1, doc2)->doc1.creationDate().compareTo(doc2.creationDate()));
                break;
            case 6:
                docInfos.sort((doc1, doc2)->doc2.creationDate().compareTo(doc1.creationDate()));
                break;
            case 7:
                docInfos.sort((doc1, doc2)->doc1.modificationDate().compareTo(doc2.modificationDate()));
                break;
            case 8:
                docInfos.sort((doc1, doc2)->doc2.modificationDate().compareTo(doc1.modificationDate()));
                break;
            case 0:
                break;
            }
        return docInfos;
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    public static final String RESET = "\033[0m";  // Text Reset

    
    // Regular Colors
    public static final String BLACK = "\033[0;30m";   // BLACK
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    public static final String CYAN = "\033[0;36m";    // CYAN
    public static final String WHITE = "\033[0;37m";   // WHITE

    // Bold
    public static final String BLACK_BOLD = "\033[1;30m";  // BLACK
    public static final String RED_BOLD = "\033[1;31m";    // RED
    public static final String GREEN_BOLD = "\033[1;32m";  // GREEN
    public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
    public static final String BLUE_BOLD = "\033[1;34m";   // BLUE
    public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
    public static final String CYAN_BOLD = "\033[1;36m";   // CYAN
    public static final String WHITE_BOLD = "\033[1;37m";  // WHITE

    // Underline
    public static final String BLACK_UNDERLINED = "\033[4;30m";  // BLACK
    public static final String RED_UNDERLINED = "\033[4;31m";    // RED
    public static final String GREEN_UNDERLINED = "\033[4;32m";  // GREEN
    public static final String YELLOW_UNDERLINED = "\033[4;33m"; // YELLOW
    public static final String BLUE_UNDERLINED = "\033[4;34m";   // BLUE
    public static final String PURPLE_UNDERLINED = "\033[4;35m"; // PURPLE
    public static final String CYAN_UNDERLINED = "\033[4;36m";   // CYAN
    public static final String WHITE_UNDERLINED = "\033[4;37m";  // WHITE

    // Background
    public static final String BLACK_BACKGROUND = "\033[40m";  // BLACK
    public static final String RED_BACKGROUND = "\033[41m";    // RED
    public static final String GREEN_BACKGROUND = "\033[42m";  // GREEN
    public static final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW
    public static final String BLUE_BACKGROUND = "\033[44m";   // BLUE
    public static final String PURPLE_BACKGROUND = "\033[45m"; // PURPLE
    public static final String CYAN_BACKGROUND = "\033[46m";   // CYAN
    public static final String WHITE_BACKGROUND = "\033[47m";  // WHITE

    // High Intensity
    public static final String BLACK_BRIGHT = "\033[0;90m";  // BLACK
    public static final String RED_BRIGHT = "\033[0;91m";    // RED
    public static final String GREEN_BRIGHT = "\033[0;92m";  // GREEN
    public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
    public static final String BLUE_BRIGHT = "\033[0;94m";   // BLUE
    public static final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
    public static final String CYAN_BRIGHT = "\033[0;96m";   // CYAN
    public static final String WHITE_BRIGHT = "\033[0;97m";  // WHITE

    // Bold High Intensity
    public static final String BLACK_BOLD_BRIGHT = "\033[1;90m"; // BLACK
    public static final String RED_BOLD_BRIGHT = "\033[1;91m";   // RED
    public static final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
    public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
    public static final String BLUE_BOLD_BRIGHT = "\033[1;94m";  // BLUE
    public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE
    public static final String CYAN_BOLD_BRIGHT = "\033[1;96m";  // CYAN
    public static final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE

    // High Intensity backgrounds
    public static final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";// BLACK
    public static final String RED_BACKGROUND_BRIGHT = "\033[0;101m";// RED
    public static final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";// GREEN
    public static final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// YELLOW
    public static final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";// BLUE
    public static final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m"; // PURPLE
    public static final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m";  // CYAN
    public static final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m";   // WHITE


}
