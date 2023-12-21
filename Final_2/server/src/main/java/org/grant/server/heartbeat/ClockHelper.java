package org.grant.server.heartbeat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ClockHelper {

    private final HeartbeatService heartbeatService;

    @Autowired
    public ClockHelper(HeartbeatService heartbeatService) {
        this.heartbeatService = heartbeatService;
    }

    @Scheduled(fixedRate = 1000) // 每150毫秒触发一次
    public void tick() {
        heartbeatService.incrementMissedHeartbeats();
    }
}
