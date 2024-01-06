package org.grant.server.GossipAE;

import org.grant.server.Manager.ContactManager;
import org.grant.server.Manager.MembershipManager;
import org.grant.server.dto.GossipAEDTO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.grant.server.dto.serverConfiguration.selfip;

@Component
public class AEClockHelper {

    private final ContactManager contactManager;
    private final MembershipManager membershipManager;
    private final GossipAEService gossipAEService;

    // 构造函数注入依赖
    public AEClockHelper(ContactManager contactManager, MembershipManager membershipManager, GossipAEService gossipAEService) {
        this.contactManager = contactManager;
        this.membershipManager = membershipManager;
        this.gossipAEService = gossipAEService;

    }

    @Scheduled(fixedRate = 3000)
    public void sendMembershipList() {
        GossipAEDTO waitSending = new GossipAEDTO(membershipManager.getMemberList(), getCurrentTimeFormatted(), selfip);

        for (int i = 0; i < 3; i++) {
            String des_ip = contactManager.getOneContactIp(selfip);
            if (des_ip != null) {
                gossipAEService.sendGroupInfo(waitSending, des_ip);
            }
        }
    }

    private String getCurrentTimeFormatted() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }
}
