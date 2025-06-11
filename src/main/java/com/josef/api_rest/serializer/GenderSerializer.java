package com.josef.api_rest.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class GenderSerializer extends JsonSerializer<String> {

    // Define just two types of gender
    @Override
    public void serialize(String gender, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String formatedGender = "Male".equals(gender) ? "F" : "M";
        jsonGenerator.writeString(formatedGender);
    }
}
