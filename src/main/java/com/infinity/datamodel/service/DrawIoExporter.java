package com.infinity.datamodel.service;

import com.infinity.datamodel.domain.Component;
import com.infinity.datamodel.domain.Diagram;
import com.infinity.datamodel.domain.Group;
import com.infinity.datamodel.domain.Relation;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j @Service public class DrawIoExporter {

    private final YamlParser parser;

    public DrawIoExporter(YamlParser parser) {
        this.parser = parser;
    }

    @PostConstruct private void export()
            throws RuntimeException, TransformerException, IOException, ParserConfigurationException {

        Diagram diagram = this.parser.parseYAML();

        Document doc = generateDocument();
        Element mxFile = generateMxFile(doc);
        Element diagramElement = generateDiagram(doc, mxFile);
        Element root = generateRootElement(doc, diagramElement);

        int groupY = 0;
        int groupHeight = 200;

        int componentX = 0;
        int componentY = 0;
        int componentWidth = 120;

        int padding = 10;

        int groupNb = diagram.getGroups().size();

        int currentGroup = 0;
        int currentComponent = 0;

        for (Group group : diagram.getGroups()) {
            log.info("group {} x {} y {}", group.getName(), 0, groupY);
            Element groupElement = createElement(doc, root, group.getName());

            createGeometry(doc, 0, groupY, 200, 400, groupElement);

            groupY += groupHeight + padding;
            currentGroup++;

            for (Component component : group.getComponents()) {

                log.info("group number {} component {} x {} y {}", groupNb, component.getName(), 0, groupY);

                Element componentElement = createElement(doc, root, component.getName());

                createGeometry(doc, componentX, componentY, 60, 120, componentElement);

                if (currentComponent != 0) {
                    componentX = componentX + componentWidth + padding;
                }

                if (currentGroup != diagram.getGroups().size()) {
                    componentY = componentY + groupHeight + padding;
                }

                currentComponent++;
            }

        }

        List<Relation> relations = diagram.getRelations();
        for (Relation relation : relations) {

            log.info("relation {} ", relation.getRelation());
            Element relationElement = doc.createElement("mxCell");
            relationElement.setAttribute("style",
                    "edgeStyle=orthogonalEdgeStyle;rounded=0;orthogonalLoop=1;jettySize=auto;html=1;");
            relationElement.setAttribute("edge", "1");
            relationElement.setAttribute("parent", "2");
            relationElement.setAttribute("source", relation.getSourceName());
            relationElement.setAttribute("target", relation.getDestinationName());
            root.appendChild(relationElement);

            Element mxGeometry = doc.createElement("mxGeometry");
            mxGeometry.setAttribute("relative", "1");
            mxGeometry.setAttribute("as", "geometry");
            relationElement.appendChild(mxGeometry);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File("src/main/resources/data_model.xml"));
        transformer.transform(source, result);
    }

    private static void createGeometry(Document doc, int x, int y, int height, int width, Element element) {
        Element mxGeometry = doc.createElement("mxGeometry");
        mxGeometry.setAttribute("as", "geometry");
        mxGeometry.setAttribute("height", String.valueOf(height));
        mxGeometry.setAttribute("width", String.valueOf(width));
        mxGeometry.setAttribute("x", String.valueOf(x));
        mxGeometry.setAttribute("y", String.valueOf(y));

        element.appendChild(mxGeometry);
    }

    private static Element createElement(Document doc, Element root, String name) {
        Element groupElement = doc.createElement("mxCell");
        groupElement.setAttribute("value", name);
        groupElement.setAttribute("parent", "2");
        groupElement.setAttribute("style", "rounded=1;whiteSpace=wrap;html=1;");
        groupElement.setAttribute("vertex", "1");
        groupElement.setAttribute("id", name);

        root.appendChild(groupElement);
        return groupElement;
    }

    private static Document generateDocument() throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        return docBuilder.newDocument();
    }

    private static Element generateMxFile(Document doc) {
        Element mxFile = doc.createElement("mxfile");
        mxFile.setAttribute("host", "Page-1");
        doc.appendChild(mxFile);
        return mxFile;
    }

    private static Element generateDiagram(Document doc, Element mxFile) {
        Element diagram = doc.createElement("diagram");
        diagram.setAttribute("name", "Page-1");
        diagram.setAttribute("id", "1");
        mxFile.appendChild(diagram);
        return diagram;
    }

    private static Element generateRootElement(Document doc, Element diagram) {
        Element mxGraphModel = doc.createElement("mxGraphModel");
        mxGraphModel.setAttribute("pageWidth", "827");
        mxGraphModel.setAttribute("pageHeight", "1169");
        mxGraphModel.setAttribute("dx", "1720");
        mxGraphModel.setAttribute("dy", "803");
        mxGraphModel.setAttribute("grid", "1");
        mxGraphModel.setAttribute("gridSize", "10");
        mxGraphModel.setAttribute("guides", "1");
        mxGraphModel.setAttribute("tooltips", "1");
        mxGraphModel.setAttribute("connect", "1");
        mxGraphModel.setAttribute("arrows", "1");
        mxGraphModel.setAttribute("fold", "1");
        mxGraphModel.setAttribute("page", "1");
        mxGraphModel.setAttribute("pageScale", "1");

        diagram.appendChild(mxGraphModel);

        Element root = doc.createElement("root");
        mxGraphModel.appendChild(root);

        Element mxCell = doc.createElement("mxCell");
        mxCell.setAttribute("id", "1");
        root.appendChild(mxCell);

        Element mxCell2 = doc.createElement("mxCell");
        mxCell2.setAttribute("id", "2");
        mxCell2.setAttribute("parent", "1");
        root.appendChild(mxCell2);

        return root;
    }
}
