package com.zhwyd.server.common.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Map;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
public class HttpUtils {
    public static String getData(String url, Map<String, String> parameters) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            URIBuilder urlBuilder = new URIBuilder(url);
            if (parameters != null) {
                for (Map.Entry<String, String> entry : parameters.entrySet()) {
                    urlBuilder.addParameter(entry.getKey(), entry.getValue());
                }
            }
            HttpGet httpGet = new HttpGet(urlBuilder.build());
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new IOException("status code not 200");
                }
                HttpEntity entity = response.getEntity();
                try {
                    StringBuilder sb = new StringBuilder();
                    if (entity != null) {
                        ContentType contentType = ContentType.getOrDefault(entity);
                        Charset charset;
                        String encoding = contentType.getParameter("encoding");
                        if (encoding != null) {
                            charset = Charset.forName(encoding);
                        } else {
                            charset = Consts.UTF_8;
                        }
                        BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(), charset));
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                    }
                    return sb.toString();
                } finally {
                    EntityUtils.consume(entity);
                }
            } finally {
                response.close();
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } finally {
            httpClient.close();
        }
    }

    public static String postData(String url, Map<String, String> parameters, String data) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            URIBuilder urlBuilder = new URIBuilder(url);
            if (parameters != null) {
                for (Map.Entry<String, String> entry : parameters.entrySet()) {
                    urlBuilder.addParameter(entry.getKey(), entry.getValue());
                }
            }
            HttpPost httpPost = new HttpPost(urlBuilder.build());
            httpPost.setEntity(new StringEntity(data, Consts.UTF_8));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new IOException("status code not 200");
                }
                HttpEntity entity = response.getEntity();
                try {
                    StringBuilder sb = new StringBuilder();
                    if (entity != null) {
                        ContentType contentType = ContentType.getOrDefault(entity);
                        Charset charset;
                        String encoding = contentType.getParameter("encoding");
                        if (encoding != null) {
                            charset = Charset.forName(encoding);
                        } else {
                            charset = Consts.UTF_8;
                        }
                        BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(), charset));
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                    }
                    return sb.toString();
                } finally {
                    EntityUtils.consume(entity);
                }
            } finally {
                response.close();
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } finally {
            httpClient.close();
        }
    }
}
