package org.grant.server.GossipAE;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.grant.server.dto.GossipAEDTO;
import org.grant.server.dto.MemberDTO;
import org.grant.server.dto.NodeStatus;

import java.util.ArrayList;
import java.util.List;

public class jsontest {
    public static void main(String[] args){
        MemberDTO m1 = new MemberDTO("127.0.0.1", NodeStatus.ACTIVE, "2021-10-1 05:25:55");
        MemberDTO m2 = new MemberDTO("127.0.0.2", NodeStatus.DEPARTED, "2025-5-6 05:25:55");
        MemberDTO m3 = new MemberDTO("127.0.0.3", NodeStatus.ACTIVE, "2024-5-1 15:25:55");

        List<MemberDTO> listm = new ArrayList<>();
        listm.add(m1);
        listm.add(m2);
        listm.add(m3);

        GossipAEDTO gossipAEDTO = new GossipAEDTO(listm,"2024-01-01 12:00:00","127.1.1.1");
        try {
            String json = GossipAEDataTransfer.toJson(gossipAEDTO);
            System.out.println(json);

            GossipAEDTO receivegossipAEDTO = GossipAEDataTransfer.fromJson(json);
            System.out.println(receivegossipAEDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

}
