package src.persistance.controllers;

import src.domain.core.DocumentInfo;
import src.enums.Format;
import src.persistance.documentexporter.DocumentExporter;
import src.persistance.documentexporter.DocumentExporterFactory;
import src.persistance.documentimporter.DocumentImporter;
import src.persistance.documentimporter.DocumentImporterFactory;
import src.persistance.datamanagement.BEDataManager;
import src.persistance.datamanagement.DocumentsDataManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

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

    public void exportToFile(String titleName, String authorName, ArrayList<String> content, Format fileFormat, String path) throws IOException, Exception{
        DocumentExporterFactory exporterFactory = DocumentExporterFactory.getInstance();
        DocumentExporter documentExporter = exporterFactory.getExporter(fileFormat);
        documentExporter.exportToFile(titleName, authorName, content, path);
    }

    public DocumentInfo importFromFile(String path, Format fileFormat) throws FileNotFoundException, IOException, Exception{
        DocumentImporterFactory importerFactory = DocumentImporterFactory.getInstance();
        DocumentImporter documentImporter = importerFactory.getImporter(fileFormat);
        return documentImporter.importFromFile(path);
    }

    public void saveDocumentsData(ArrayList<DocumentInfo> docsInfos) throws ParserConfigurationException, IOException, FileNotFoundException, TransformerConfigurationException, TransformerException{
        documentsDataManager.saveData(docsInfos);
    }

    public ArrayList<DocumentInfo> loadDocumentsData() throws ParserConfigurationException, SAXException, IOException{
        return documentsDataManager.loadData();
    }

    public void saveBooleanexpressionsData(ArrayList<String> expressionNames, ArrayList<String> expressions) throws ParserConfigurationException, IOException, TransformerConfigurationException, TransformerException{
        beDataManager.saveData(expressionNames, expressions);
    }

    public ArrayList<String> loadExpressionsNames() throws ParserConfigurationException, SAXException, IOException{
        return beDataManager.loadExpressionNames();
    }

    public ArrayList<String> loadExpressions() throws ParserConfigurationException, SAXException, IOException{
        return beDataManager.loadExpressions();
    }

    // Deletes boolean expressions and documents from their backup files.
    public void clearData() throws ParserConfigurationException, IOException, TransformerConfigurationException, TransformerException{
        beDataManager.clearData();
        documentsDataManager.clearData();
    }

}
