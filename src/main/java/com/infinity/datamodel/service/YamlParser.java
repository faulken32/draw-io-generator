package com.infinity.datamodel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.infinity.datamodel.domain.Diagram;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class YamlParser {




    public Diagram parseYAML() throws IOException {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        return mapper.readValue(new File("src/main/resources/data.yml"), Diagram.class);


    }
}
