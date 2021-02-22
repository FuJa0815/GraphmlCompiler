package com.htlgkr.GraphmlCompiler.Nodes;

import com.htlgkr.GraphmlCompiler.Edges.GraphEdge;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class GraphNode {
    private String id;
    private List<GraphEdge> incomingEdges;
    private List<GraphEdge> outgoingEdges;
    private String label;

    public GraphNode(Element nodeElement) {
        id = nodeElement.getAttribute("id");
        incomingEdges = new ArrayList<>();
        outgoingEdges = new ArrayList<>();
        label = NodeHelper.getLabel(nodeElement);
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

    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraphNode graphNode = (GraphNode) o;
        return id.equals(graphNode.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getElsePathNode() {
        return getOutgoingEdges().get(0).getTargetId();
    }
}
