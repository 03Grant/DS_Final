package org.grant.requiryServer.requiryController;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Scanner;

public class HashScanner {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter author name: ");
        String authorToQuery = scanner.nextLine();

        System.out.print("Enter year: ");
        int yearToQuery = scanner.nextInt();

        int paperCount = queryPaperCount(authorToQuery, yearToQuery);

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
