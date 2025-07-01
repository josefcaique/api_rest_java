package com.josef.api_rest.file.exporter.factory;

import com.josef.api_rest.exception.FileNotSupportedException;
import com.josef.api_rest.file.exporter.MediaTypes;
import com.josef.api_rest.file.exporter.contract.FileExporter;
import com.josef.api_rest.file.exporter.impl.CsvExporter;
import com.josef.api_rest.file.exporter.impl.XlsxExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FileExporterFactory {

    private Logger logger = LoggerFactory.getLogger(FileExporterFactory.class);

    @Autowired
    private ApplicationContext context;

    public FileExporter getExporter(String acceptHeader) throws Exception {
        if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_XLSX_VALUE)) {
            return context.getBean(XlsxExporter.class);
        } else if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_CSV_VALUE)) {
            return context.getBean(CsvExporter.class);
        }

        throw new FileNotSupportedException("Invalid file format!");
    }
}
