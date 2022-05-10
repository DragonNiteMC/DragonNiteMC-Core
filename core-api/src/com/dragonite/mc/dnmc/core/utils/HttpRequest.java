package com.dragonite.mc.dnmc.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * HttpRequest 工具
 */
public class HttpRequest {

    public static CompletableFuture<String> getFuture(String link) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return get(link);
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        });
    }

    public static String get(String link) throws IOException {
        URL url = new URL(link);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
