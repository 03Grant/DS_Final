package org.grant.requiryServer.requiryController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class DirectScanner {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            // 获取用户输入的XML文件路径
            System.out.print("请输入XML文件路径：");
            String xmlFilePath = scanner.nextLine();

            long startTime = System.currentTimeMillis();

            // 使用 ProcessBuilder 运行 Shell 命令
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c",
                    "grep -E '<author>|<www mdate=' " + xmlFilePath + " | sed 's/.*<author>\\(.*\\)<\\/author>.*/\\1/' > authors.txt && " +
                            "grep '<www mdate=' " + xmlFilePath + " | awk -F'-' '{print $1}' > years.txt && " +
                            "paste authors.txt years.txt | sort | uniq -c");

            Process process = processBuilder.start();

            // 等待命令执行完成
            int exitCode = process.waitFor();

            // 打印执行结果
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;

            // 打印执行时间
            System.out.println("Command executed in " + elapsedTime + " milliseconds");

            // 检查命令是否成功执行
            if (exitCode == 0) {
                System.out.println("Command executed successfully");
            } else {
                System.out.println("Command failed with exit code: " + exitCode);
            }

            // 关闭Scanner
            scanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
