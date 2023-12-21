package org.grant.server.heartbeat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeartbeatController {

    private final ContactManager contactManager;
    private final HeartbeatService heartbeatService;
    private final ReceiveHeartBeaten receiveHeartBeaten;

    @Value("${heartbeat.user.ip1}")
    private String ip1;

    @Value("${heartbeat.user.ip2}")
    private String ip2;


    @Autowired
    public HeartbeatController(ContactManager contactManager,
                               HeartbeatService heartbeatService,
                               ReceiveHeartBeaten receiveHeartBeaten) {
        this.contactManager = contactManager;
        this.heartbeatService = heartbeatService;
        this.receiveHeartBeaten = receiveHeartBeaten;
    }

    @GetMapping("/start-heartbeat")
    public String startHeartbeat() {
        // 初始化联系人列表
        initializeContacts();

        // 初始化 HeartbeatService
        heartbeatService.initializeMissedHeartbeats();

        // 启动心跳接收服务
        receiveHeartBeaten.init();

        return "Heartbeat monitoring started";
    }

    private void initializeContacts() {
        // 示例：添加一些预定义的联系人 IP 地址
        // 后续从gossip列表里添加
        contactManager.addContact(ip1);
        System.out.println("Controller initialize ip1: " + ip1);
        contactManager.addContact(ip2);
        System.out.println("Controller initialize ip2: " + ip2);

    }
}

