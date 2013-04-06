WebStubDemo
===========

Demonstrating usage of [WebStub](https://github.com/tusharm/WebStub) in a test for a Spring-based application with external dependency.

## Application

This is a simple Spring-based webservice that exposes endpoints to create and get Books. When a book is queried,
the webservice calls an external service to fetch reviews for the book and returns it as part of the response.
The external service's base URL is configured [default.properties](/src/main/resources/default.properties).
Look at the only controller [BookController](/src/main/java/com/thoughtworks/webstub/demo/web/BookController.java) for the request handling logic.

## Testing the application

The [test](/src/test/java/com/thoughtworks/webstub/demo/FunctionalTest.java) uses test-specific configuration from [this file](/src/test/resources/test.properties).
It uses InProcTester to load the entire application within the test itself. At the same time, it uses WebStub to  stub response from the external review service.
It creates a Book resource first and then fetches it, asserting that the response contains the review for the book too.



