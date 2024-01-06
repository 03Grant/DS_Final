//package org.grant.requiryServer;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.URL;
//
//public class tets {
//
//    private String ip = "";
//
//    public String getPublicIP() {
//        if (ip.isEmpty()) {
//            try {
//                URL url = new URL("http://checkip.amazonaws.com/");
//                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
//                ip = br.readLine();
//            } catch (Exception e) {
//                e.printStackTrace();
//                return "Unable to determine public IP";
//            }
//        }
//        return ip;
//    }
//
//    public static void main(String[] args){
//        tets temp = new tets();
//        System.out.println(temp.getPublicIP());
//    }
//
//}
