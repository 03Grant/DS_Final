package org.grant.requiryServer.Helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

// 一台服务器一个IP，使用静态更方便
public class IPHelper {
    private static String ip = "";

    public static String getPublicIP() {
        if (ip.isEmpty()) {
            try {
                URL url = new URL("http://checkip.amazonaws.com/");
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                ip = br.readLine();
            } catch (Exception e) {
                e.printStackTrace();
                return "Unable to determine public IP";
            }
        }
        return ip;
    }
}
