package org.grant.server.heartbeat;

import org.grant.server.Manager.ContactManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.grant.server.dto.serverConfiguration.selfip;

// 记录contact:missTime 的一个类，其中的增加missTime和resetMissTime可以被调用
// todo 希望在web程序运行之前，已经完成一次上线
// todo 上线完毕 --request--> 初始化ContactManager --->获得contact表  ----> 启动心跳服务
@Service
@Lazy
public class HeartbeatService {

    // 计时器自动增加
    private final ConcurrentHashMap<String, Integer> missedHeartbeats = new ConcurrentHashMap<>();
    private final CopyOnWriteArrayList<String> inactive = new CopyOnWriteArrayList<>();

    @Autowired
    private ContactManager contactList;

    @Autowired
    private HeartbeatProcessor heartbeatProcessor;

    public void initializeMissedHeartbeats() {
        List<String> contacts = contactList.getContacts();
        for (String ip : contacts) {
            missedHeartbeats.put(ip, 0);
        }
    }


    // 每个 IP 地址的遗漏次数增加 1。
    public void incrementMissedHeartbeats() {
        missedHeartbeats.forEach((ip, count) -> {
            int newCount = count + 1;
            missedHeartbeats.put(ip, newCount);
            if (newCount > 10) {
                // 目前的不在 不活跃联系人里的话，就进行通知
                if(!inactive.contains(ip)) {
                    inactive.add(ip); // 将服务器添加到崩溃列表
                    // todo 某server离开时添加inactive，
                    //  某server加入时移除inactive并重置。
                    // 需要一些配合...
                    heartbeatProcessor.processMissedHeartbeat(ip);
                }
            }
        });

        System.out.println("---------------" + selfip +":" + "的时钟心跳tick检验列表为"+"---------------");
        List<String> contacts = contactList.getContacts();
        for (String ip : contacts){
            System.out.println("tick: " + ip + ":"  + missedHeartbeats.getOrDefault(ip,-1));
        }
        System.out.println("-----------------end---------------------------------");
    }

    public void addInactive(String ip) {
        if (!inactive.contains(ip)) {
            inactive.add(ip);
        }
        System.out.println("--" + ip + "加入不活跃联系人列表-------------当前ip:" + selfip +"---------------");
        System.out.println("-----------------end-----------------------------------");
    }

    public void removeInactive(String ip) {
        // 重置并移除
        if(inactive.isEmpty()) {
            System.out.println("****不活跃联系人列表为空****");
            return;
        }
        resetMissedHeartbeats(ip);
        inactive.remove(ip);
        System.out.println("--" + ip + "移出不活跃联系人列表-------------当前ip:" + selfip +"---------------");
        System.out.println("-----------------end-----------------------------------");
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
