package org.grant.server.GossipAE;


import org.grant.server.dto.GossipAEDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.grant.server.dto.serverConfiguration.LOSS_RATE;
import static org.grant.server.dto.serverConfiguration.selfip;

@Component
public class SendGossipAE {
    private final UdpServiceAE udpServiceAE;

    @Autowired
    public SendGossipAE(UdpServiceAE udpServiceAE) {
        this.udpServiceAE = udpServiceAE;
    }

    public void sendMemberAE(GossipAEDTO member, String ip) {
        try {
            // Generate a random number between 0 and 1
            double random = Math.random();

            // Check if the random number is greater than LOSS_RATE
            if (random > LOSS_RATE) {
                // 转换json
                String member_json = GossipAEDataTransfer.toJson(member);

                udpServiceAE.sendGossipAE(member_json, ip);
                System.out.println("Sent in second floor");
            } else {
                System.out.println("--------" + selfip + " RM PACKAGE LOSS" + " ------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
