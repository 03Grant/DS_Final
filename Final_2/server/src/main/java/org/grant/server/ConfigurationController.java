package org.grant.server;


import org.grant.server.Manager.MembershipManager;
import org.grant.server.dto.MemberDTO;
import org.grant.server.dto.serverConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.grant.server.dto.serverConfiguration.selfip;

@RestController
@RequestMapping("/config")
@CrossOrigin(origins = "*")
public class ConfigurationController {

    private final RestTemplate restTemplate;

    private final MembershipManager membershipManager;

    @Autowired
    public ConfigurationController(RestTemplate restTemplate,MembershipManager membershipManager){
        this.membershipManager = membershipManager;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/loss-rate")
    public String updateLossRate(@RequestBody double newLossRate) {
        serverConfiguration.LOSS_RATE = newLossRate;
        return "Loss rate updated to: " + newLossRate;
    }

    @PostMapping("/server-status")
    public String updateServerStatus(@RequestBody boolean newStatus) {
        serverConfiguration.SERVER_STATUS = newStatus;
        return "Server status updated to: " + (newStatus ? "ACTIVE" : "INACTIVE");
    }

    @PostMapping("/poweron")
    public String powerOn(@RequestBody String introducerIP) {
        try {
            serverConfiguration.SERVER_STATUS = true;
            URI uri = new URI("http://" + introducerIP + ":8001/final2/join-group?ip=" + selfip);
            restTemplate.getForObject(uri, String.class);
            return "Power on signal sent to " + introducerIP;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return "Failed to send power on signal to " + introducerIP;
        }
    }

    // 前端会频繁调用这个函数来获取组成员列表信息
    @GetMapping("/getlist")
    public List<MemberDTO> getMembers() {
        return membershipManager.getMemberList();
    }

    // 返回日志
    @GetMapping(value = "/logs/server_logs", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Resource> getServerLogs() {
        try {
            Resource resource = new FileSystemResource("/app/logs/server_logs.log");
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

