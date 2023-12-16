package org.grant.server.service;

import org.grant.server.dto.HeartBeatenDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

@Service
public class UdpService {

    private static final int PORT = 5177/* Your UDP port */;
    private DatagramSocket socket;
    private final int bufferSize = 512;

    public UdpService() throws SocketException {
        socket = new DatagramSocket(PORT);
    }

    // 发送心跳信息，@address 是IP地址
    public void sendHeartbeat(String message, String address) throws IOException {
        InetAddress inetAddress = InetAddress.getByName(address);
        DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), inetAddress, PORT);
        socket.send(packet);
    }

    // 接受其他服务器的IP心跳信息
    public HeartBeatenDTO receiveHeartbeat() throws IOException {
        byte[] buffer = new byte[bufferSize];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);

        // 解析接收到的数据
        String receivedData = new String(packet.getData(), 0, packet.getLength());
        HeartBeatenDTO heartBeaten = new HeartBeatenDTO();
        heartBeaten.setIp(receivedData.trim()); // 接收到的数据是 IP 地址

        return heartBeaten;
    }


    // 接收数据的方法...
}

