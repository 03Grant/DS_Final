package org.grant.server.heartbeat;

import org.grant.server.Manager.ContactManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.grant.server.dto.serverConfiguration.LOSS_RATE;
import static org.grant.server.dto.serverConfiguration.selfip;

@Component
public class SendHeartbeat {

    private final UdpServiceHeartbeat udpServiceHeartbeat;
    private final ContactManager contactManager;

    @Autowired
    public SendHeartbeat(UdpServiceHeartbeat udpServiceHeartbeat, ContactManager contactManager) {
        this.udpServiceHeartbeat = udpServiceHeartbeat;
        this.contactManager = contactManager;
    }

    // 假设每隔一定时间发送心跳，例如每1秒
    @Scheduled(fixedRate = 1000)
    public void sendHeartbeats() {
        List<String> contacts = contactManager.getContacts();
        for (String contactIp : contacts) {
            try {
                double random = Math.random();

                // Check if the random number is greater than LOSS_RATE
                if (random > LOSS_RATE) {
                    udpServiceHeartbeat.sendHeartbeat(selfip, contactIp);
                }else{
                    System.out.println("--------" + selfip + " HeartBeat PACKAGE LOSS" + " ------");
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 适当的异常处理
            }
        }
    }
}
