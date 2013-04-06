package com.thoughtworks.webstub.demo;

import com.thoughtworks.inproctester.core.InProcRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GetRequest implements InProcRequest {
    private URI uri;
    private Map<String, String> headers = new HashMap<String, String>() {{
        put("Host", "localhost");
    }};

    public GetRequest(String uriString) throws URISyntaxException {
        this.uri = new URI(uriString);
    }

    @Override
    public String getHttpMethod() {
        return "GET";
    }

    @Override
    public URI getUri() {
        return uri;
    }

    @Override
    public String getContent() {
        return null;
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
