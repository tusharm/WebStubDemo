package com.thoughtworks.webstub.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.webstub.demo.model.Book;

import java.io.IOException;

public class BookMapper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Book fromJson(String json) {
        try {
            return objectMapper.readValue(json, Book.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String toJson(Book book) {
        try {
            return new ObjectMapper().writeValueAsString(book);
        } catch (JsonProcessingException e) {
            throw  new RuntimeException(e);
        }
    }
}
