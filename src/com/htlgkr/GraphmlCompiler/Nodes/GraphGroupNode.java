package com.htlgkr.GraphmlCompiler.Nodes;

import org.w3c.dom.Element;

public class GraphGroupNode extends GraphMultiNode {
    GraphGroupNode graphNode;
    public GraphGroupNode(Element nodeElement) {
        super(nodeElement);
        graphNode = new GraphGroupNode((Element)nodeElement.getElementsByTagName("graph").item(0));
    }

    @Override
    public Iterable<GraphNode> GetSubGraphNodes() {
        return graphNode.GetSubGraphNodes();
    }
}
