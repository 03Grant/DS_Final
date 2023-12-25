package org.grant.server.GossipRM;


import org.grant.server.dto.GossipRMDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SendGossipRM {
    private final UdpServiceRM udpServiceRM;

    @Autowired
    public SendGossipRM(UdpServiceRM udpServiceRM) {
        this.udpServiceRM = udpServiceRM;
    }

    public void sendMemberRM(GossipRMDTO member, String ip) {
        try {
            // todo 增加一个丢包率
            udpServiceRM.sendGossipRM(member, ip);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
