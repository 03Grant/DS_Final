package org.grant.server.dto;

/**
 * @nodeIdentifier 节点标识符，用IP地址代替
 * @nodeStatus     节点状态，分为active(活跃)，()
 */
public class MemberDTO {
    private String nodeIdentifier;
    private NodeStatus nodeStatus;
    private String timeStamp;

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
