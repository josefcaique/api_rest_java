package com.josef.api_rest.file.importer.contract;

import com.josef.api_rest.data.dto.v1.PersonDTO;

import java.io.InputStream;
import java.util.List;

public interface FileImporter {

    List<PersonDTO> importFile(InputStream inputStream) throws Exception;
}
