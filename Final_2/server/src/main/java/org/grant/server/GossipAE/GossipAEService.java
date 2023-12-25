package org.grant.server.GossipAE;



import org.grant.server.MembershipManager;
import org.grant.server.dto.GossipAEDTO;
import org.grant.server.dto.MemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GossipAEService {
    private final MembershipManager membershipManager;
    private final SendGossipAE sendGossipAE;

    @Autowired
    public GossipAEService(MembershipManager membershipManager, SendGossipAE sendGossipAE) {
        this.membershipManager = membershipManager;
        this.sendGossipAE = sendGossipAE;
    }

    public void getMemberList(){
        System.out.println("-----------------Presentation Members---------------------------------");
        // todo test列表安全性
        List<MemberDTO> test = membershipManager.getMemberList();
        System.out.println("经过AE处理后，列表为：");
        for (MemberDTO m:test
        ) {
            // System.out.println("source: "+m.getSource());
            System.out.println("status: "+m.getNodeStatus());
            System.out.println("nodeIdentifier: "+m.getNodeIdentifier());
            System.out.println("timeStamp: "+m.getTimeStamp());
        }
        System.out.println("-----------------Presentation end---------------------------------");
    }

    // 用于从Introdecer那里获取组成员
    public void processMemberInfo(GossipAEDTO receivedGossip) {
        // 全部加入
        List<MemberDTO> Member = receivedGossip.getMemberList();
        for (MemberDTO m: Member) {
            membershipManager.addMember(m);
        }

        System.out.println("-----------------AE process begin---------------------------------");
        // todo test列表安全性
        List<MemberDTO> test = membershipManager.getMemberList();
        System.out.println("经过AE处理后，列表为：");
        for (MemberDTO m:test
        ) {
            // System.out.println("source: "+m.getSource());
            System.out.println("status: "+m.getNodeStatus());
            System.out.println("nodeIdentifier: "+m.getNodeIdentifier());
            System.out.println("timeStamp: "+m.getTimeStamp());
        }
        System.out.println("-----------------AE process end---------------------------------");
    }

    // Introducer发送全部信息表给组成员
    public void sendGroupInfo(GossipAEDTO member, String ip) {
        sendGossipAE.sendMemberAE(member, ip);
    }
}
