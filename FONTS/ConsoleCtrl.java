import DomainLayer.Classes.AuthorCtrl;
import DomainLayer.Classes.DomainCtrl;
import DomainLayer.Classes.TitleCtrl;

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
                        "   3   Add a title in the sistem",
                        "   4   Add a Boolean expresion in the sistem",
                        "   5   Make a Search",
                        "   0   Print this messatge"
    };

    String[] search = {   "\n",
                        "These are the avileble seaches:",
                        "   1   List all the Authors",
                        "   2   List all the Titles",
                        "   3   List all the Documents with his ID, Title and Author",
                        "   4   List all the Boolean expresions",
                        "   5   List the Document Title of an Author",
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
            //System.out.print(">");
            
            command = in.nextInt();
            switch (command) {
                case 0:
                    printHelp();
                    break;
                case 1:
                    addDocument();
                    break;
                case 2:
                    System.out.print("Enter Author name:");
                    String a = in.nextLine();
                    
                    break;
                case 3:
                    System.out.println("Enter Title name:");

                    result = domain.addTitle(in.nextLine());
                    System.out.println(result);
                    break;
                case 4:
                    System.out.println("Enter the Boolean Expresion:");
                    in.nextLine();
                    System.out.println("This do nothing");
                    break;
                case 5:
                    search();
                default:
                    System.out.println("invalid command, please enter a valid command or help for help");
                    break;
                     
            }
            //in.close();
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
        String authorName = in.nextLine();
        System.out.println("Enter Title name:");
        String titleName = in.nextLine();
        System.out.println("Enter the Document content:");
        List<String> content = new ArrayList<String>();
        while (in.hasNextLine()) {
            content.add(in.nextLine());
        }
        System.out.println(domain.addDocument(titleName, authorName, content));
    }

    private void search() {
        for (String s : search){
            System.out.println(s);
        }
        System.out.println("");
        System.out.println("Enter a command:");
        Integer command = in.nextInt();
        switch (command) {
            case 1:
                Set<String> a = AuthorCtrl.getInstance().getAllAuthorsNames();
                for (String string : a) {
                    System.out.println(string);
                }
                break;
            case 2:
                Set<String> t = TitleCtrl.getInstance().getAllTitlesNames();
                for (String string : t) {
                    System.out.println(string);
                }
                break;
            default:
                break;
        }
    }


}
