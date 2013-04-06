package com.thoughtworks.webstub.demo.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Client {
    private DefaultHttpClient httpClient;

    public Client() {
        httpClient = new DefaultHttpClient();
    }

    public String get(String url) {
        return content(execute(new HttpGet(url)));
    }

    private HttpResponse execute(HttpRequestBase requestBase) {
        try {
            return httpClient.execute(requestBase);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error executing request: " + requestBase.getMethod(), e);
        }
        finally {
            requestBase.releaseConnection();
        }
    }

    public String content(HttpResponse response) {
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            throw new RuntimeException("Response does not contain entity");
        }

        try {
            return EntityUtils.toString(entity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                EntityUtils.consume(entity);
            } catch (IOException e) {
                throw new RuntimeException("Error closing entity stream", e);
            }
        }
    }

}
