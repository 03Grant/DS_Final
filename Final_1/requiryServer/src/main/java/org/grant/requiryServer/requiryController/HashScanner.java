package org.grant.requiryServer.requiryController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Scanner;

@RestController
public class HashScanner {

    @GetMapping("/final1/hashscan")
    public void HashScan(@RequestParam String author, @RequestParam int beginyear, @RequestParam int endyear) {
        Scanner scanner = new Scanner(System.in);

        String authorToQuery = author;

        int yearToQuery = beginyear;

        int paperCount = queryPaperCount(authorToQuery, yearToQuery);

        //todo 这里返回一个Response之类的
        System.out.println(authorToQuery + " published " + paperCount + " papers in " + yearToQuery + ".");
    }

    private static int queryPaperCount(String author, int year) {
        try {
            File xmlFile = new File("output.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            NodeList yearList = doc.getElementsByTagName("Year");
            for (int i = 0; i < yearList.getLength(); i++) {
                Element yearElement = (Element) yearList.item(i);
                int currentYear = Integer.parseInt(yearElement.getAttribute("value"));

                if (currentYear == year) {
                    NodeList authorList = yearElement.getElementsByTagName("Author");
                    for (int j = 0; j < authorList.getLength(); j++) {
                        Element authorElement = (Element) authorList.item(j);
                        String currentAuthor = authorElement.getAttribute("name");

                        if (currentAuthor.equals(author)) {
                            return Integer.parseInt(authorElement.getAttribute("frequency"));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
