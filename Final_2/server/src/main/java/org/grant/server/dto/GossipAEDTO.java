package org.grant.server.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
/**
 * gossipRM协议类 gossip Anti-Entropy 减熵模型
 * 该DTO模型用于节点状态时加入时，由Introducer使用
 * 具体用法：
 *
 * @nodeIdentifier  消息中的更新节点
 * @nodeStatus      更新的状态
 * @timeStamp       更新的时间
 * @source          发送此消息的节点
 */
public class GossipAEDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private List<MemberDTO> MemberList;
    private String timeStamp;
    private String source;

    public GossipAEDTO(List<MemberDTO> memberList, String timeStamp, String source) {
        MemberList = memberList;
        this.timeStamp = timeStamp;
        this.source = source;
    }
    public List<MemberDTO> getMemberList() {
        return MemberList;
    }

    public void setMemberList(List<MemberDTO> memberList) {
        MemberList = memberList;
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
