package org.grant;
import org.grant.IPAddress;

public class IPSetting {
    public static int serverNum = 3;
    public static IPAddress[] serverIP;

    static {
        serverIP = new IPAddress[serverNum];
        // 初始化数组元素，例如使用默认构造函数
        for (int i = 0; i < serverNum; i++) {
            serverIP[i] = new IPAddress(); // 假设IPAddress有一个默认构造函数
        }

        serverIP[0].IP = "172.29.80.1";
        serverIP[0].PORT= 4177;

        serverIP[1].IP = "192.168.31.1";
        serverIP[1].PORT= 4177;

        serverIP[2].IP = "100.80.164.123";
        serverIP[2].PORT= 4177;
    }
}

