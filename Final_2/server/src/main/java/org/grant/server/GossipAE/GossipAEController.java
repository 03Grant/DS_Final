package org.grant.server.GossipAE;



import org.grant.server.GossipRM.GossipRMService;
import org.grant.server.Manager.MembershipManager;
import org.grant.server.dto.*;
import org.grant.server.heartbeat.HeartbeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.grant.server.helper.TimeHelper.getCurrentTimeFormatted;
import static org.grant.server.dto.serverConfiguration.selfip;


@RestController
@CrossOrigin(origins = "*")
public class GossipAEController {


    private final GossipAEService gossipAEService;
    private final MembershipManager membershipManager;
    private final GossipRMService gossipRMService;
    private final HeartbeatService heartbeatService;

    @Autowired
    public GossipAEController
            ( GossipAEService gossipAEService,MembershipManager membershipManager,
              GossipRMService gossipRMService,HeartbeatService heartbeatService) {
        this.gossipAEService = gossipAEService;
        this.membershipManager = membershipManager;
        this.gossipRMService = gossipRMService;
        this.heartbeatService=heartbeatService;
    }


    // 参数ip为想要加入的节点ip，此函数只有introducer能够执行
    @GetMapping("/final2/join-group")
    public String introduceJoin(@RequestParam String ip) {
        // 非Introducer节点收到了join信息，忽视
        if(!serverConfiguration.introducer)
        {
            return "Join Failed -> Not Introducer!";
        }
        // 获取加入时间
        String timeStamp = getCurrentTimeFormatted();

        //ip = "127.0.0.1";

        // 1. Introducer添加改节点的信息
        // todo 不止是添加，还需要进行修改？
        MemberDTO member = new MemberDTO(ip, NodeStatus.ACTIVE,timeStamp);
        membershipManager.addMember(member);

        // 2. Introducer打包自己所拥有的列表，并发送
        GossipAEDTO waitSending = new GossipAEDTO
                (membershipManager.getMemberList(),timeStamp,selfip);
        // 3. Introducer 返回给加入节点一套完整的Member列表
        gossipAEService.sendGroupInfo(waitSending,ip);

        // Introducer<选择>联系人节点发送<RM>更新消息 !!!
        // 初始化时，introducer会向列表里第一个非自己、非新入节点的节点发信息，消息发送4次
        // 包装RM信息
        GossipRMDTO RMinfo = new GossipRMDTO(member,selfip);
        int sendTime = 4;

        for(int i = 0; i< sendTime;i++){
            String des_ip = membershipManager.getOneMemberIp_initialize(ip);
            if(des_ip!=null)
                 gossipRMService.sendUpdateInfo(RMinfo,des_ip);
        }
        // 加入之后移除不活跃联系人状态
        heartbeatService.removeInactive(ip);

        // todo 测试：初始话时的列表
        List<MemberDTO> test = membershipManager.getMemberList();
        System.out.println("----------Introducer初始化后列表为：----------");
        for (MemberDTO m:test
        ) {
            System.out.print("nodeIdentifier: "+m.getNodeIdentifier());
            System.out.print(" | status: "+m.getNodeStatus());
            System.out.println(" | timeStamp: "+m.getTimeStamp());
        }
        System.out.println("-----------------初始化展示结束end---------------------------------");
        System.out.println();

        return "Joined Successfully";
    }


    @GetMapping("/final2/set-introducer")
    public String sendReceiveGossipRM(@RequestParam boolean isIntroducer) {
        if(isIntroducer) {
            //设置为introducer
            serverConfiguration.introducer = true;
            //把introducer自己的信息写到membership里
            String timeStamp = getCurrentTimeFormatted();
            MemberDTO member = new MemberDTO(selfip, NodeStatus.ACTIVE, timeStamp);
            membershipManager.addMember(member);
        }

        System.out.println("-----------------initialize begin---------------------------------");
        // todo test: 1->首先检验是否初始化成功
        List<MemberDTO> test = membershipManager.getMemberList();
        System.out.println("initialized:");
        for (MemberDTO m:test
        ) {
            System.out.println("status: "+m.getNodeStatus());
            System.out.println("nodeIdentifier: "+m.getNodeIdentifier());
            System.out.println("timeStamp: "+m.getTimeStamp());
        }
        System.out.println("-----------------initialize end---------------------------------");

        // 浏览器保存一个IP地址，？？？需要还是不需要？？？
        return selfip;
    }
    @GetMapping("/final2/show-members")
    public List<MemberDTO> showMembers(){

        return gossipAEService.getMemberList();
    }

    @GetMapping("/final2/test-udp")
    public void testUDP(){

    }

}
