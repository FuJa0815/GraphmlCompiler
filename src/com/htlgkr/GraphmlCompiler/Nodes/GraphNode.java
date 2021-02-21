package com.htlgkr.GraphmlCompiler.Nodes;

import com.htlgkr.GraphmlCompiler.Edges.GraphEdge;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public abstract class GraphNode {
    private String id;
    private List<GraphEdge> incomingEdges;
    private List<GraphEdge> outgoingEdges;

    public GraphNode(Element nodeElement) {
        id = nodeElement.getAttribute("id");
        incomingEdges = new ArrayList<>();
        outgoingEdges = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void addIncomingEdge(GraphEdge edge) {
        incomingEdges.add(edge);
    }

    public void addOutgoingEdge(GraphEdge edge) {
        outgoingEdges.add(edge);
    }


    public List<GraphEdge> getIncomingEdges() {
        return incomingEdges;
    }

    public List<GraphEdge> getOutgoingEdges() {
        return outgoingEdges;
    }
}
