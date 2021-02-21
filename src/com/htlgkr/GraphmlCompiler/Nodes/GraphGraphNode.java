package com.htlgkr.GraphmlCompiler.Nodes;

import org.w3c.dom.Element;

import java.util.stream.StreamSupport;

public class GraphGraphNode extends GraphMultiNode {
    Element nodeElement;
    Iterable<GraphNode> subGraphs = null;

    public GraphGraphNode(Element nodeElement) {
        super(nodeElement);
        this.nodeElement = nodeElement;
    }

    @Override
    public Iterable<GraphNode> getSubGraphNodes() {
        if (subGraphs == null)
            subGraphs = () -> StreamSupport.stream(NodeHelper.getNodeIterable(NodeHelper.getDirectChildrenWithTag(nodeElement, "node")).spliterator(), false).map(e -> GraphNodeFactory.getGraphNode(e)).iterator();
        return subGraphs;
    }
}
