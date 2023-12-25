package org.grant.server.GossipRM;


import org.grant.server.ContactManager;
import org.grant.server.MembershipManager;
import org.grant.server.dto.GossipRMDTO;
import org.grant.server.dto.MemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.grant.server.dto.serverConfiguration.selfip;


@Service
public class GossipRMService {
    private final MembershipManager membershipManager;
    private final SendGossipRM sendGossipRM;
    private final ContactManager contactManager;

    @Autowired
    public GossipRMService(MembershipManager membershipManager, SendGossipRM sendGossipRM, ContactManager contactManager) {
        this.membershipManager = membershipManager;
        this.sendGossipRM = sendGossipRM;
        this.contactManager = contactManager;
    }

    // 辅助程序：用于确定需要向哪个邻居发送信息。
    public void findMember2SendInfo(GossipRMDTO newGossipDTO, String sourceip){
        int round = 4;
        for(int i = 0;i < round;i++){
            String des = contactManager.getOneContactIp(sourceip);
            if(des!=null)
              sendUpdateInfo(newGossipDTO,des);
        }
    }


    // 所有接收到的RM更新信息都在这里更新
    public void processMemberInfo(GossipRMDTO receivedGossip) {
        // 拆解接受的信息为1.数据 2.数据来源
        MemberDTO Member = receivedGossip.getMemberDTO();
        String sourceIP = receivedGossip.getSource();

        // String ip = getip(Member);
        String ip = "127.0.0.1";

        // 获取目前的组成员列表
        MemberDTO existingMember = membershipManager.getMember(Member.getNodeIdentifier());
        // 重新包装信息，将自己的ip打包
        GossipRMDTO newGossipDTO = new GossipRMDTO(Member, selfip);
        if (existingMember == null) {   //如果组成员列表为空，直接加入（但是一般情况下是不为空的，至少有自己）
            membershipManager.addMember(Member);
            // 向邻居发送数据，但排除sourceip
            findMember2SendInfo(newGossipDTO, sourceIP);
        } else {
            LocalDateTime existingTimeStamp =
                    LocalDateTime.parse(existingMember.getTimeStamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime receivedTimeStamp =
                    LocalDateTime.parse(Member.getTimeStamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            // 丢弃掉过期的消息（跟自己相比）
            if (!receivedTimeStamp.isBefore(existingTimeStamp)) {
                //消息是最新的
                //若节点状态发生改变：更新节点状态，并向其他节点发送信息
                if (!existingMember.getNodeStatus().equals(Member.getNodeStatus())) {
                    membershipManager.updateNodeStatus(Member.getNodeIdentifier(), Member.getNodeStatus());
                    // 向邻居发送数据，但排除sourceip
                    findMember2SendInfo(newGossipDTO, sourceIP);
                }
                // 若节点状态不变且时间变新了，则只更新时间
                else if (receivedTimeStamp.isAfter(existingTimeStamp)) {
                    membershipManager.updateNodeTimeStamp(Member.getNodeIdentifier(), Member.getTimeStamp());
                }
            }
        }
        // todo test列表安全性
        List<MemberDTO> test = membershipManager.getMemberList();
        System.out.println("经过处理后，列表为：");
        for (MemberDTO m:test
             ) {
                System.out.println("status: "+m.getNodeStatus());
                System.out.println("nodeIdentifier: "+m.getNodeIdentifier());
                System.out.println("timeStamp: "+m.getTimeStamp());
        }
        System.out.println("-----------------end---------------------------------");
    }

    // 模拟关机时调用
    public void cleanMembership(){
        membershipManager.removeAllMembers();
    }

    // 所有的RM更新信息都从这里发送
    public void sendUpdateInfo(GossipRMDTO member, String ip) {
        sendGossipRM.sendMemberRM(member, ip);
    }

}
