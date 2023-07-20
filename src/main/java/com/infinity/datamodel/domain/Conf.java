package com.infinity.datamodel.domain;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;

import org.springframework.context.annotation.Configuration;

@Configuration @Slf4j @Data public class Conf {

    private String modelPath;
    private String exportPath;

    public Conf(ApplicationArguments args) {

        String[] sourceArgs = args.getSourceArgs();

        for (int i = 0; i < sourceArgs.length; i++) {
            log.info(sourceArgs[i]);
            if (sourceArgs[i].equals("-M")) {
                modelPath = sourceArgs[i + 1];
            }
            if (sourceArgs[i].equals("-F")) {

                exportPath = sourceArgs[i + 1];

            }
        }
    }
}
