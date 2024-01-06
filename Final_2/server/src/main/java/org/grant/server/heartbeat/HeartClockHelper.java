package org.grant.server.heartbeat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static org.grant.server.dto.serverConfiguration.SERVER_STATUS;

@Component
public class HeartClockHelper {

    private final HeartbeatService heartbeatService;

    @Autowired
    public HeartClockHelper(HeartbeatService heartbeatService) {
        this.heartbeatService = heartbeatService;
    }

    @Scheduled(fixedRate = 1000) // 每150毫秒触发一次
    public void tick() {
        if(!SERVER_STATUS){
            return;
        }
        heartbeatService.incrementMissedHeartbeats();
    }
}
