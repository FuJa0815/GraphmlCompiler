package com.htlgkr.GraphmlCompiler.Nodes;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
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
