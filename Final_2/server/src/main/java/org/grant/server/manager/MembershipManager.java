package org.grant.server.manager;

import org.grant.server.dto.GossipDTO;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

@Service
public class MembershipManager {

    private final List<GossipDTO> membershipList = Collections.synchronizedList(new ArrayList<>());

    public synchronized void addMember(GossipDTO member) {
        // 添加成员
    }
    public synchronized void removeMember(String nodeIdentifier) {
        // 移除成员
    }
    public synchronized List<GossipDTO> getMembers() {
        // 返回成员列表的副本，以防止外部修改
        return new ArrayList<>(membershipList);
    }

    // 可以添加更多管理成员的方法
}

