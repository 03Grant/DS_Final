package org.grant.server.heartbeat;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.grant.server.dto.HeartBeatenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.grant.server.dto.serverConfiguration.SERVER_STATUS;

@Service
public class ReceiveHeartBeaten {

    private final UdpServiceHeartbeat udpServiceHeartbeat;
    private final HeartbeatService heartbeatService;

    private ExecutorService executorService;

    @Autowired
    public ReceiveHeartBeaten(UdpServiceHeartbeat udpServiceHeartbeat, HeartbeatService heartbeatService){
        this.udpServiceHeartbeat = udpServiceHeartbeat;
        this.heartbeatService = heartbeatService;
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
                if(!SERVER_STATUS)
                    continue;
                HeartBeatenDTO heartBeaten = udpServiceHeartbeat.receiveHeartbeat();
                if(heartBeaten == null){
                    continue;
                }

                // 处理接收到的心跳
                processHeartbeat(heartBeaten);
            } catch (Exception e) {
                e.printStackTrace();
                // 错误处理
            }
        }
    }

    // 处理心跳：让miss的次数归零
    private void processHeartbeat(HeartBeatenDTO heartBeaten) {
        heartbeatService.resetMissedHeartbeats(heartBeaten.getIp());
    }


    // 清理资源
    @PreDestroy
    public void destroy() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow();
        }
    }
}
