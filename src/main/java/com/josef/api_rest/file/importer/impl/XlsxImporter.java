package com.josef.api_rest.file.importer.impl;

import com.josef.api_rest.data.dto.v1.PersonDTO;
import com.josef.api_rest.file.importer.contract.FileImporter;

import java.io.InputStream;
import java.util.List;

public class XlsxImporter implements FileImporter {
    @Override
    public List<PersonDTO> importFile(InputStream inputStream) throws Exception {
        return List.of();
    }
}
