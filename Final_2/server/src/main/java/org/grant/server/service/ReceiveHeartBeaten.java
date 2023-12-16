package org.grant.server.service;

import org.grant.server.dto.HeartBeatenDTO;
import org.grant.server.manager.ContactManager;
import org.grant.server.manager.MembershipManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ReceiveHeartBeaten {

    private final UdpService udpService;
    private final ContactManager contactList;

    // 这里需要吗？
    @Autowired
    private MembershipManager membershipList;
    private ExecutorService executorService;
    private ConcurrentHashMap<String, Integer> missedHeartbeats = new ConcurrentHashMap<>();
    @Autowired
    public ReceiveHeartBeaten(UdpService udpService,ContactManager contactList){
        this.udpService = udpService;
        this.contactList = contactList;
    }

    @PostConstruct
    public void init() {
        // 初始化线程池
        executorService = Executors.newSingleThreadExecutor();

        // 启动异步接收心跳的任务
        executorService.submit(this::receiveHeartbeats);
    }

    private void receiveHeartbeats() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                HeartBeatenDTO heartBeaten = udpService.receiveHeartbeat();
                // 处理接收到的心跳
                processHeartbeat(heartBeaten);
            } catch (Exception e) {
                e.printStackTrace();
                // 适当的错误处理
            }
        }
    }

    // todo 跟时间有关系，这里逻辑稍有问题
    private void processHeartbeat(HeartBeatenDTO heartBeaten) {
        // 检查心跳来源是否在 contactList 中
        if (contactList.contains(heartBeaten.getIp())) {
            missedHeartbeats.put(heartBeaten.getIp(), 0); // 重置计数器
        } else {
            missedHeartbeats.compute(heartBeaten.getIp(), (ip, count) -> (count == null) ? 1 : count + 1);
            if (missedHeartbeats.get(heartBeaten.getIp()) >= 4) {
                // 将信息传给 ReceivedHeartBeatController 处理
                receivedHeartBeatController.handleMissedHeartbeat(heartBeaten.getIp());
            }
        }
    }


    // 清理资源
    public void destroy() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow();
        }
    }
}

