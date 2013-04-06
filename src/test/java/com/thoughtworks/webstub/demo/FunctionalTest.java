package com.thoughtworks.webstub.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.inproctester.core.InProcRequest;
import com.thoughtworks.inproctester.core.InProcResponse;
import com.thoughtworks.inproctester.jetty.HttpAppTester;
import com.thoughtworks.webstub.demo.model.Book;
import com.thoughtworks.webstub.dsl.HttpDsl;
import com.thoughtworks.webstub.stub.HttpServerStub;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.thoughtworks.webstub.demo.model.Book.Book;
import static com.thoughtworks.webstub.StubServerFactory.stubServer;
import static com.thoughtworks.webstub.dsl.HttpDsl.dslWrapped;
import static com.thoughtworks.webstub.dsl.builders.ResponseBuilder.response;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FunctionalTest {
    private static HttpAppTester app;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static HttpServerStub httpStub;
    private static HttpDsl dslServer;

    @BeforeClass
    public static void beforeAll() {
        // to override default.properties with test.properties
        System.setProperty("env", "test");

        // the external service is configured at localhost:9999, so let's start a stub there!
        httpStub = stubServer(9999, "/");
        dslServer = dslWrapped(httpStub);
        httpStub.start();

        app = new HttpAppTester("./src/main/webapp", "/");
        app.start();
    }

    @Test
    public void shouldReturnBookWithReviewInAnAwesomeWay() throws URISyntaxException, IOException {

        // create a book resource
        InProcRequest put = new PutRequest("http://localhost/book/4", Book("My Experiments With Truth"));
        assertThat(app.getResponses(put).getStatus(), is(202));

        // stub external endpoint to return canned book review
        dslServer.get("/book/4").returns(response(200).withContent("MK Gandhi's autobiography"));

        // act
        InProcRequest request = new GetRequest("http://localhost/book/4");
        InProcResponse response = app.getResponses(request);

        // assert
        assertThat(response.getStatus(), is(200));

        Book book = parseBook(response.getContent());
        assertThat(book.getName(), is("My Experiments With Truth"));
        assertThat(book.getReview(), is("MK Gandhi's autobiography"));
    }

    @AfterClass
    public static void afterAll() {
        app.stop();
        httpStub.stop();
    }

    private Book parseBook(String json) throws IOException {
        return objectMapper.readValue(json, Book.class);
    }
}
