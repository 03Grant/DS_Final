package org.grant.server.GossipRM;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.grant.server.dto.GossipRMDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.grant.server.dto.serverConfiguration.SERVER_STATUS;

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
                // todo 解决关机后还能接收的问题
                if(!SERVER_STATUS)
                    continue;
                // 转换
                String receivedMember_json = udpServiceRM.receiveGossipRM();
                System.out.println("接收到的RM json文件：");
                System.out.println(receivedMember_json);
                System.out.println("-------------------");

                GossipRMDTO receivedMember = GossipRMDataTransfer.fromJson(receivedMember_json);
                if(receivedMember == null)
                    continue;
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
