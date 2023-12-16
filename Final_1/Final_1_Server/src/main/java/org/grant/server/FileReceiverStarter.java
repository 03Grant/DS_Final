package org.grant.server;

public class FileReceiverStarter {
    public static void main(String[] args) {
        int port = 4177; // 文件接收的端口号

        // 创建第服务器实例
        FileReceiver server = new FileReceiver(port);
        server.startServer();
    }
}
