import Domain.Document;
import Domain.DomainCtrl;

import java.util.HashSet;
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
                        "   load        Load a file",
                        "   create      Create an empty Document",
                        "   save        Seve a document",
                        "   export      Export the document into a new file",
                        "   search      Make a search",
                        "   exit        Exit the program",
                        "   doclist     List all the docs on the sistem",
                        "   help        Print this messatge"
    };


    /**
     * Constructor
     */
    public ConsoleCtrl() {
    }
    
    public void launchPresentation() {
        //logo

        printLogo();
        printHelp(); //print the commands
        

        String command;
        Scanner in = new Scanner(System.in);
        

        while (true) {
            System.out.print(">");
            command = in.nextLine();
            switch (command) {
                case "help":
                    printHelp();
                    break;

                case "load":
                    System.out.println("Enter the full file path:");
                    DomainCtrl.getInstance().loadFile(in.nextLine());
                    break;
                case "exit":
                    exit();
                case "":
                    break;
                case "doclist":
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

    private Set<Document> getDocuments() {
        return DomainCtrl.getInstance().getDocs();
    } 

}
