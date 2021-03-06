package com.htlgkr.GraphmlCompiler;

import com.htlgkr.GraphmlCompiler.Edges.GraphEdge;
import com.htlgkr.GraphmlCompiler.Nodes.GraphGraphNode;
import com.htlgkr.GraphmlCompiler.Nodes.GraphNode;
import com.htlgkr.GraphmlCompiler.Nodes.NodeHelper;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Main {
    public static void main(String[] args) {
        Element graph;
        try {
            graph = getTopGraphElement("ElectionUtilBasic.graphml");
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
            return;
        }

        GraphGraphNode topGraphNode = new GraphGraphNode(graph);
        for (GraphEdge edge : getEdges(graph))
        {
            NodeHelper.getNodeWithId(topGraphNode, edge.getSourceId()).addOutgoingEdge(edge);
            NodeHelper.getNodeWithId(topGraphNode, edge.getTargetId()).addIncomingEdge(edge);
        }

        NodeHelper.translateFromTopNode(topGraphNode);
    }

    private static Element getTopGraphElement(String filename) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse(new File(filename));
        Element graphml = doc.getDocumentElement();
        Element graph = NodeHelper.getFirstChildrenWithTag(graphml, "graph");
        return graph;
    }

    private static List<GraphEdge> getEdges(Element graph) {
        return StreamSupport.stream(NodeHelper.getNodeIterable(NodeHelper.getDirectChildrenWithTag(graph, "edge")).spliterator(), false).map(GraphEdge::new).collect(Collectors.toList());
    }
}
