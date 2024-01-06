package org.grant.requiryServer.requiryController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class DirectScanner {

    @GetMapping("/final1/directscan")
    public ResponseEntity<String> DirectScan(@RequestParam String author, @RequestParam int beginyear, @RequestParam int endyear) {

        String directoryPath = "/app/fileWare/";
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null && files.length > 0) {
                // 创建字符数组来存储文件名
                String[] fileNames = new String[files.length];

                // 提取文件名并放入字符数组中
                for (int i = 0; i < files.length; i++) {
                    fileNames[i] = files[i].getName();
                }
                List<Map<String, Object>> dataList = new ArrayList<>();
                try {
                    for (String fileName : fileNames) {
                        String authorToQuery = author;
                        List<Integer> years = new ArrayList<>();
                        for (int year = beginyear; year <= endyear; year++) {
                            years.add(year);
                        }
                        int paperCount = queryPaperCount(directoryPath, fileName, authorToQuery, years);
                        Map<String, Object> num = parseXmlPath(fileName);
                        String blockNumber = (String) num.get("blocknum");
                        String replicaNumber = (String) num.get("repnum");
                        Map<String, Object> block = new HashMap<>();
                        block.put("blocknum", blockNumber);
                        block.put("repnum", replicaNumber);
                        block.put("num", paperCount);
                        dataList.add(block);
                    }
                    // 将 dataList 转换为 JSON 字符串
                    String jsonResult = convertListToJson(dataList);
                    return new ResponseEntity<>(jsonResult, HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity<>("查询失败", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>("目录为空", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>("目录无效", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String convertListToJson(List<Map<String, Object>> dataList) throws JsonProcessingException {
        // 使用 ObjectMapper 将 List 转换为 JSON 字符串
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(dataList);
    }

    public static Map<String, Object> parseXmlPath(String xmlName) {
        // 定义匹配blocknum和副本号的正则表达式
        Pattern pattern = Pattern.compile("(\\d+)_(\\d+)\\.xml");

        // 创建 Matcher 对象
        Matcher matcher = pattern.matcher(xmlName);

        Map<String, Object> resultJson = new HashMap<>();
        // 查找匹配
        if (matcher.find()) {
            // 提取blocknum和副本号
            String blockNumber = matcher.group(1);
            String replicaNumber = matcher.group(2);

            // 将blocknum和副本号放入 resultJson
            resultJson.put("blocknum", blockNumber);
            resultJson.put("repnum", replicaNumber);
        }

        return resultJson;
    }

    private static int queryPaperCount(String directoryPath, String xmlName, String author, List<Integer> years) {
        try {
            // 构建 AWK 命令
            String awkCommand = buildAwkCommand(directoryPath, xmlName, author, years);

            // 使用 ProcessBuilder 运行 AWK 命令
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", awkCommand);
            Process process = processBuilder.start();

            // 获取命令的输出流
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;

            // 读取命令输出并拼接到 StringBuilder 中
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // 等待命令执行完成
            int exitCode = process.waitFor();

            // 打印执行结果
            System.out.println("Command executed successfully");
            System.out.println("Output: " + output);

            // 解析 AWK 命令的输出结果，提取论文数
            int paperCount = parseOutput(output.toString());

            return paperCount;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return -1; // 返回一个错误码或异常值
        }
    }

    private static String buildAwkCommand(String directoryPath, String xmlName, String author, List<Integer> years) {
        StringBuilder commandBuilder = new StringBuilder();

        // 构建 AWK 命令
        commandBuilder.append("awk -F'[<>]' '/<author>").append(author).append("<\\/author>/ { author_found=1 } ");
        commandBuilder.append("/<year>([0-9]+)<\\/year>/ { if (author_found && ($3 >= ");
        // 添加年份范围
        for (int year : years) {
            commandBuilder.append(year).append(" || $3 == ").append(year).append(" || ");
        }
        commandBuilder.delete(commandBuilder.length() - 4, commandBuilder.length());

        commandBuilder.append(")) { count++ } author_found=0 } END { print count }' ");
        commandBuilder.append(directoryPath).append("/").append(xmlName);

        return commandBuilder.toString();
    }

    private static int parseOutput(String output) {
        // 解析 AWK 命令的输出结果，提取论文数
        String[] lines = output.split("\n");
        if (lines.length > 0) {
            String lastLine = lines[lines.length - 1];
            try {
                return Integer.parseInt(lastLine.trim());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return -1; // 返回一个错误码或异常值
    }

}
