package com.htlgkr.GraphmlCompiler;

import com.htlgkr.GraphmlCompiler.Edges.GraphEdge;
import com.htlgkr.GraphmlCompiler.Nodes.GraphGraphNode;
import com.htlgkr.GraphmlCompiler.Nodes.NodeHelper;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.util.List;
import java.util.stream.StreamSupport;

public class Main {
    public static void main(String[] args) {
        Element graph;
        try {
            graph = getTopGraphElement("D:\\Programmieren\\AUD\\GraphmlCompiler\\GraphmlCompiler\\ElectionUtilBasic.graphml");
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
            return;
        }

        GraphGraphNode topGraphNode = new GraphGraphNode(graph);
        for (GraphEdge edge : getEdges(graph))
        {
            NodeHelper.executeForNode(topGraphNode, edge.getSourceId(), node -> node.addOutgoingEdge(edge));
            NodeHelper.executeForNode(topGraphNode, edge.getTargetId(), node -> node.addIncomingEdge(edge));
        }

        NodeHelper.translateFromTopNode(topGraphNode);
    }

    private static Element getTopGraphElement(String filename) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse(new File(filename));
        Element graphml = doc.getDocumentElement();
        Element graph = (Element)graphml.getElementsByTagName("graph").item(0);
        return graph;
    }

    private static Iterable<GraphEdge> getEdges(Element graph) {
        return () -> StreamSupport.stream(NodeHelper.getNodeIterable(graph.getElementsByTagName("edge")).spliterator(), false).map(GraphEdge::new).iterator();
    }
}
