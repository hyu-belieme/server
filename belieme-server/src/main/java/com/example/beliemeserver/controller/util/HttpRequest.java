package com.example.beliemeserver.controller.util;

import com.example.beliemeserver.controller.httpexception.GateWayTimeOutException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpRequest {
    public static String sendGetRequest(String urlString, Map<String, String> requestHeader) throws GateWayTimeOutException {
        String output;
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            requestHeader.forEach((key, value) ->{
                con.setRequestProperty(key, value);
            });

            InputStream in = null;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            in = con.getInputStream();
            while (true) {
                int readLen = in.read(buf);
                if (readLen < 1)
                    break;
                bos.write(buf, 0, readLen);
            }
            output = new String(bos.toByteArray(), "UTF-8");

            if (bos != null) bos.close();
            if (in != null) in.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new GateWayTimeOutException("Timeout comes up while send Http Get Request to" + urlString);
        }
        return output;
    }
}
