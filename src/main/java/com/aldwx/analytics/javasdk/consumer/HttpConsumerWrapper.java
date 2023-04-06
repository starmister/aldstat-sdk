package com.aldwx.analytics.javasdk.consumer;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import com.sensorsdata.analytics.javasdk.util.Base64Coder;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.GZIPOutputStream;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpConsumerWrapper implements Closeable {
    CloseableHttpClient httpClient;
    final String serverUrl;
    final Map<String, String> httpHeaders;
    final boolean compressData;
    final RequestConfig requestConfig;

    public HttpConsumerWrapper(String serverUrl, int timeoutSec) {
        this(serverUrl, (Map)null, timeoutSec);
    }

    public HttpConsumerWrapper(String serverUrl, Map<String, String> httpHeaders) {
        this(serverUrl, httpHeaders, 3);
    }

    HttpConsumerWrapper(String serverUrl, Map<String, String> httpHeaders, int timeoutSec) {
        this.serverUrl = serverUrl.trim();
        this.httpHeaders = httpHeaders;
        this.compressData = true;
        int timeout = timeoutSec * 1000;
        this.requestConfig = RequestConfig.custom().setConnectionRequestTimeout(timeout).setConnectTimeout(timeout).setSocketTimeout(timeout).build();
        this.httpClient = HttpClients.custom().setUserAgent(String.format("AldStat Java SDK %s", "3.4.3")).setDefaultRequestConfig(this.requestConfig).build();
    }

    void consume(String data) throws IOException, HttpConsumerWrapper.HttpConsumerException {
        HttpUriRequest request = this.getHttpRequest(data);
        CloseableHttpResponse response = null;
        if (this.httpClient == null) {
            this.httpClient = HttpClients.custom().setUserAgent(String.format("AldStat Java SDK %s", "3.4.3")).setDefaultRequestConfig(this.requestConfig).build();
        }

        try {
            response = this.httpClient.execute(request);
            int httpStatusCode = response.getStatusLine().getStatusCode();
            if (httpStatusCode < 200 || httpStatusCode >= 300) {
                String httpContent = new String(EntityUtils.toByteArray(response.getEntity()), StandardCharsets.UTF_8);
                throw new HttpConsumerWrapper.HttpConsumerException(String.format("Unexpected response %d from AldStat: %s", httpStatusCode, httpContent), data, httpStatusCode, httpContent);
            }
        } finally {
            if (response != null) {
                response.close();
            }

        }

    }

    HttpUriRequest getHttpRequest(String data) throws IOException {
        HttpPost httpPost = new HttpPost(this.serverUrl);
        httpPost.setEntity(this.getHttpEntry(data));
        if (this.httpHeaders != null) {
            Iterator var3 = this.httpHeaders.entrySet().iterator();

            while(var3.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry)var3.next();
                httpPost.addHeader((String)entry.getKey(), (String)entry.getValue());
            }
        }

        return httpPost;
    }

    UrlEncodedFormEntity getHttpEntry(String data) throws IOException {
        byte[] bytes = data.getBytes(Charset.forName("UTF-8"));
        List<NameValuePair> nameValuePairs = new ArrayList();
        if (this.compressData) {
            ByteArrayOutputStream os = new ByteArrayOutputStream(bytes.length);
            GZIPOutputStream gos = new GZIPOutputStream(os);
            gos.write(bytes);
            gos.close();
            byte[] compressed = os.toByteArray();
            os.close();
            nameValuePairs.add(new BasicNameValuePair("gzip", "1"));
            nameValuePairs.add(new BasicNameValuePair("data_list", new String(Base64Coder.encode(compressed))));
        } else {
            nameValuePairs.add(new BasicNameValuePair("gzip", "0"));
            nameValuePairs.add(new BasicNameValuePair("data_list", new String(Base64Coder.encode(bytes))));
        }

        return new UrlEncodedFormEntity(nameValuePairs);
    }

    public synchronized void close() {
        try {
            if (this.httpClient != null) {
                this.httpClient.close();
                this.httpClient = null;
            }
        } catch (IOException var2) {
        }

    }

    static class HttpConsumerException extends Exception {
        final String sendingData;
        final int httpStatusCode;
        final String httpContent;

        HttpConsumerException(String error, String sendingData, int httpStatusCode, String httpContent) {
            super(error);
            this.sendingData = sendingData;
            this.httpStatusCode = httpStatusCode;
            this.httpContent = httpContent;
        }

        String getSendingData() {
            return this.sendingData;
        }

        int getHttpStatusCode() {
            return this.httpStatusCode;
        }

        String getHttpContent() {
            return this.httpContent;
        }
    }
}

