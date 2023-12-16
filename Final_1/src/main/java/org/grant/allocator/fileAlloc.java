package org.grant.allocator;

import org.grant.IPAddress;
import org.grant.IPSetting;
import java.io.*;
import java.net.Socket;

public class fileAlloc {
    private final addressAlloc allocator;
    private final int copy;
    private final File folder;

    public fileAlloc(int[] storage, int copy , String folderPath){
        this.copy = copy;
        this.allocator = new addressAlloc(storage,copy);
        this.folder = new File(folderPath);
        // fileTransfer();
    }

    // 这个函数会检索文件夹下的所有.xml文件，并传输到分布式系统上
    // 对于每个文件，会传输copy+1次，并且传输到互不相同的系统
    public boolean fileTransfer(){
        if (!folder.isDirectory()) {
            System.out.println("Provided path is not a directory.");
            return false;
        }

        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    // 获取下一个文件的源文件以及目标文件的发送地址，共发送 1+copy 次，以实现负载均衡
                    int[] destinationAddress = allocator.getAddress();
                    // 通过网络发送文件
                    for (int i = 0; i < copy + 1; i++) {
                        // 检查文件名是否以 ".xml" 结尾,只处理.xml文件
                        if (fileName.endsWith(".xml")) {
                            String baseName = fileName.substring(0, fileName.length() - 4);
                            // 讲文件名字加上后缀
                            String newFileName = baseName + "_"+ i + ".xml";
                            // 发送文件
                            sendFile(file, destinationAddress[i], newFileName);
                        }
                    }
                }
            }
            return true;
        } else {
            System.out.println("No files found in the directory.");
            return false;
        }
    }

    private void sendFile(File file, int destinationAddress, String fileNameWithSuffix) {
        // 文件传输
        IPAddress ip_port = IPSetting.serverIP[destinationAddress];
        String ip = ip_port.IP;
        int port = ip_port.PORT;

        System.out.println("send to " + ip);

        try (Socket socket = new Socket(ip, port);
             FileInputStream fileInputStream = new FileInputStream(file);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
             OutputStream outputStream = socket.getOutputStream();
             PrintWriter printWriter = new PrintWriter(outputStream, true)) {

                // 发送文件名,这里用了PrintWriter
                printWriter.println(fileNameWithSuffix);

                // 发送文件数据
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
            }

            System.out.println("Successfully sent " + fileNameWithSuffix + " to " + ip + ":" + port);
        } catch (IOException e) {
            System.err.println("File transfer failed for " + fileNameWithSuffix);
            e.printStackTrace();
        }
    }
}

