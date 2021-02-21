package com.htlgkr.GraphmlCompiler.Nodes;

import org.w3c.dom.*;

import java.util.Objects;
import java.util.stream.Stream;

public class GraphNodeFactory {
    public static GraphNode getGraphNode(Element nodeElement) {
        GraphNode tmp;
        tmp = getGraphGroupNode(nodeElement);
        if (tmp != null) return tmp;

        tmp = getGraphNodeFromElement(nodeElement);
        if (tmp != null) return tmp;

        throw new UnsupportedOperationException();
    }

    private static GraphNode getGraphGroupNode(Element nodeElement) {
        if (nodeElement.getAttribute("yfiles.foldertype").equals("group"))
            return new GraphGroupNode(nodeElement);
        return null;
    }

    private static GraphNode getGraphNodeFromElement(Element nodeElement) {
        NodeList styleProperties = NodeHelper.getStyleProperties(nodeElement);
        switch (NodeHelper.getValueInPropertyList(styleProperties, "com.yworks.yfiles.bpmn.view.BPMNTypeEnum")) {
            case "ARTIFACT_TYPE_DATA_OBJECT": // Input or Output
                switch (NodeHelper.getValueInPropertyList(styleProperties, "com.yworks.yfiles.bpmn.view.DataObjectTypeEnum"))
                {
                    case "DATA_OBJECT_TYPE_INPUT":
                        return new GraphInputNode(nodeElement);
                    case "DATA_OBJECT_TYPE_OUTPUT":
                        return new GraphOutputNode(nodeElement);
                    default:
                        return null;
                }
            case "ACTIVITY_TYPE": // Execution
                return new GraphTaskNode(nodeElement);
            case "GATEWAY_TYPE_PLAIN": // Flow Control
                String label = NodeHelper.getLabel(nodeElement);
                if (label.startsWith("if"))
                    return new GraphIfNode(nodeElement);
                if (label.startsWith("for"))
                    return new GraphForNode(nodeElement);
                return null;
            default:
                return null;
        }
    }
}
