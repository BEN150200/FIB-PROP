package src.persistance.documentexporter;

import src.enums.Format;
import src.persistance.documentexporter.DocumentExporter;
import src.persistance.documentexporter.DocumentExporterTXT;
import src.persistance.documentexporter.DocumentExporterXML;

import java.util.HashMap;

public class DocumentExporterFactory {
    private static DocumentExporterFactory instance = null;

    private static HashMap<Format, DocumentExporter> exporters = new HashMap<Format, DocumentExporter>();

    public static DocumentExporterFactory getInstance(){
        if(instance == null){
            instance = new DocumentExporterFactory();
            exporters.put(Format.TXT, new DocumentExporterTXT());
            exporters.put(Format.XML, new DocumentExporterXML());
            //exporters.put(Format.PROP, new ExporterPROP());
        }
        return instance;
    }

    public DocumentExporter getExporter(Format fileFormat){
        return exporters.get(fileFormat);
    }
}
