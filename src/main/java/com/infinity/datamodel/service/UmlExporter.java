//package com.infinity.datamodel.service;
//
//import com.infinity.datamodel.domain.Component;
//import com.infinity.datamodel.domain.Data;
//import com.infinity.datamodel.domain.Relation;
//import jakarta.annotation.PostConstruct;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.List;
//
//@Service @Slf4j public class UmlExporter {
//
//    private final YamlParser parser;
//
//    public UmlExporter(YamlParser parser) {
//        this.parser = parser;
//    }
//
//    @PostConstruct private void export() {
//        StringBuilder sb = new StringBuilder();
//        Data data = null;
//        try {
//            data = parser.parseYAML();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        // Header
//        sb.append("@startuml\n\n");
//
//        // Component
//        List<Component> components = data.getComponent();
//
//        for (Component component : components) {
//
//            if (!component.getGroup().isBlank()) {
//                sb.append("package \"").append(component.getGroup()).append("\"").append("{\n").append("[ ")
//                        .append(component.getName()).append(" ]\n").append("}\n");
//
//            } else {
//                sb.append("[ ").append(component.getName()).append(" ]\n");
//            }
//
//        }
//
//        // Relations
//        List<Relation> relations = data.getRelations();
//        for (Relation relation : relations) {
//            sb.append("[ ").append(relation.getSourceName()).append(" ] ").append(" --> ").append("[ ")
//                    .append(relation.getDestinationName()).append(" ]").append(" : ").append(relation.getRelation())
//                    .append("\n");
//        }
//
//        // Footer
//        sb.append("\n@enduml");
//
//        // Write to file
//        try (FileWriter fileWriter = new FileWriter("src/main/resources/data_model.puml")) {
//            fileWriter.write(sb.toString());
//
//            log.info("generate");
//
//        } catch (IOException e) {
//            log.info("Error while generating PlantUML file: " + e.getMessage());
//        }
//    }
//
//}
