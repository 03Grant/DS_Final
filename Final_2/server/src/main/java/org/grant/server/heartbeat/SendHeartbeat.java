package org.grant.server.heartbeat;

import org.grant.server.ContactManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SendHeartbeat {

    private final UdpServiceHeartbeat udpServiceHeartbeat;
    private final ContactManager contactManager;

    @Value("${heartbeat.user.ip0}")
    private String ip0;

    @Autowired
    public SendHeartbeat(UdpServiceHeartbeat udpServiceHeartbeat, ContactManager contactManager) {
        this.udpServiceHeartbeat = udpServiceHeartbeat;
        this.contactManager = contactManager;
    }

    // 假设每隔一定时间发送心跳，例如每5秒
    @Scheduled(fixedRate = 1000)
    public void sendHeartbeats() {
        List<String> contacts = contactManager.getContacts();
        for (String contactIp : contacts) {
            try {
                udpServiceHeartbeat.sendHeartbeat(ip0, contactIp);
                System.out.println("Send heartbeat: " +contactIp );
            } catch (Exception e) {
                e.printStackTrace();
                // 适当的异常处理
            }
        }
    }
}
