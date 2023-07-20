package com.infinity.datamodel.service;

import com.infinity.datamodel.domain.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j @Service public class DrawIoExporter {

    public static final String MX_CELL = "mxCell";
    public static final String STYLE   = "style";

    private final Conf       conf;
    private final YamlParser yamlParser;

    public DrawIoExporter(Conf conf, YamlParser yamlParser) {
        this.conf = conf;
        this.yamlParser = yamlParser;

    }

    @PostConstruct public void export()
            throws RuntimeException, TransformerException, IOException, ParserConfigurationException {

        log.info("start generate diagram {} from {}", this.conf.getExportPath(), this.conf.getModelPath());

        Diagram diagram = yamlParser.parseYAML();




        Document doc = generateDocument();
        Element mxFile = generateMxFile(doc);
        Element diagramElement = generateDiagram(doc, mxFile);
        Element root = generateRootElement(doc, diagramElement);

        int groupY = 0;
        int groupHeight = 200;
        int groupWidth = 200;

        int componentX = 0;
        int componentY = 0;
        int componentWidth = 120;

        int padding = 50;

        Group previousGroup = null;

        for (Group group : diagram.getGroups()) {
            int nbComponentsInCurrentGroup = group.getComponents().size();

            Element groupElement = createElementGroup(doc, root, group);
            createGeometry(doc, 0, groupY, 200, groupWidth * nbComponentsInCurrentGroup, groupElement);

            groupY += groupHeight + padding;

            for (Component component : group.getComponents()) {

                Element componentElement = createElementComponent(doc, root, component);
                if (previousGroup == null) {
                    log.info("premier group");
                } else if (groupChange(previousGroup, group)) {
                    componentX = 0;
                    componentY = componentY + groupHeight + padding;
                } else {
                    componentX = componentX + componentWidth + padding;
                }
                createGeometry(doc, componentX, componentY, 60, 120, componentElement);
                previousGroup = group;
            }
        }

        List<Relation> relations = diagram.getRelations();
        for (Relation relation : relations) {

            log.info("relation {} ", relation.getRelationText());
            Element relationElement = doc.createElement(MX_CELL);

            if (relation.getType() != null && !relation.getType().isBlank() && relation.getType().equals("event")) {

                relationElement.setAttribute(STYLE, "fillColor=#e1d5e7;strokeColor=#9673a6;strokeWidth=2;dashed=1;");
            } else if (relation.getType() != null && !relation.getType().isBlank() && relation.getType()
                    .equals("file")) {
                relationElement.setAttribute(STYLE,
                        "edgeStyle=orthogonalEdgeStyle;rounded=0;orthogonalLoop=1;jettySize=auto;html=1;dashed=1;dashPattern=1 1;strokeWidth=3;");
            } else {
                relationElement.setAttribute(STYLE,
                        "edgeStyle=orthogonalEdgeStyle;rounded=0;orthogonalLoop=1;jettySize=auto;html=1;");
            }

            relationElement.setAttribute("edge", "1");
            relationElement.setAttribute("parent", "2");
            relationElement.setAttribute("source", relation.getSourceName());
            relationElement.setAttribute("target", relation.getDestinationName());
            relationElement.setAttribute("value", relation.getRelationText());
            root.appendChild(relationElement);

            Element mxGeometry = doc.createElement("mxGeometry");
            mxGeometry.setAttribute("relative", "1");
            mxGeometry.setAttribute("as", "geometry");
            relationElement.appendChild(mxGeometry);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(this.conf.getExportPath()));
        transformer.transform(source, result);
    }

    private boolean groupChange(Group previousGroup, Group group) {
        return previousGroup != null && !group.equals(previousGroup);
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

    private static Element createElementGroup(Document doc, Element root, Group group) {

        Element groupElement = doc.createElement(MX_CELL);
        groupElement.setAttribute("parent", "2");
        groupElement.setAttribute("id", group.getName());
        groupElement.setAttribute("value", group.getName());
        groupElement.setAttribute(STYLE, "rounded=1;whiteSpace=wrap;html=1;");
        groupElement.setAttribute("vertex", "1");
        root.appendChild(groupElement);

        return groupElement;
    }

    private static Element createElementComponent(Document doc, Element root, Component component) {

        Element groupElement = doc.createElement(MX_CELL);
        groupElement.setAttribute("parent", "2");
        groupElement.setAttribute("id", component.getName());
        if (isHyperLink(component)) {
            groupElement.setAttribute("value",
                    "<a href=" + component.getDocumentationLink() + ">" + component.getName() + "</a>");
        } else {
            groupElement.setAttribute("value", component.getName());
        }
        groupElement.setAttribute(STYLE, "rounded=1;whiteSpace=wrap;html=1;");
        groupElement.setAttribute("vertex", "1");
        //        if (isHyperLink(component)) {
        //            groupElement.setAttribute("href", component.getDocumentationLink());
        //        }
        root.appendChild(groupElement);

        return groupElement;
    }

    private static boolean isHyperLink(Component component) {
        return Objects.nonNull(component) && Objects.nonNull(component.getDocumentationLink());
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

        Element mxCell = doc.createElement(MX_CELL);
        mxCell.setAttribute("id", "1");
        root.appendChild(mxCell);

        Element mxCell2 = doc.createElement(MX_CELL);
        mxCell2.setAttribute("id", "2");
        mxCell2.setAttribute("parent", "1");
        root.appendChild(mxCell2);

        return root;
    }
}
