package com.infinity.datamodel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.infinity.datamodel.domain.Conf;
import com.infinity.datamodel.domain.Diagram;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component @Slf4j public class YamlParser {

    private final Conf conf;

    public YamlParser(Conf conf) {
        this.conf = conf;
    }

    public Diagram parseYAML() throws IOException {

        log.info("Parsing {}", this.conf.getModelPath());
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.registerModule(new Jdk8Module());
        return mapper.readValue(new File(this.conf.getModelPath()), Diagram.class);

    }
}
