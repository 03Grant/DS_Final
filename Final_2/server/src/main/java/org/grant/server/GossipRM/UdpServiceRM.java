package org.grant.server.GossipRM;


import jakarta.annotation.PostConstruct;
import org.grant.server.dto.GossipRMDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import static org.grant.server.dto.serverConfiguration.SERVER_STATUS;


@Service
public class UdpServiceRM {
    //private static final int PORT = 5179;

    @Value("${test.user.port}")
    private int PORT;

    @Value("${test.user.port1}")
    private int PORT1;

    private DatagramSocket socket;
    private final int bufferSize = 1024;

    @PostConstruct
    public void init() throws SocketException {
        socket = new DatagramSocket(PORT);
    }

    public void sendGossipRM(GossipRMDTO member, String destinationAddress) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {

            objectOutputStream.writeObject(member);
            byte[] byteData = byteArrayOutputStream.toByteArray();

            InetAddress address = InetAddress.getByName(destinationAddress);
            DatagramPacket packet = new DatagramPacket(byteData, byteData.length, address, PORT1);
            socket.send(packet);
            // System.out.println("Send RMInfo Successfully!");
        }
    }

    public GossipRMDTO receiveGossipRM() throws IOException, ClassNotFoundException {
        if(!SERVER_STATUS){
            return null;
        }
        byte[] buffer = new byte[bufferSize];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);

        System.out.println("UDP_RM: Receive packet and check it: ");

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            return (GossipRMDTO) objectInputStream.readObject();
        }
    }
}
