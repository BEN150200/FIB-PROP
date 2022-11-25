package src.persistance.documentimporter;

import src.enums.Format;
import src.persistance.documentimporter.DocumentImporter;
import src.persistance.documentimporter.DocumentImporterTXT;
import src.persistance.documentimporter.DocumentImporterXML;

import java.util.HashMap;

public class DocumentImporterFactory {
    private static DocumentImporterFactory instance = null;

    private static HashMap<Format, DocumentImporter> importers = new HashMap<Format, DocumentImporter>();

    public static DocumentImporterFactory getInstance(){
        if(instance == null){
            instance = new DocumentImporterFactory();
            importers.put(Format.TXT, new DocumentImporterTXT());
            importers.put(Format.XML, new DocumentImporterXML());
            //importers.put(Format.PROP, new ImporterPROP());
        }
        return instance;
    }

    public DocumentImporter getImporter(Format fileFormat){
        return importers.get(fileFormat);
    }
}
