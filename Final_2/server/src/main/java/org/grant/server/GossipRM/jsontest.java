package org.grant.server.GossipRM;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.grant.server.dto.GossipRMDTO;
import org.grant.server.dto.NodeStatus;

public class jsontest {
    public static void main(String[] args){
        GossipRMDTO dto = new GossipRMDTO("127.0.0.1", NodeStatus.ACTIVE,"2023-10-01 05:54:54","127.0.0.2");
        // 设置 dto 的属性...
        try {
            String json = GossipRMDataTransfer.toJson(dto);
            System.out.println(json);

            // 反序列化示例
            GossipRMDTO dtoFromJson = GossipRMDataTransfer.fromJson(json);
            System.out.println(dtoFromJson);
            // 使用 dtoFromJson...
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}
