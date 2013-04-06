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

public class PutRequest implements InProcRequest {
    private String bookDetails;
    private URI uri;
    private Map<String, String> headers = new HashMap<String, String>() {{
        put("Host", "localhost");
        put("Content-Type", "application/json");
    }};

    public PutRequest(String uriString, Book book) throws JsonProcessingException, URISyntaxException {
        this.bookDetails = new ObjectMapper().writeValueAsString(book);
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
        return bookDetails;
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
