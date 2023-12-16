package org.grant;

public class IPSetting {
    public static int serverNum = 7;
    public static IPAddress[] serverIP;

    static {
        serverIP = new IPAddress[serverNum];
        // 初始化数组元素，例如使用默认构造函数
        for (int i = 0; i < serverNum; i++) {
            serverIP[i] = new IPAddress();
        }

        serverIP[0].IP = "123.206.121.97";
        serverIP[0].PORT= 4177;

        serverIP[1].IP = "47.99.166.45";
        serverIP[1].PORT= 4177;

        serverIP[2].IP = "43.142.91.204";
        serverIP[2].PORT= 4177;

        serverIP[3].IP = "47.96.73.250";
        serverIP[3].PORT= 4177;

        serverIP[4].IP = "47.99.162.243";
        serverIP[4].PORT= 4177;

        serverIP[5].IP = "8.136.125.35";
        serverIP[5].PORT= 4177;

        serverIP[6].IP = "8.136.123.0";
        serverIP[6].PORT= 4177;


    }
}

