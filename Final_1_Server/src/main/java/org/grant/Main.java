package org.grant;
import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws Exception {
        URL whatismyip = new URL("http://icanhazip.com");
        URLConnection connection = whatismyip.openConnection();
        connection.addRequestProperty("Protocol", "Http/1.1");
        connection.addRequestProperty("Connection", "keep-alive");
        connection.addRequestProperty("Keep-Alive", "1000");
        connection.addRequestProperty("User-Agent", "Web-Agent");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String ip = in.readLine();
        in.close();
        System.out.println("My public IP address is: " + ip);
    }
}