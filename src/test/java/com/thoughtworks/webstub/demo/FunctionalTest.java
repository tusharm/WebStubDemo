package com.thoughtworks.webstub.demo;

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
    private final BookMapper mapper = new BookMapper();
    private static HttpServerStub httpStub;
    private static HttpDsl dslServer;

    @BeforeClass
    public static void beforeAll() {
        // to override default.properties with test.properties
        System.setProperty("env", "test");

        // the external service is configured at localhost:9999, so let's start a stub there!
        httpStub = stubServer(9999, "/");
        httpStub.start();
        dslServer = dslWrapped(httpStub);

        // start the app under test in the test process itself
        app = new HttpAppTester("./src/main/webapp", "/");
        app.start();
    }

    @Test
    public void shouldReturnBookWithReviewInAnAwesomeWay() throws URISyntaxException, IOException {

        // create a book resource
        Book newBook = Book("My Experiments With Truth");
        InProcRequest put = new Put("http://localhost/book/4", mapper.toJson(newBook));
        assertThat(app.getResponses(put).getStatus(), is(202));

        // stub external endpoint to return canned book review
        dslServer.get("/book/4").returns(response(200).withContent("MK Gandhi's autobiography"));

        // act
        InProcRequest get = new Get("http://localhost/book/4");
        InProcResponse response = app.getResponses(get);

        // assert
        assertThat(response.getStatus(), is(200));

        Book book = mapper.fromJson(response.getContent());
        assertThat(book.getName(), is("My Experiments With Truth"));
        assertThat(book.getReview(), is("MK Gandhi's autobiography"));
    }

    @AfterClass
    public static void afterAll() {
        app.stop();
        httpStub.stop();
    }
}
