package org.grant.server.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * gossipRM协议类 gossip Rumor Mongering 谣言传播模型
 * 该DTO模型用于更新节点状态时使用
 * 具体用法：
 *
 * @nodeIdentifier  消息中的更新节点
 * @nodeStatus      更新的状态
 * @timeStamp       更新的时间
 * @source          发送此消息的节点
 */
public class GossipRMDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private String nodeIdentifier;
    private NodeStatus nodeStatus;
    private String timeStamp;
    private String source;

    public GossipRMDTO(String nodeIdentifier, NodeStatus nodeStatus, String timeStamp, String source) {
        this.nodeIdentifier = nodeIdentifier;
        this.nodeStatus = nodeStatus;
        this.timeStamp = timeStamp;
        this.source = source;
    }
    public GossipRMDTO(MemberDTO member, String source) {
        this.nodeIdentifier = member.getNodeIdentifier();
        this.nodeStatus = member.getNodeStatus();
        this.timeStamp = member.getTimeStamp();
        this.source = source;
    }

    public MemberDTO getMemberDTO(){
        return new MemberDTO(nodeIdentifier, nodeStatus, timeStamp);
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


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
