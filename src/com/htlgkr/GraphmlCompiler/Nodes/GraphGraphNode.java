package com.htlgkr.GraphmlCompiler.Nodes;

import org.w3c.dom.Element;

import java.util.stream.StreamSupport;

public class GraphGraphNode extends GraphMultiNode {
    Element nodeElement;

    public GraphGraphNode(Element nodeElement) {
        super(nodeElement);
        this.nodeElement = nodeElement;
    }

    @Override
    public Iterable<GraphNode> GetSubGraphNodes() {
        return () -> StreamSupport.stream(NodeHelper.getNodeIterable(nodeElement.getElementsByTagName("node")).spliterator(), false).map(e -> GraphNodeFactory.getGraphNode(e)).iterator();
    }
}
