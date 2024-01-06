package org.grant.server.Manager;


import org.grant.server.dto.MemberDTO;
import org.grant.server.dto.NodeStatus;
import org.springframework.stereotype.Service;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static org.grant.server.dto.serverConfiguration.selfip;


/**
 * void addMember(MemberDTO member)
 * void removeMember(String nodeIdentifier)
 * void removeAllMembers()
 * synchronized boolean isNodeIdentifierExists(String nodeIdentifier)
 * synchronized boolean isNodeIdentifierExists(String nodeIdentifier)
 * MemberDTO getMember(String nodeIdentifier)
 * CopyOnWriteArrayList<MemberDTO> getMemberList()
 * synchronized void updateNodeStatus(String nodeIdentifier, NodeStatus newStatus)
 * synchronized void updateNodeTimeStamp(String nodeIdentifier, String newTimeStamp)
 */

@Service
public class MembershipManager {
    // 使用CopyOnWriteArrayList来确保线程安全
    private final CopyOnWriteArrayList<MemberDTO> memberList = new CopyOnWriteArrayList<>();

    public void addMember(MemberDTO member) {
        boolean exists = false;

        for (MemberDTO m : memberList) {
            if (m.getNodeIdentifier().equals(member.getNodeIdentifier())) {
                exists = true;
                m.setNodeStatus(NodeStatus.ACTIVE); // 假设 NodeStatus 是枚举类型，且包含 ACTIVE 状态
                break;
            }
        }

        // 如果不存在相同标识符的成员，则添加新成员
        if (!exists) {
            memberList.add(member);
        }
    }

    // 一般不用清除单个，保存记录
    public void removeMember(String nodeIdentifier) {
        memberList.removeIf(member -> member.getNodeIdentifier().equals(nodeIdentifier));
    }
    //清空所有的成员
    public void removeAllMembers() {
        memberList.clear();
    }

    // 用于返回node记录是否已经存在
    public synchronized boolean isNodeIdentifierExists(String nodeIdentifier) {
        return memberList.stream()
                .anyMatch(member -> member.getNodeIdentifier().equals(nodeIdentifier));
    }

    // 获取单条信息，用于Rumor Mongering比较信息
    public MemberDTO getMember(String nodeIdentifier) {
        return memberList.stream()
                .filter(member -> member.getNodeIdentifier().equals(nodeIdentifier))
                .findFirst()
                .orElse(null);
    }

    // 获取到整个列表，适合Anti-Entropy模式使用
    public CopyOnWriteArrayList<MemberDTO> getMemberList() {
        return new CopyOnWriteArrayList<>(memberList);
    }

    // 更新节点状态->当前状态，适合Rumor Mongering使用
    public synchronized void updateNodeStatus(String nodeIdentifier, NodeStatus newStatus) {
        memberList.forEach(member -> {
            if (member.getNodeIdentifier().equals(nodeIdentifier)) {
                member.setNodeStatus(newStatus);
            }
        });
    }

    // 更新节点状态->更新的时间戳，适合Rumor Mongering使用
    public synchronized void updateNodeTimeStamp(String nodeIdentifier, String newTimeStamp) {
        memberList.forEach(member -> {
            if (member.getNodeIdentifier().equals(nodeIdentifier)) {
                member.setTimeStamp(newTimeStamp);
            }
        });
    }

    public String getOneMemberIp_initialize(String ip_source) {
        if (memberList.isEmpty()) {
            return null; // 列表为空时返回 null
        }
        int attempts = memberList.size();
        while (attempts-- > 0) {
            int index = ThreadLocalRandom.current().nextInt(memberList.size());
            String contact = memberList.get(index).getNodeIdentifier();

            if (!(contact.equals(ip_source) || contact.equals(selfip))) {
                return contact;
            }
        }
        return null; // 如果没有找到符合条件的IP地址
    }


}
