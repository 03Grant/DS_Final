package org.grant.server.GossipRM;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.grant.server.dto.GossipRMDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ReceiveGossipRM {
    private final UdpServiceRM udpServiceRM;
    private final GossipRMService gossipRMService;
    private ExecutorService executorService;

    @Autowired
    public ReceiveGossipRM(UdpServiceRM udpServiceRM,GossipRMService gossipRMService) {
        this.udpServiceRM = udpServiceRM;
        this.gossipRMService = gossipRMService;
    }

    @PostConstruct
    public void init() {
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(this::receiveLoop);
    }

    private void receiveLoop() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                GossipRMDTO receivedMember = udpServiceRM.receiveGossipRM();
                if(receivedMember == null)
                    return;
                gossipRMService.processMemberInfo(receivedMember);

            } catch (Exception e) {
                Thread.currentThread().interrupt(); // 在异常情况下中断线程
                e.printStackTrace();
            }
        }
    }

    @PreDestroy
    public void cleanUp() {
        executorService.shutdownNow(); // 确保在销毁Bean时关闭线程池
    }
}
