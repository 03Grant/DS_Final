package org.grant.server.dto;

import java.io.Serial;
import java.io.Serializable;

public class MemberDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private String nodeIdentifier;
    private NodeStatus nodeStatus;
    private String timeStamp;

    public MemberDTO(String nodeIdentifier, NodeStatus nodeStatus, String timeStamp) {
        this.nodeIdentifier = nodeIdentifier;
        this.nodeStatus = nodeStatus;
        this.timeStamp = timeStamp;
    }

    public String getNodeIdentifier() {
        return nodeIdentifier;
    }

    public void setNodeIdentifier(String nodeIdentifier) {
        this.nodeIdentifier = nodeIdentifier;
    }

    public NodeStatus getNodeStatus() {
        return nodeStatus;
    }

    public void setNodeStatus(NodeStatus nodeStatus) {
        this.nodeStatus = nodeStatus;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
