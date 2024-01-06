package org.grant.server.Manager;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static org.grant.server.dto.serverConfiguration.selfip;

@Service
public class ContactManager {

    // 使用线程安全的列表
    private final CopyOnWriteArrayList<String> contactList = new CopyOnWriteArrayList<>();

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

    public String getOneContactIp(String ip_source) {
        if (contactList.isEmpty()) {
            return null; // 列表为空时返回 null
        }
        int attempts = contactList.size();
        while (attempts-- > 0) {
            int index = ThreadLocalRandom.current().nextInt(contactList.size());
            String contact = contactList.get(index);

            if (!(contact.equals(ip_source) || contact.equals(selfip))) {
                return contact;
            }
        }
        return null; // 如果没有找到符合条件的IP地址
    }


    public void cleanContact(){
        contactList.clear();
    }



}
