package com.htlgkr.GraphmlCompiler.Nodes;

import com.htlgkr.GraphmlCompiler.Edges.GraphEdge;
import org.w3c.dom.Element;

import java.util.Optional;

public class GraphIfNode extends GraphNode {

    public GraphNode nextNodeWithTwoOrMore;
    public boolean closed;

    public GraphIfNode(Element nodeElement) {
        super(nodeElement);
    }

    @Override
    public String getElsePathNode() {
        Optional<GraphEdge> tmp =  getOutgoingEdges().stream().filter(p -> p.getEdgeType() == GraphEdge.GraphEdgeType.DEF_FLOW).findFirst();
        if (tmp.isPresent())
            return tmp.get().getTargetId();
        return getIncomingEdges().get(0).getSourceId();
    }
}
