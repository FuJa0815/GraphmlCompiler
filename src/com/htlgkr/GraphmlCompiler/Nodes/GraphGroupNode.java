package com.htlgkr.GraphmlCompiler.Nodes;

import org.w3c.dom.Element;

import java.util.List;

public class GraphGroupNode extends GraphMultiNode {
    GraphGraphNode graphNode;
    public GraphGroupNode(Element nodeElement) {
        super(nodeElement);
        graphNode = new GraphGraphNode(NodeHelper.getFirstChildrenWithTag(nodeElement, "graph"));
    }

    @Override
    public List<GraphNode> getSubGraphNodes() {
        return graphNode.getSubGraphNodes();
    }
}
