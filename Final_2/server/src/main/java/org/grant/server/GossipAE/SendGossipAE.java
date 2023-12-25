package org.grant.server.GossipAE;


import org.grant.server.dto.GossipAEDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SendGossipAE {
    private final UdpServiceAE udpServiceAE;

    @Autowired
    public SendGossipAE(UdpServiceAE udpServiceAE) {
        this.udpServiceAE = udpServiceAE;
    }

    public void sendMemberAE(GossipAEDTO member, String ip) {
        try {
            udpServiceAE.sendGossipAE(member, ip);
            System.out.println("Sent in second floor");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
