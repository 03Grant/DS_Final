package org.grant.server.heartbeat;


import jakarta.annotation.PostConstruct;
import org.grant.server.dto.HeartBeatenDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.*;

import static org.grant.server.dto.serverConfiguration.SERVER_STATUS;

@Service
public class UdpServiceHeartbeat {


    private static final int PORT = 5178 /* Your UDP port */;
    private DatagramSocket socket;
    private final int bufferSize = 512;

    @Value("${heartbeat.user.ip0}")
    private String ip0;

    @PostConstruct
    public void init() throws SocketException, UnknownHostException {
        socket = new DatagramSocket(PORT);

    }

    // 发送心跳信息，@address 是IP地址
    public void sendHeartbeat(String message, String address) throws IOException {
        if(!SERVER_STATUS){
            return;
        }

        InetAddress inetAddress = InetAddress.getByName(address);
        DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), inetAddress, PORT);
        socket.send(packet);
    }

    // 接受其他服务器的IP心跳信息
    public HeartBeatenDTO receiveHeartbeat() throws IOException {

        if(!SERVER_STATUS){
            return null;
        }

        byte[] buffer = new byte[bufferSize];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);

        // 解析接收到的数据
        String receivedData = new String(packet.getData(), 0, packet.getLength());
        System.out.println("UdpService Receive ip" + receivedData);
        HeartBeatenDTO heartBeaten = new HeartBeatenDTO();
        heartBeaten.setIp(receivedData.trim()); // 接收到的数据是 IP 地址

        return heartBeaten;
    }

}
