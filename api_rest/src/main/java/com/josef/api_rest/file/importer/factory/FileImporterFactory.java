package com.josef.api_rest.file.importer.factory;

import com.josef.api_rest.exception.FileNotSupportedException;
import com.josef.api_rest.file.importer.contract.FileImporter;
import com.josef.api_rest.file.importer.impl.CsvImporter;
import com.josef.api_rest.file.importer.impl.XlsxImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FileImporterFactory {

    private Logger logger = LoggerFactory.getLogger(FileImporterFactory.class);

    @Autowired
    private ApplicationContext context;

    public FileImporter getImporter(String fileName) throws Exception {
        if (fileName.endsWith(".xlsx")) {
            return context.getBean(XlsxImporter.class);
        } else if (fileName.endsWith(".csv")) {
            return context.getBean(CsvImporter.class);
        }

        throw new FileNotSupportedException("Invalid file format!");
    }
}
