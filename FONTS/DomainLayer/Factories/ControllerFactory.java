package DomainLayer.Factories;

import DomainLayer.Interfaces.AuthorController;
import DomainLayer.Interfaces.TitleController;
import DomainLayer.Interfaces.WordController;
import DomainLayer.Interfaces.DocumentController;

import DataLayer.Classes.DataBase;


public class ControllerFactory {
    private static ControllerFactory instance = null;

    private static WordController wordController;
    private static AuthorController authorController;
    private static TitleController titleController;
    private static DocumentController documentController;

    public static ControllerFactory getInstance(){
        if (instance == null) {
            instance = new ControllerFactory();
            wordController = DataBase.getInstance();
            authorController = DataBase.getInstance();
            titleController = DataBase.getInstance();
            documentController = DataBase.getInstance();
        }
        return instance;
    }

    public WordController getWordController(){
        return wordController;
    }

    public AuthorController getAuthorController(){
        return authorController;
    }

    public TitleController getTitleController(){
        return titleController;
    }

    public DocumentController getDocumentController(){
        return documentController;
    }
}