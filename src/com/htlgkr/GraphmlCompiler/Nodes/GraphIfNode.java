package com.htlgkr.GraphmlCompiler.Nodes;

import org.w3c.dom.Element;

public class GraphIfNode extends GraphNode {
    private boolean ifClosed = false;

    public GraphIfNode(Element nodeElement) {
        super(nodeElement);
    }

    public boolean isIfClosed() {
        return ifClosed;
    }

    public void setIfClosed(boolean ifClosed) {
        this.ifClosed = ifClosed;
    }
}
