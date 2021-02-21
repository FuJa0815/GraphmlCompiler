package com.htlgkr.GraphmlCompiler.Nodes;

import org.w3c.dom.Element;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GraphGraphNode extends GraphMultiNode {
    Element nodeElement;
    List<GraphNode> subGraphs = null;

    public GraphGraphNode(Element nodeElement) {
        super(nodeElement);
        this.nodeElement = nodeElement;
    }

    @Override
    public List<GraphNode> getSubGraphNodes() {
        if (subGraphs == null)
            subGraphs = StreamSupport.stream(NodeHelper.getNodeIterable(NodeHelper.getDirectChildrenWithTag(nodeElement, "node")).spliterator(), false).map(e -> GraphNodeFactory.getGraphNode(e)).collect(Collectors.toList());
        return subGraphs;
    }
}
