package org.grant.server.heartbeat;

import org.grant.server.ContactManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

// 记录contact:missTime 的一个类，其中的增加missTime和resetMissTime可以被调用
// todo 希望在web程序运行之前，已经完成一次上线
// todo 上线完毕 --request--> 初始化ContactManager --->获得contact表  ----> 启动心跳服务
@Service
public class HeartbeatService {

    // 计时器自动增加
    private final ConcurrentHashMap<String, Integer> missedHeartbeats = new ConcurrentHashMap<>();

    @Autowired
    private ContactManager contactList;

    public void initializeMissedHeartbeats() {
        List<String> contacts = contactList.getContacts();
        for (String ip : contacts) {
            missedHeartbeats.put(ip, 0);
        }
    }

    @Value("${heartbeat.user.ip1}")
    private String ip1;

    @Value("${heartbeat.user.ip2}")
    private String ip2;

    // 每个 IP 地址的遗漏次数增加 1。
    public void incrementMissedHeartbeats() {
        missedHeartbeats.forEach((ip, count) -> missedHeartbeats.put(ip, count + 1));
        // todo 扫描是否有大于10的服务器，将其传输给一个处理线程
        System.out.println("tick: " + missedHeartbeats.getOrDefault(ip1,-1));
        System.out.println("tick: " + missedHeartbeats.getOrDefault(ip2,-1));

    }

    // 重置遗漏次数
    public void resetMissedHeartbeats(String ip) {
        missedHeartbeats.put(ip, 0);
    }

    // 获取特定 IP 地址的心跳遗漏次数
    public int getMissedHeartbeats(String ip) {
        return missedHeartbeats.getOrDefault(ip, 0);
    }


}
