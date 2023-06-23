package com.infinity.datamodel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.infinity.datamodel.domain.Diagram;
import lombok.extern.slf4j.Slf4j;


import java.io.File;
import java.io.IOException;

@Slf4j public class YamlParser {


    public Diagram parseYAML(String modelPath) throws IOException {

        log.info("Parsing {}", modelPath);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.registerModule(new Jdk8Module());
        return mapper.readValue(new File(modelPath), Diagram.class);

    }
}
