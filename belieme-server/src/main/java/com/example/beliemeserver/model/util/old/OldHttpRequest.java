package com.example.beliemeserver.model.util.old;

import com.example.beliemeserver.exception.BadGateWayException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class OldHttpRequest {
    public static String sendGetRequest(String urlString, Map<String, String> requestHeader) throws BadGateWayException {
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
            throw new BadGateWayException("Timeout comes up while send Http Get Request to" + urlString);
        }
        return output;
    }
}
