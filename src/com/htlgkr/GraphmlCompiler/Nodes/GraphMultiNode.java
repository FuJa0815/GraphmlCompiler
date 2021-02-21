package com.htlgkr.GraphmlCompiler.Nodes;

import org.w3c.dom.Element;

import java.util.List;

public abstract class GraphMultiNode extends GraphNode {
    public GraphMultiNode(Element nodeElement) {
        super(nodeElement);
    }

    public abstract List<GraphNode> getSubGraphNodes();
}
