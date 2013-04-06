package com.thoughtworks.webstub.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.inproctester.core.InProcRequest;
import com.thoughtworks.webstub.demo.model.Book;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Put implements InProcRequest {
    private String content;
    private URI uri;

    private Map<String, String> headers = new HashMap<String, String>() {{
        put("Host", "localhost");
        put("Content-Type", "application/json");
    }};

    public Put(String uriString, Book book) throws JsonProcessingException, URISyntaxException {
        this.content = new ObjectMapper().writeValueAsString(book);
        this.uri = new URI(uriString);
    }

    public Put(String uriString, String content) throws URISyntaxException {
        this.content = content;
        this.uri = new URI(uriString);
    }

    @Override
    public String getHttpMethod() {
        return "PUT";
    }

    @Override
    public URI getUri() {
        return  uri;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public Set<String> getHeaderNames() {
        return headers.keySet();
    }

    @Override
    public String getHeader(String s) {
        return headers.get(s);
    }

    @Override
    public void addHeader(String s, String s2) {
    }
}
