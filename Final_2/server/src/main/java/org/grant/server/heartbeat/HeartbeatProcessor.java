package org.grant.server.heartbeat;

import org.grant.server.Manager.ContactManager;
import org.grant.server.GossipRM.SendGossipRM;
import org.grant.server.Manager.LogsManager;
import org.grant.server.Manager.MembershipManager;
import org.grant.server.helper.TimeHelper;
import org.grant.server.dto.GossipRMDTO;
import org.grant.server.dto.NodeStatus;
import org.grant.server.dto.serverConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.grant.server.dto.serverConfiguration.selfip;

@Service
public class HeartbeatProcessor {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    private MembershipManager membershipManager;


    @Autowired
    private SendGossipRM sendGossipRM;

    @Autowired
    private ContactManager contactManager;

    public void processMissedHeartbeat(String ip) {
        executorService.submit(() -> {
            // 更新Membership中的状态
            membershipManager.updateNodeStatus(ip, NodeStatus.CRASHED);
            System.out.println(ip + "'s status has been changed into CRASHED");

            // 创建并发送GossipRMDTO
            GossipRMDTO gossipRMDTO = new GossipRMDTO(ip, NodeStatus.CRASHED, TimeHelper.getCurrentTimeFormatted(), serverConfiguration.selfip);
            for (int i = 0; i < 4; i++) {
                String des_ip = contactManager.getOneContactIp(serverConfiguration.selfip);
                if (des_ip != null) {
                    sendGossipRM.sendMemberRM(gossipRMDTO, des_ip);
                    LogsManager.logHeartbeatMessage(ip + " crashed");

                    System.out.println("--" + ip + "心跳超时-------------" + selfip +":" + "已发送Update"+"---------------");
                    System.out.println("-----------------end-----------------------------------");

                }
            }
        });
    }

}
