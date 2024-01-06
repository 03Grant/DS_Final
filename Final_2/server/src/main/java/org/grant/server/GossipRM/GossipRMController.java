package org.grant.server.GossipRM;

import org.grant.server.Manager.ContactManager;
import org.grant.server.dto.GossipRMDTO;
import org.grant.server.dto.NodeStatus;
import org.grant.server.dto.serverConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.grant.server.helper.TimeHelper.getCurrentTimeFormatted;
import static org.grant.server.dto.serverConfiguration.selfip;


@RestController
@CrossOrigin(origins = "*")
public class GossipRMController {


    private final SendGossipRM S;
    private final ContactManager C;
    private final GossipRMService gossipRMService;

    @Autowired
    public GossipRMController(GossipRMService gossipRMService,SendGossipRM s, ContactManager contactManager) {
        this.gossipRMService = gossipRMService;
        C = contactManager;
        S = s;
    }


    // 外部接口，供测试的时候用，用来发送一条RM信息
    @GetMapping("/final2/start-gossipRM")
    public String startReceiveGossipRM() {
        //GossipRMDTO g = new GossipRMDTO("127.0.0.1", NodeStatus.ACTIVE,"2023-01-01 10:00:00","127.0.0.1");
        // S.sendMemberRM(g,"127.0.0.1");
        return "init!";
    }

    // 外部接口，用于测试，接受一些信息在本地发。
    @GetMapping("/final2/send-gossipRM")
    public String sendReceiveGossipRM(@RequestParam NodeStatus status, @RequestParam String nodeIdentifier , @RequestParam String timeStamp) {
        // 使用获取的参数
        GossipRMDTO g = new GossipRMDTO(nodeIdentifier, status, timeStamp, "127.0.0.1");
        S.sendMemberRM(g, "127.0.0.1");
        return "test gossip sent!";
    }

    // 外部接口，用于前端发送一个离开的信号
    @GetMapping("/final2/leave-group")
    public String leaveGroup(){

        GossipRMDTO gossipRMDTO = new GossipRMDTO(selfip, NodeStatus.DEPARTED, getCurrentTimeFormatted(), selfip);
        // 选择contact
        int sendTime = 4;
        for(int i = 0; i< sendTime;i++){
            String des_ip = C.getOneContactIp(selfip);
            System.out.println("---------------" + selfip + " leave info 已经被发送到" + des_ip+"-----------");
            if(des_ip!=null)
                gossipRMService.sendUpdateInfo(gossipRMDTO,des_ip);
        }
        // 一些模拟关机操作：
        //1. 设置STATUS，关闭UDP_RM接收。但是UDP_AE只涉及在初始化时使用，所以UDP_AE可以不关，等待模拟开机命令
        //2. 清空Membership,等待模拟开机后重新载入
        gossipRMService.cleanMembership();
        serverConfiguration.SERVER_STATUS = false;
        return selfip + " successfully leaved!";
    }

    @GetMapping("/final2/crush")
    public String crush(){
        // 一些模拟关机操作：
        //1. 设置STATUS，关闭UDP_RM接收。但是UDP_AE只涉及在初始化时使用，所以UDP_AE可以不关，等待模拟开机命令
        serverConfiguration.SERVER_STATUS = false;
        //2. 清空Membership,等待模拟开机后重新载入
        gossipRMService.cleanMembership();
        return selfip + " crushed!";
    }


}
