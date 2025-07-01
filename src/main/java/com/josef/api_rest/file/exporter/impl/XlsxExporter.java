package com.josef.api_rest.file.exporter.impl;

import com.josef.api_rest.data.dto.v1.PersonDTO;
import com.josef.api_rest.file.exporter.contract.FileExporter;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class XlsxExporter implements FileExporter {

    @Override
    public Resource exportFile(List<PersonDTO> people) throws Exception {

        return null;
    }
}
