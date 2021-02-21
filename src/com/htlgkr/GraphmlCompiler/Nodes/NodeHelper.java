package com.htlgkr.GraphmlCompiler.Nodes;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.StreamHandler;
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
        Element data = (Element)nodeElement.getElementsByTagName("data").item(0);
        Element yGenericNode = (Element)data.getElementsByTagName("y:GenericNode").item(0);
        Element styleProperties = (Element)yGenericNode.getElementsByTagName("y:StyleProperties");
        return styleProperties.getElementsByTagName("y:Property");
    }

    public static String getValueInPropertyList(NodeList properties, String className) {
        Element property = StreamSupport.stream(NodeHelper.getNodeIterable(properties).spliterator(), false).filter(e -> e.getAttribute("class").equals(className)).findFirst().orElse(null);
        if (property == null)
            return null;
        return property.getAttribute("value");
    }

    public static String getLabel(Element nodeElement) {
        Element data = (Element)nodeElement.getElementsByTagName("data").item(0);
        Element yGenericNode = (Element)data.getElementsByTagName("y:GenericNode").item(0);
        Element yNodeLabel = (Element)yGenericNode.getElementsByTagName("y:NodeLabel").item(0);
        return yNodeLabel.getTextContent();
    }

    public static boolean executeForNode(GraphNode topNode, String nodeId, Consumer<GraphNode> action) {
        if (topNode.getId().equals(nodeId)) {
            action.accept(topNode);
            return true;
        }
        if (topNode instanceof GraphMultiNode) {
            for (GraphNode subNode : ((GraphMultiNode) topNode).GetSubGraphNodes()) {
                if (executeForNode(subNode, nodeId, action))
                    return true;
            }
        }
        return false;
    }
}
