package com.infinity.datamodel;

import com.infinity.datamodel.service.DrawIoExporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

@Slf4j @SpringBootApplication public class DataModelApplication {

    public static void main(String[] args) throws IOException, ParserConfigurationException, TransformerException {

        String modelPath = "";
        String exportPath = "";
        SpringApplication.run(DataModelApplication.class, args);

        for (int i = 0; i < args.length; i++) {
            log.info(args[i]);
            if (args[i].equals("-M")) {
                modelPath = args[i + 1];
            }
            if (args[i].equals("-F")) {

                exportPath = args[i + 1];

            }
        }

        new DrawIoExporter().export(modelPath, exportPath);

    }
}
