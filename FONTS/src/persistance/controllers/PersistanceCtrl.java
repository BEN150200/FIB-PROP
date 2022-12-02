package src.persistance.controllers;

import src.domain.core.DocumentInfo;
import src.enums.Format;
import src.persistance.documentexporter.DocumentExporter;
import src.persistance.documentexporter.DocumentExporterFactory;
import src.persistance.documentimporter.DocumentImporter;
import src.persistance.documentimporter.DocumentImporterFactory;
import src.persistance.datamanagement.BEDataManager;
import src.persistance.datamanagement.DocumentsDataManager;

import java.util.ArrayList;

public class PersistanceCtrl {
    private static PersistanceCtrl instance = null;
    private static BEDataManager beDataManager = null;
    private static DocumentsDataManager documentsDataManager = null;

    private PersistanceCtrl() {
        beDataManager = new BEDataManager();
        documentsDataManager = new DocumentsDataManager();
    }

    public static PersistanceCtrl getInstance(){
        if(instance == null){
            instance = new PersistanceCtrl();
        }
        return instance;
    }

    public boolean exportToFile(String titleName, String authorName, ArrayList<String> content, Format fileFormat, String path){
        DocumentExporterFactory exporterFactory = DocumentExporterFactory.getInstance();
        DocumentExporter documentExporter = exporterFactory.getExporter(fileFormat);
        return documentExporter.exportToFile(titleName, authorName, content, path);
    }

    public DocumentInfo importFromFile(String path, Format fileFormat){
        DocumentImporterFactory importerFactory = DocumentImporterFactory.getInstance();
        DocumentImporter documentImporter = importerFactory.getImporter(fileFormat);
        return documentImporter.importFromFile(path);
    }

    public boolean saveDocumentsData(ArrayList<DocumentInfo> docsInfos){

        return documentsDataManager.saveData(docsInfos);
    }

    public ArrayList<DocumentInfo> loadDocumentsData(){
        return documentsDataManager.loadData();
    }

    public boolean saveBooleanexpressionsData(ArrayList<String> expressionNames, ArrayList<String> expressions){
        return beDataManager.saveData(expressionNames, expressions);
    }

    public ArrayList<String> loadExpressionsNames(){
        return beDataManager.loadExpressionNames();
    }

    public ArrayList<String> loadExpressions(){
        return beDataManager.loadExpressions();
    }

    // Deletes boolean expressions and documents from their backup files.
    public boolean clearData(){
        if(!beDataManager.clearData()) return false;
        if(!documentsDataManager.clearData()) return false;
        return true;
    }

}
