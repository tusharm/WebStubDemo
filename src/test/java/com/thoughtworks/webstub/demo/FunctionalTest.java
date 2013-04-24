package com.thoughtworks.webstub.demo;

import com.thoughtworks.inproctester.core.InProcRequest;
import com.thoughtworks.inproctester.core.InProcResponse;
import com.thoughtworks.inproctester.jetty.HttpAppTester;
import com.thoughtworks.webstub.StubServerFacade;
import com.thoughtworks.webstub.demo.model.Book;
import com.thoughtworks.webstub.dsl.HttpDsl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.thoughtworks.webstub.StubServerFacade.newServer;
import static com.thoughtworks.webstub.demo.model.Book.Book;
import static com.thoughtworks.webstub.dsl.builders.ResponseBuilder.response;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FunctionalTest {
    private static HttpAppTester app;
    private static StubServerFacade server;
    private static HttpDsl dslServer;

    private final BookMapper mapper = new BookMapper();

    @BeforeClass
    public static void beforeAll() {
        // to override default.properties with test.properties
        System.setProperty("env", "test");

        // the app assumes the external service at localhost:9999, so let's start a stub there!
        server = newServer(9999);
        server.start();
        dslServer = server.withContext("/");

        // start the app under test in the test process itself
        app = new HttpAppTester("./src/main/webapp", "/");
        app.start();
    }

    @Test
    public void shouldReturnBookWithReviewInAnAwesomeWay() throws URISyntaxException, IOException {
        Book newBook = Book("My Experiments With Truth");
        String bookReview = "This is MK Gandhi's autobiography.\nIt is quite an interesting read!";

        // create a book resource
        InProcRequest put = new Put("http://localhost/book/4", mapper.toJson(newBook));
        assertThat(app.getResponses(put).getStatus(), is(202));

        // stub external endpoint to return canned book review
        dslServer.get("/book/4").returns(response(200).withContent(bookReview));

        // act
        InProcRequest get = new Get("http://localhost/book/4");
        InProcResponse response = app.getResponses(get);

        // assert
        assertThat(response.getStatus(), is(200));

        Book book = mapper.fromJson(response.getContent());
        assertThat(book.getName(), is("My Experiments With Truth"));
        assertThat(book.getReview(), is(bookReview));
    }

    @AfterClass
    public static void afterAll() {
        app.stop();
        server.stop();
    }
}
