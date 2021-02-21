package com.htlgkr.GraphmlCompiler.Edges;

import org.w3c.dom.Element;

public class GraphEdge {
    private String id;
    private String sourceId;
    private String targetId;
    private GraphEdgeType edgeType;

    public GraphEdge(Element edgeElement) {
        id       = edgeElement.getAttribute("id");
        sourceId = edgeElement.getAttribute("source");
        targetId = edgeElement.getAttribute("target");
        Element data = (Element)edgeElement.getElementsByTagName("data").item(0);
        Element yGenericEdge = (Element)data.getElementsByTagName("y:GenericEdge").item(0);
        Element yArrows = (Element)yGenericEdge.getElementsByTagName("y:Arrows").item(0);
        switch (yArrows.getAttribute("source"))
        {
            case "skewed_dash":
                edgeType = GraphEdgeType.DEF_FLOW;
                break;
            case "none":
                edgeType = GraphEdgeType.SEQ_FLOW;
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public String getId() {
        return id;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getTargetId() {
        return targetId;
    }

    public GraphEdgeType getEdgeType() {
        return edgeType;
    }

    public enum GraphEdgeType {
        SEQ_FLOW,
        DEF_FLOW
    }
}
