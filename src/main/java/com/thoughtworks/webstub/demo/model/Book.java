package com.thoughtworks.webstub.demo.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL;

@JsonSerialize(include = NON_NULL)
public class Book {
    public static final Book EMPTY = new Book();

    private String name;
    private String review;

    public static Book Book(String name) {
        return new Book(name);
    }

    private Book() {}

    private Book(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
