package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_9;

import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Task9 {

    public static void main(String[] args) throws IOException {
        HttpServer server = createServer();
        sendRequest();

        server.stop(5);
    }

    private static void sendRequest() throws IOException {
        URL url = new URL("http://localhost:8090/test");
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("Authorization", getAuth());

        connection.setDoInput(true);
        connection.connect();
        Map<String, List<String>> responseHeaders = connection.getHeaderFields();
        System.out.println("(Client) Response header: " + responseHeaders);

        try (InputStream in = connection.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        ) {
            String resp = reader.lines()
                    .collect(Collectors.joining("\r\n"));
            System.out.println("(Client) Response body: " + resp);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static HttpServer createServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8090), 0);
        server.createContext("/test", httpExchange -> {
            String authorization = httpExchange.getRequestHeaders().getFirst("Authorization");
            System.out.println("(Server) Authorization: " + authorization);

            OutputStream response = httpExchange.getResponseBody();

            String responseStr = "";
            for (int i = 0; i < 10; i++) {
                String line = "This is the line #" + i + "\r\n";
                responseStr += line;
            }

            httpExchange.sendResponseHeaders(200, responseStr.length());
            response.write(responseStr.getBytes(StandardCharsets.UTF_8));
            response.flush();
            response.close();
        });
        server.start();
        return server;
    }

    private static String getAuth() {
        String username = "user";
        String password = "1234";
        String input = username + ":" + password;

        return Base64.getEncoder().encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }

}
