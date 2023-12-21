package org.grant.server.heartbeat;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ContactManager {

    // 使用线程安全的列表
    private final List<String> contactList = new CopyOnWriteArrayList<>();

    // 检查列表中是否包含特定的 IP 地址
    public boolean contains(String ip) {
        return contactList.contains(ip);
    }

    // 向列表中添加新的 IP 地址
    public void addContact(String ip) {
        if (!contains(ip)) {
            contactList.add(ip);
        }
    }

    // 获取当前所有的联系人 IP 地址
    public List<String> getContacts() {
        return new ArrayList<>(contactList);
    }

}
