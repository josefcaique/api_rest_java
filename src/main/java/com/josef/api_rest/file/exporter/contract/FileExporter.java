package com.josef.api_rest.file.exporter.contract;

import com.josef.api_rest.data.dto.v1.PersonDTO;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.util.List;

public interface FileExporter {

    Resource exportFile(List<PersonDTO> people) throws Exception;
}
