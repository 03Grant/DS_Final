package org.grant.allocator;

public class FileSenderStarter {
    public static void main(String[] args) {
        // 服务器数量配置
        int servers = 7;
        /* storage是一个帮助文件分发的数组。
        *  数组长度 servers 代表了服务器的数量
        *  storage[i]代表第i+1个服务器目前的存储量是多少
        * */
        int[] storage = new int[servers]; // 服务器存储量数组


        String folderPath = "C:\\Works\\大三\\分布式系统\\DS_Final\\split_files"; // 文件夹路径，需要替换成实际路径

        fileAlloc sender = new fileAlloc(storage,copyNum(servers),folderPath);
        sender.fileTransfer();
    }

    public static int copyNum(int servers){
        return servers/2;
    }

}
