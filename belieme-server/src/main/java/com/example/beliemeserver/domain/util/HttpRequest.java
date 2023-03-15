package com.example.beliemeserver.domain.util;

import com.example.beliemeserver.error.exception.BadGatewayException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    public static JSONObject getUserInfoFromHanyangApi(String apiUrl, String clientKey, String apiToken) {
        Map<String, String> headers = makeHanyangApiHeaders(apiUrl, clientKey, apiToken);
        String response = HttpRequest.sendGetRequest(apiUrl, headers);

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonResponse;

        try {
            jsonResponse = (JSONObject) jsonParser.parse(response);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new BadGatewayException();
        }

        jsonResponse = (JSONObject) jsonResponse.getOrDefault("response", new JSONObject());
        jsonResponse = (JSONObject) jsonResponse.getOrDefault("item", null);
        if (jsonResponse == null) throw new BadGatewayException();
        return jsonResponse;
    }

    private static Map<String, String> makeHanyangApiHeaders(String apiUrl, String clientKey, String apiToken) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", getHost(apiUrl));
        headers.put("client_id", clientKey);
        headers.put("swap_key", Long.toString(System.currentTimeMillis() / 1000));
        headers.put("access_token", apiToken);

        return headers;
    }

    private static String sendGetRequest(String urlString, Map<String, String> requestHeader) {
        String output;
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            requestHeader.forEach(con::setRequestProperty);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            InputStream in = con.getInputStream();
            while (true) {
                int readLen = in.read(buf);
                if (readLen < 1)
                    break;
                bos.write(buf, 0, readLen);
            }
            output = bos.toString(StandardCharsets.UTF_8);

            bos.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadGatewayException();
        }
        return output;
    }

    private static String getHost(String apiUrl) {
        String host;
        try {
            URI uri = new URI(apiUrl);
            host = uri.getHost();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new BadGatewayException();
        }
        return host;
    }
}
