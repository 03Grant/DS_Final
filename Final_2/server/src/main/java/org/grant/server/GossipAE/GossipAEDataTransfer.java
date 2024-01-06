package org.grant.server.GossipAE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.grant.server.dto.GossipAEDTO;

public class GossipAEDataTransfer {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(GossipAEDTO gossipAEDTO) throws JsonProcessingException {
        return objectMapper.writeValueAsString(gossipAEDTO);
    }

    public static GossipAEDTO fromJson(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, GossipAEDTO.class);
    }
}
