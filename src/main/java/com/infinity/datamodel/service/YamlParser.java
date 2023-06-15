package com.infinity.datamodel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.infinity.datamodel.domain.Diagram;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class YamlParser {




    public Diagram parseYAML() throws IOException {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.registerModule(new Jdk8Module());
        return mapper.readValue(new File("src/main/resources/data.yml"), Diagram.class);


    }
}
