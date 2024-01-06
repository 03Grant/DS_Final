package org.grant.server.GossipRM;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.grant.server.dto.GossipRMDTO;

public class GossipRMDataTransfer {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJson(GossipRMDTO gossipRMDTO) throws JsonProcessingException {
        return mapper.writeValueAsString(gossipRMDTO);
    }

    public static GossipRMDTO fromJson(String json) throws JsonProcessingException {
        return mapper.readValue(json, GossipRMDTO.class);
    }
}
