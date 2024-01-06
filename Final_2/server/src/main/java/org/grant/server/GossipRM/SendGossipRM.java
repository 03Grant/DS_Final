package org.grant.server.GossipRM;


import org.grant.server.GossipAE.GossipAEDataTransfer;
import org.grant.server.dto.GossipRMDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.grant.server.dto.serverConfiguration.LOSS_RATE;
import static org.grant.server.dto.serverConfiguration.selfip;

@Component
public class SendGossipRM {
    private final UdpServiceRM udpServiceRM;

    @Autowired
    public SendGossipRM(UdpServiceRM udpServiceRM) {
        this.udpServiceRM = udpServiceRM;
    }

    public void sendMemberRM(GossipRMDTO member, String ip) {
        try {
            double random = Math.random();
            if (random > LOSS_RATE) {
                // todo 增加一个丢包率
                // json转换
                String member_json = GossipRMDataTransfer.toJson(member);
                udpServiceRM.sendGossipRM(member_json, ip);
            }else{
                System.out.println("--------" + selfip + " RM PACKAGE LOSS" + " ------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
