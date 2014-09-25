package com.google.samples.apps.cloudlaunch.gitkit;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Helper class for sending and receiving HTTP request and response.
 */
public final class HttpUtils {

    private HttpUtils() {}

    public static byte[] get(String url) throws IOException {
        Log.i("HttpUtils", "HTTP GET " + url);
        return request("GET", url, null);
    }

    public static byte[] post(String url, byte[] body) throws IOException {
        Log.i("HttpUtils", "HTTP POST " + url);
        return request("POST", url, body);
    }

    private static byte[] request(String method, String url, byte[] body) throws IOException {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoInput(true);
            if (method.equals("POST")) {
                conn.setDoOutput(true);
                conn.setFixedLengthStreamingMode(body.length);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestMethod("POST");
                OutputStream os = conn.getOutputStream();
                os.write(body);
                os.close();
            }

            BufferedInputStream in;
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = new BufferedInputStream(conn.getInputStream());
            } else {
                in = new BufferedInputStream(conn.getErrorStream());
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            in.close();
            return out.toByteArray();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}