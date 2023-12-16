package org.grant.server.manager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ContactManager {

    // 使用线程安全的列表
    private final List<String> contactList = new CopyOnWriteArrayList<>();

    public void addContact(String contact) {
        // 添加一个新的联系人
        contactList.add(contact);
    }

    public boolean contains(String ip) {
        return contactList.contains(ip);
    }

    public List<String> getContacts() {
        // 返回联系人列表的副本，以防止外部修改
        return new ArrayList<>(contactList);
    }

    // 如果还有其他相关操作，可以在这里添加方法
}


