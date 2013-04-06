package com.thoughtworks.webstub.demo.web;

import com.thoughtworks.webstub.demo.model.Book;
import com.thoughtworks.webstub.demo.utils.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.thoughtworks.webstub.demo.model.Book.EMPTY;
import static javax.servlet.http.HttpServletResponse.SC_ACCEPTED;
import static javax.servlet.http.HttpServletResponse.SC_CONFLICT;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Controller
@RequestMapping("/book")
public class BookController {
    @Autowired
    private Client client;

    @Value("${review.service.url}") // read from configured property file
    private String bookReviewBase;

    private final Map<Integer, Book> books = new HashMap<Integer, Book>();

    @ResponseBody
    @RequestMapping(method = GET, value = "/{id:[\\d]+}", produces = "application/json")
    public Book find(@PathVariable Integer id, HttpServletResponse response) {
        if (!books.containsKey(id)) {
            response.setStatus(SC_NOT_FOUND);
            return EMPTY;
        }

        Book book = books.get(id);
        book.setReview(reviewFor(id));
        return book;
    }

    @RequestMapping(method = PUT, value = "/{id:[\\d]+}", consumes = "application/json")
    public void createOrUpdate(@PathVariable Integer id, @RequestBody Book book, HttpServletResponse response) throws IOException {
        books.put(id, book);
        response.setStatus(SC_ACCEPTED);
    }

    private String reviewFor(Integer id) {
        return client.get(bookReviewBase + "/book/" + id);
    }
}
