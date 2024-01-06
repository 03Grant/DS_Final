package org.grant.requiryServer.requiryController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class HashScanner {
    @GetMapping("/final1/hashscan")
    public ResponseEntity<String> HashScan(@RequestParam String author, @RequestParam int beginyear, @RequestParam int endyear) {
        String directoryPath = "/app/hash/";
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
                    return new ResponseEntity<>(dataList.toString(), HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity<>("查询失败", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>("目录为空", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>("目录无效", HttpStatus.INTERNAL_SERVER_ERROR);
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
            File xmlFile = new File(directoryPath+xmlName);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            int totalFrequency = 0;

            NodeList yearList = doc.getElementsByTagName("Year");
            for (int i = 0; i < yearList.getLength(); i++) {
                Element yearElement = (Element) yearList.item(i);
                int currentYear = Integer.parseInt(yearElement.getAttribute("value"));


                if (years.contains(currentYear)) {
                    NodeList authorList = yearElement.getElementsByTagName("Author");
                    for (int j = 0; j < authorList.getLength(); j++) {
                        Element authorElement = (Element) authorList.item(j);
                        String currentAuthor = authorElement.getAttribute("name");

                        if (currentAuthor.equals(author)) {
                            totalFrequency += Integer.parseInt(authorElement.getAttribute("frequency"));
                        }
                    }
                }
            }
            return totalFrequency;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
