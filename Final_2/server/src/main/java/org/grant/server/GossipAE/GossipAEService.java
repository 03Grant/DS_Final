package org.grant.server.GossipAE;



import org.grant.server.Manager.LogsManager;
import org.grant.server.Manager.MembershipManager;
import org.grant.server.dto.GossipAEDTO;
import org.grant.server.dto.MemberDTO;
import org.grant.server.dto.NodeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public List<MemberDTO> getMemberList(){
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
        return test;
    }

    // 用于从Introdecer那里获取组成员
    public void processMemberInfo(GossipAEDTO receivedGossip) {
        // 对比信息并加入
        List<MemberDTO> members = receivedGossip.getMemberList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (MemberDTO m : members) {
            MemberDTO existingMember = membershipManager.getMember(m.getNodeIdentifier());
            System.out.println("-----------------AE preprocess begin---------------------------------");
            if (existingMember == null) {
                // 成员不存在，添加新成员
                membershipManager.addMember(m);
                System.out.println(m.getNodeIdentifier()  +  "新加入");
                LogsManager.logAEMessage(m.getNodeIdentifier() + "joined");


            } else {
                // 成员已存在，比较时间戳

                // 需要用到的信息，保存在临时变量中
                String Member_Identifier = m.getNodeIdentifier();
                NodeStatus Member_Status = m.getNodeStatus();
                String Member_Stamp = m.getTimeStamp();

                LocalDateTime existingTimeStamp = LocalDateTime.parse(existingMember.getTimeStamp(), formatter);
                LocalDateTime receivedTimeStamp = LocalDateTime.parse(Member_Stamp, formatter);

                if (receivedTimeStamp.isAfter(existingTimeStamp)) {
                    // 接收到的时间戳更新，更新现有成员信息
                    existingMember.setNodeStatus(Member_Status);
                    existingMember.setTimeStamp(m.getTimeStamp());
                    System.out.println(Member_Identifier  +  "状态更新为：" + Member_Status);
                    LogsManager.logRMMessage(Member_Identifier +
                            "update to status:"+ Member_Status + " time:"+ Member_Stamp);
                }
                System.out.println("-----------------AE preprocess end---------------------------------");
                // 如果时间戳没有更新，不做任何处理
            }
        }

        System.out.println("-----------------AE process begin---------------------------------");
        // todo test列表安全性
        List<MemberDTO> test = membershipManager.getMemberList();
        System.out.println("经过AE处理后，列表为：");
        for (MemberDTO m:test
        ) {
            // System.out.println("source: "+m.getSource());
            System.out.print("nodeIdentifier: "+m.getNodeIdentifier());
            System.out.print(" | status: "+m.getNodeStatus());
            System.out.print(" | timeStamp: "+m.getTimeStamp());
            System.out.println();
        }
        System.out.println("-----------------AE process end---------------------------------");
    }

    // Introducer发送全部信息表给组成员
    public void sendGroupInfo(GossipAEDTO member, String ip) {
        sendGossipAE.sendMemberAE(member, ip);
    }
}
