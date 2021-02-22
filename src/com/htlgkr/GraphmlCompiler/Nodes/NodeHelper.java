package com.htlgkr.GraphmlCompiler.Nodes;

import com.htlgkr.GraphmlCompiler.Edges.GraphEdge;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.StreamSupport;

public final class NodeHelper {
    public static Iterable<Element> getNodeIterable(NodeList nodeList)  {
        return () -> new Iterator() {
            int length = nodeList.getLength();
            int currentIndex = 0;
            @Override
            public boolean hasNext() {
                return currentIndex < length;
            }

            @Override
            public Object next() {
                return nodeList.item(currentIndex++);
            }
        };
    }

    public static NodeList getStyleProperties(Element nodeElement) {
        Element data = getFirstChildrenWithTag(nodeElement, "data");
        Element yGenericNode = getFirstChildrenWithTag(data, "y:GenericNode");
        Element styleProperties = getFirstChildrenWithTag(yGenericNode, "y:StyleProperties");
        return getDirectChildrenWithTag(styleProperties, "y:Property");
    }

    public static String getValueInPropertyList(NodeList properties, String className) {
        Element property = StreamSupport.stream(NodeHelper.getNodeIterable(properties).spliterator(), false).filter(e -> e.getAttribute("class").equals(className)).findFirst().orElse(null);
        if (property == null)
            return null;
        return property.getAttribute("value");
    }

    public static String getLabel(Element nodeElement) {
        Element data = getFirstChildrenWithTag(nodeElement, "data");
        if (data == null) return null;
        Element yGenericNode = getFirstChildrenWithTag(data, "y:GenericNode");
        if (yGenericNode == null) return null;
        Element yNodeLabel = getFirstChildrenWithTag(yGenericNode, "y:NodeLabel");
        return yNodeLabel.getTextContent();
    }

    public static GraphNode getNodeWithId(GraphNode topNode, String id) {
        if (topNode.getId().equals(id))
            return topNode;

        if (topNode instanceof GraphMultiNode) {
            GraphMultiNode topMultiNode = (GraphMultiNode) topNode;
            for (GraphNode subNode : topMultiNode.getSubGraphNodes()) {
                GraphNode tmp = getNodeWithId(subNode, id);
                if (tmp != null) return tmp;
            }
        }

        return null;
    }

    public static String translateFromTopNode(GraphNode topnode) {
        String content = "public class ElectionUtilGraphml { ";

        for (GraphNode node : ((GraphGraphNode) topnode).getSubGraphNodes()) {
            if (node instanceof GraphGroupNode) {
                content += node.getLabel() + " { ";
            }
        }

        for (GraphNode node : ((GraphGraphNode) topnode).getSubGraphNodes()) {
            if (node instanceof GraphInputNode) {
                content += translateFromTopNodeRec(topnode, node.getOutgoingEdges().get(0), new ArrayList<>(), new ArrayList<>());
            }
        }

        content += " } }";
        return content;
    }

    private static String translateFromTopNodeRec(GraphNode topnode, GraphEdge edge, List<GraphForNode> visitedFors, List<GraphIfNode> visitedIfs) {
        String content = "";

        GraphNode node = getNodeWithId(topnode, edge.getTargetId());

        for(GraphIfNode ifNode : visitedIfs) {
            if (ifNode.nextNodeWithTwoOrMore.equals(node)) {
                if (ifNode.closed) {
                    content += " } ";
                    visitedIfs.remove(ifNode);
                    return content + translateFromTopNodeRec(topnode, edge, visitedFors, visitedIfs);
                } else {
                    ifNode.closed = true;
                    Optional<GraphEdge> elsePath = ifNode.getOutgoingEdges().stream().filter(p -> p.getEdgeType() == GraphEdge.GraphEdgeType.DEF_FLOW).findFirst();
                    if (elsePath.isPresent()) {
                        content += " } else { ";
                        return content + translateFromTopNodeRec(topnode, elsePath.get(), visitedFors, visitedIfs);
                    }
                    content += " } ";
                    visitedIfs.remove(ifNode);
                    return content + translateFromTopNodeRec(topnode, edge, visitedFors, visitedIfs);
                }
            }
        }

        if (node instanceof GraphForNode) {
            if (visitedFors.contains(node)){
                content += " } " + translateFromTopNodeRec(topnode, node.getOutgoingEdges().stream().filter(p -> p.getEdgeType() == GraphEdge.GraphEdgeType.DEF_FLOW).findFirst().get(), visitedFors, visitedIfs);
            } else
            {
                visitedFors.add((GraphForNode) node);
                content += node.getLabel() + " { ";
                content += translateFromTopNodeRec(topnode, node.getOutgoingEdges().stream().filter(p -> p.getEdgeType() == GraphEdge.GraphEdgeType.SEQ_FLOW).findFirst().get(), visitedFors, visitedIfs);
            }
        }
        else if (node instanceof GraphIfNode) {
            content += node.getLabel() + " { ";

            GraphNode prevWith2OrMore;
            for(prevWith2OrMore = node;
                prevWith2OrMore.getIncomingEdges().size() < 2;
                prevWith2OrMore = getNodeWithId(topnode, prevWith2OrMore.getElsePathNode())) { }

            ((GraphIfNode)node).nextNodeWithTwoOrMore = prevWith2OrMore;
            Collections.reverse(visitedIfs);
            visitedIfs.add((GraphIfNode) node);
            Collections.reverse(visitedIfs);

            content += translateFromTopNodeRec(topnode, node.getOutgoingEdges().stream().filter(p -> p.getEdgeType() == GraphEdge.GraphEdgeType.SEQ_FLOW).findFirst().get(), visitedFors, visitedIfs);
        }
        else if (node instanceof GraphTaskNode) {
            content += node.getLabel();
            content += translateFromTopNodeRec(topnode, node.getOutgoingEdges().get(0), visitedFors, visitedIfs);
        }

        return content;
    }

    public static NodeList getDirectChildrenWithTag(Element element, String tag) {
        List<Element> tmp = new ArrayList<>();
        for (Node e : getNodeIterable(element.getChildNodes())) {
            if (e instanceof Element) {
                if (((Element)e).getTagName().equals(tag))
                    tmp.add((Element)e);
            }
        }
        return new NodeList() {
            @Override
            public Node item(int index) {
                return tmp.get(index);
            }

            @Override
            public int getLength() {
                return tmp.size();
            }
        };
    }

    public static Element getFirstChildrenWithTag(Element element, String tag) {
        for (Node e : getNodeIterable(element.getChildNodes())) {
            if (e instanceof Element) {
                if (((Element)e).getTagName().equals(tag))
                    return (Element)e;
            }
        }
        return null;
    }

    public static <T> List<T> iterableToList(Iterable<T> i) {
        List<T> result = new ArrayList<>();
        i.forEach(result::add);
        return result;
    }
}
