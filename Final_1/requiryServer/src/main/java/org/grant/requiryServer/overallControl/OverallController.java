package org.grant.requiryServer.overallControl;

import org.grant.requiryServer.DTO.ServerStatusDTO;
import org.grant.requiryServer.Helper.IPHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OverallController {

    private static boolean serverSwitch = true;
    private String ip;

    OverallController(){
        this.ip = IPHelper.getPublicIP();
    }

    @GetMapping("/api/server/on")
    public ServerStatusDTO switchOn() {
        serverSwitch = true;
        return new ServerStatusDTO("Server " + ip + " is now ON.", serverSwitch, ip);
    }

    @GetMapping("/api/server/off")
    public ServerStatusDTO switchOff() {
        serverSwitch = false;
        return new ServerStatusDTO("Server " + ip + " is now OFF.", serverSwitch, ip);
    }

    @GetMapping("/api/server/status")
    public ServerStatusDTO isServerSwitchOn() {
        String s;
        if(serverSwitch)
            s = "ON";
        else
            s = "OFF";
        return new ServerStatusDTO("Server " + ip + " is now "+s, serverSwitch, ip);
    }


}
