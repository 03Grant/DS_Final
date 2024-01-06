package org.grant.server.GossipRM;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.grant.server.dto.GossipRMDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

import static org.grant.server.dto.serverConfiguration.SERVER_STATUS;


@Service
public class UdpServiceRM {
    //private static final int PORT = 5179;

    @Value("${test.user.port}")
    private int PORT;

    private DatagramSocket socket;
    private final int bufferSize = 1024;

    @PostConstruct
    public void init() throws SocketException {
        socket = new DatagramSocket(PORT);
    }

    public void sendGossipRM(String member, String destinationAddress) throws IOException {
        byte[] byteData = member.getBytes(StandardCharsets.UTF_8);

        try {
            InetAddress address = InetAddress.getByName(destinationAddress);
            DatagramPacket packet = new DatagramPacket(byteData, byteData.length, address, PORT);
            socket.send(packet);
            // System.out.println("Send RMInfo Successfully!");
        } catch (IOException e) {
            System.err.println("Error in sending RMInfo: " + e.getMessage());
            throw e;
        }
//        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
//
//            objectOutputStream.writeObject(member);
//            byte[] byteData = byteArrayOutputStream.toByteArray();
//
//            InetAddress address = InetAddress.getByName(destinationAddress);
//            DatagramPacket packet = new DatagramPacket(byteData, byteData.length, address, PORT);
//            socket.send(packet);
//            // System.out.println("Send RMInfo Successfully!");
//        }
    }

    public String receiveGossipRM() throws IOException, ClassNotFoundException {
        if(!SERVER_STATUS){
            return null;
        }
        byte[] buffer = new byte[bufferSize];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);

        System.out.println("UDP_RM: Receive packet and check it.");

        // Extract the data from the packet
        String receivedJson = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
        return receivedJson;
//        byte[] buffer = new byte[bufferSize];
//        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
//        socket.receive(packet);
//
//        System.out.println("UDP_RM: Receive packet and check it: ");
//
//        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
//             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
//            return (GossipRMDTO) objectInputStream.readObject();
//        }
    }

    @PreDestroy
    public void destroy() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}
