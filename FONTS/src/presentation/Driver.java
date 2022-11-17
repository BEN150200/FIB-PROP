package src.presentation;

import src.domain.controllers.DomainCtrl;
import src.domain.core.DocumentInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.HashMap;

public class Driver{


    /**
     * Attributes
     */

    Set<Integer> documents = new HashSet<Integer>();

    String[] logo = {
        "",
        "___  ___                                __ _     _    _               _ ",
		"|  \\/  |                               / _| |   | |  | |             | |",
		"| .  . | __ _  ___ _ __ ___  ___  ___ | |_| |_  | |  | | ___  _ __ __| |",
		"| |\\/| |/ _` |/ __| '__/ _ \\/ __|/ _ \\|  _| __| | |/\\| |/ _ \\| '__/ _` |",
		"| |  | | (_| | (__| | | (_) \\__ \\ (_) | | | |_  \\  /\\  / (_) | | | (_| |",
		"\\_|  |_/\\__,_|\\___|_|  \\___/|___/\\___/|_|  \\__|  \\/  \\/ \\___/|_|  \\__,_|",
		""
    };

    String[] mainMenu = {  
                        "",
                        "_____________________Main Menu_______________________",
                        "Choose the type of operation:",
                        "   1 - Add document",
                        "   2 - Open document",
                        "   3 - Manage boolean expressions",
                        "   4 - Search in the system",
                        "   0 - Exit",
                        "_____________________________________________________",
                        "",
    };

    String[] booleanExpressionsMenuHeader = {
                        "",
                        "________________Boolean Expressions__________________",
                        ""
    };

    String[] booleanExpressionsMenuOptions = {
                        "",
                        "___________Boolean Expressions options_______________",
                        "Choose the type of operation:",
                        "   1 - Add a new boolean expression",
                        "   2 - Modify an existing boolean expression",
                        "   3 - Delete an existing boolean expression",
                        "   0 - Return to Main Menu",
                        "_____________________________________________________",
                        ""
    };

    String[] booleanSearchOptions = {
                        "",
                        "______________Boolean Expressions Search_____________",
                        "Choose the type of operation:",
                        "   1 - Use a saved expression",
                        "   2 - New expression",
                        "_____________________________________________________",
                        ""
    };
                    
    String[] searchMenu = {
                        "",
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
                        "_______________________Authors_______________________",
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

    String[] optionStrings = {
                        "",
                        "______________________Options________________________",
                        "   1 - Sort results",
                        "   2 - Open document",
                        "   0 - Exit",
                        "_____________________________________________________",
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
    public Driver() {
        domain = DomainCtrl.getInstance();
    }

    /**
     * Private Functions
    **/
    private void exit() {
        System.out.println("\n");
        System.out.println("Closing the program");
        System.out.println("\n");
        System.exit(0);
    }

    private void printError(String error) {
        System.out.println(error);
    }

    private void printCorrect(String message) {
        System.out.println(message);
    }

    private void printEnter(String message) {
        System.out.println(message);
    }

    private int getInputAsInt(int min, int max, String message) {
        System.out.println(message);
        Integer command;
        while(true){
            try{
                command = terminalIn.nextInt();
                if(min > command || max < command){
                    System.out.println("Please enter a number from " + min + " to " + max);
                } else break;
            } catch(Exception e){
                terminalIn.nextLine();
                System.out.println("Please enter a valid option (number)");
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
                    addBooleanExpression();
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
        printEnter("-Enter the Boolean Expression Name:");
        String boolExpName = terminalIn.nextLine();
        if(!domain.existsBooleanExpression(boolExpName)) System.out.println("There is not a expression with this name");
        else{
            printEnter("-Enter a Boolean Expression:");
            String boolExp = terminalIn.nextLine();
            if (domain.addBooleanExpression(boolExpName, boolExp)) {
                printCorrect("Boolean Expression modified successfully");
            }
            else printError("The expression is not correct");
        }
    }

    public void addBooleanExpression() {
        //terminalIn.nextLine();
        printEnter("-Enter the Boolean Expresion Name:");
        String boolExpName = terminalIn.nextLine();
        if(domain.existsBooleanExpression(boolExpName)) printError("There is already a expression with this name");
        else{
            printEnter("-Enter a Boolean Expresion:");
            String boolExp = terminalIn.nextLine();
            if (domain.addBooleanExpression(boolExpName, boolExp)) {
                printCorrect("Boolean Expression added successfully");
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
                    System.out.println("End of the document");
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
                System.out.println("Author: " + authorName);
                System.out.println("Title:  " + titleName);
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
            printToConsole(titlesHeader);
            for(int i = 0; i < titles.size(); ++i){
                System.out.println((i + 1) + " - " + titles.get(i));
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
                    System.out.println((i + 1) + " - " + authors.get(i));
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
            printToConsole(authorsHeader);
            for(int i = 0; i < authors.size(); ++i){
                System.out.println((i + 1) + " - " + authors.get(i));
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
                     System.out.println((i + 1) + " - " + titles.get(i));
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
                    System.out.println((i + 1) + " - " + documentsInfo.get(i).toString());
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
        int k = getInputAsInt(0, 100,"Enter the number of documents showed or 0 if you want the default number of Documents(5):");
        ArrayList<DocumentInfo> similarDocuments = domain.similarDocumentsSearch(titleName, authorName, k);
        if (similarDocuments == null) printError("No Similar Documents Found!!!");
        while(similarDocuments != null){
            for(int i = 0; i < similarDocuments.size(); ++i){
                DecimalFormat df = new DecimalFormat("0,00");
                System.out.println((i + 1) + " - " + similarDocuments.get(i).toString() + "  " + df.format(similarDocuments.get(i).semblance()*100) + "%");
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
        printToConsole(booleanSearchOptions);
        
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
                System.out.println((i + 1) + " - " + docsInfo.get(i).toString());
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
                System.out.println((i + 1) + " - " + queryDocs.get(i).toString());
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


}
