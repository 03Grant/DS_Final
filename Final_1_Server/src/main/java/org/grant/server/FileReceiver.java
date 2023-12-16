package org.grant.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileReceiver {
    private final int port;

    public FileReceiver(int port) {
        this.port = port;
    }
    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     InputStream inputStream = socket.getInputStream()) {

                    // 接收文件名
                    String fileName = reader.readLine();
                    File file = new File("/app/fileWare/" + fileName); // 设置文件保存路径和文件名

                    // 接收文件数据
                    try (FileOutputStream fileOutputStream = new FileOutputStream(file);
                         BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream)) {

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            bufferedOutputStream.write(buffer, 0, bytesRead);
                        }
                    }

                    System.out.println("Received file: " + fileName);
                } catch (IOException e) {
                    System.err.println("Error handling client connection");
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + port);
            e.printStackTrace();
        }
    }
}

