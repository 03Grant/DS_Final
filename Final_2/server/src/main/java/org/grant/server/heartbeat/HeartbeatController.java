package org.grant.server.heartbeat;

import org.grant.server.Manager.ContactManager;
import org.grant.server.Manager.MembershipManager;
import org.grant.server.dto.MemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.grant.server.dto.serverConfiguration.selfip;

@RestController
@CrossOrigin(origins = "*")
public class HeartbeatController {

    private final ContactManager contactManager;
    private final HeartbeatService heartbeatService;
    private final MembershipManager membershipManager;


    @Autowired
    public HeartbeatController(ContactManager contactManager,
                               HeartbeatService heartbeatService,
                               MembershipManager membershipManager) {
        this.contactManager = contactManager;
        this.heartbeatService = heartbeatService;
        this.membershipManager = membershipManager;
    }

    @GetMapping("/start-heartbeat")
    public String startHeartbeat(@RequestParam int serverNum) {
        // 初始化联系人列表
        initializeContacts(serverNum);
        // 初始化 HeartbeatService
        heartbeatService.initializeMissedHeartbeats();
        // 启动心跳接收服务 -->已经自动化启动了
        // receiveHeartBeaten.init();
        return "Heartbeat monitoring started";
    }

    private void initializeContacts(int serverNum) {
        List<MemberDTO> members = membershipManager.getMemberList();
        // 对于当前服务器而言，找出它的邻居
        int pos = findPositionOfSelf(members, selfip);
        // 先清空(对于扩展的情况)
        contactManager.cleanContact();

        if (pos != -1) {
            int[] positions = {
                    (pos + serverNum - 1) % serverNum,
                    (pos + serverNum - 2) % serverNum,
                    (pos + 1) % serverNum,
                    (pos + 2) % serverNum
            };

            for (int position : positions) {
                if (position >= 0 && position < members.size()) {
                    contactManager.addContact(members.get(position).getNodeIdentifier());
                }
            }
        }
        System.out.println("---------------初始化后" + selfip +":" + "的联系人列表为"+"---------------");
        List<String> test = contactManager.getContacts();
        int i = 0;
        for (String m:test
        ) {
            i++;
            System.out.println("contact"+ i + ": "+m);
        }
        System.out.println("-----------------end---------------------------------");
    }

    private int findPositionOfSelf(List<MemberDTO> members, String selfip) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getNodeIdentifier().equals(selfip)) {
                return i;
            }
        }
        return -1; // 如果没有找到selfip
    }
}

