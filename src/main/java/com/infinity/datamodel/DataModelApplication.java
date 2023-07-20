package com.infinity.datamodel;

import com.infinity.datamodel.domain.Conf;
import com.infinity.datamodel.service.DrawIoExporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

@Slf4j @SpringBootApplication public class DataModelApplication {

    public static void main(String[] args) {

        SpringApplication.run(DataModelApplication.class, args);

    }

    @Bean public Object setArgs(ApplicationArguments arguments) {

        return new Conf(arguments);
    }

}
