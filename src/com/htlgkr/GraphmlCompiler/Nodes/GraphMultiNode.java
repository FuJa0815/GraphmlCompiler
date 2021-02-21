package com.htlgkr.GraphmlCompiler.Nodes;

import org.w3c.dom.Element;

public abstract class GraphMultiNode extends GraphNode {
    public GraphMultiNode(Element nodeElement) {
        super(nodeElement);
    }

    public abstract Iterable<GraphNode> getSubGraphNodes();
}
