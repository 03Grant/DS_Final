package org.grant.server.GossipAE;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.grant.server.dto.GossipAEDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ReceiveGossipAE {
    private final UdpServiceAE udpServiceAE;
    private final GossipAEService gossipAEService;
    private ExecutorService executorService;

    @Autowired
    public ReceiveGossipAE(UdpServiceAE udpServiceAE,GossipAEService gossipAEService) {
        this.udpServiceAE = udpServiceAE;
        this.gossipAEService = gossipAEService;
    }

    @PostConstruct
    public void init() {
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(this::receiveLoop);
    }

    private void receiveLoop() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                GossipAEDTO receivedMember = udpServiceAE.receiveGossipAE();
                gossipAEService.processMemberInfo(receivedMember);

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
