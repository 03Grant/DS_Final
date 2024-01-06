package org.grant.server.dto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class serverConfiguration {


    /**
     * @SERVER_STATUS 用于模拟服务器 开机或者关机。
     * 当为true时，代表开机。
     * 当为false时，代表关机。此时所有 @request、@UDP 接收发送请求都要屏蔽，心跳也停止
     * 关机的时候Membership清空
     * Contact保留（因为目前的拓扑结构在开机的时候生成）
     */
    public static boolean SERVER_STATUS = true;

    // 当前节点是否为Introducer，默认为false
    public static boolean introducer = false;

    public static double LOSS_RATE = 0;

    public static int position = 0;

    public static String selfip = "";

    public static String getPublicIP() {
        if (selfip.isEmpty()) {
            try {
                URL url = new URL("http://checkip.amazonaws.com/");
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                selfip = br.readLine();
            } catch (Exception e) {
                e.printStackTrace();
                return "Unable to determine public IP";
            }
        }
        return selfip;
    }



}
