import Domain.Document;
import Domain.DomainCtrl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

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

    String[] help = {   "\n",
                        "These are the avileble commands:",
                        "   1   Add a document in the sistem",
                        "   2   Add an autor in the sistem",
                        "   3   Add a new title in the sistem",
                        "   ",
                        "   7   Make a search",
                        "   8   Exit the program",
                        "   9   List all the docs on the sistem",
                        "   0   Print this messatge"
    };
    Scanner in = new Scanner(System.in);

    /**
     * Constructor
     */
    private DomainCtrl domain;

    public ConsoleCtrl() {
        domain = DomainCtrl.getInstance();
    }
    
    public void launchPresentation() {
        //logo

        printLogo();
        printHelp(); //print the commands
        

        Integer command;
        
        

        while (true) {
            boolean result;
            System.out.print(">");
            command = in.nextInt();
            switch (command) {
                case 0:
                    printHelp();
                    break;
                case 1:
                    addDocument();
                    break;
                case 2:
                    System.out.println("Enter Author name:");
                    result = domain.addAuthor(in.nextLine());
                    System.out.println(result);
                    break;
                case 3:
                    System.out.println("Enter Title name:");
                    result = domain.addTitle(in.nextLine());
                    System.out.println(result);
                    break;

                case 7:
                    System.out.println("Enter the full file path:");
                    result = domain.loadFile(in.nextLine());
                    System.out.println(result);
                    break;
                case 8:
                case 9:
                    Set<Document> docs = DomainCtrl.getInstance().getDocs();
                    for (Document document : docs) {
                        System.out.println(document.getTitle());
                        
                    }
                    break;
                default:
                    System.out.println("invalid command, please enter a valid command or help for help");
                    break;

                     
            }
        }
    }

    private void exit() {
        System.exit(0);
    }

    private void printHelp() {
        for (String h : help){
            System.out.println(h);
        }
        System.out.println("");
        System.out.println("Enter a command:");
    }

    private void printLogo() {
        for (String l : logo){
            System.out.println(l);
        }
    }

    private void addDocument() {
        System.out.println("Enter Author name:");
        String a = in.nextLine();
        System.out.println("Enter Title name:");
        String t = in.nextLine();
        System.out.println("Enter the Document content:");
        List<String> content = new ArrayList<String>();
        while (in.hasNextLine()) {
            content.add(in.nextLine());
        }
        domain.addDocument(a, t, content);
    }
    
    private Set<Document> getDocuments() {
        return DomainCtrl.getInstance().getDocs();
    } 

}
