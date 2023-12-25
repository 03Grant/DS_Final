package org.grant.server.GossipAE;


import jakarta.annotation.PostConstruct;
import org.grant.server.dto.GossipAEDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

@Service
public class UdpServiceAE {

    //private static final int PORT = 5179;

    @Value("${test.user.AEport}")
    private int PORT;

    @Value("${test.user.AEport1}")
    private int PORT1;

    private DatagramSocket socket;
    private final int bufferSize = 1024;

    @PostConstruct
    public void init() throws SocketException {
        socket = new DatagramSocket(PORT);
    }

    public void sendGossipAE(GossipAEDTO member, String destinationAddress) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {

            objectOutputStream.writeObject(member);
            byte[] byteData = byteArrayOutputStream.toByteArray();

            InetAddress address = InetAddress.getByName(destinationAddress);
            DatagramPacket packet = new DatagramPacket(byteData, byteData.length, address, PORT1);
            socket.send(packet);
            System.out.println("Send AEInfo to" + address + " Successfully!");
        }
    }

    public GossipAEDTO receiveGossipAE() throws IOException, ClassNotFoundException {
        byte[] buffer = new byte[bufferSize];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);

        System.out.println("UDP_AE: Receive packet and check it: ");

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            return (GossipAEDTO) objectInputStream.readObject();
        }
    }
}
