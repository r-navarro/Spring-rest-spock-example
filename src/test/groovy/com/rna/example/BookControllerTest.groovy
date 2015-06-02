package com.rna.example

import com.rna.example.controllers.BookController
import com.rna.example.controllers.GlobalExceptionHandler
import com.rna.example.entities.Book
import com.rna.example.repositories.BookRepository
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders as ReqBuilder
import org.springframework.test.web.servlet.result.MockMvcResultMatchers as Matcher
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.method.HandlerMethod
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod
import spock.lang.Specification

import java.lang.reflect.Method

class BookControllerTest extends Specification {

    MockMvc mockMvc

    BookRepository bookRepository = Mock(BookRepository)

    def setup() {
        def bookController = new BookController()
        bookController.bookRepository = bookRepository
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).setHandlerExceptionResolvers(createExceptionResolver()).build()
    }

    private ExceptionHandlerExceptionResolver createExceptionResolver() {
        def handlerExceptionResolver = new ExceptionHandlerExceptionResolver() {
            protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod, Exception exception) {
                Method method = new ExceptionHandlerMethodResolver(GlobalExceptionHandler).resolveMethod(exception)
                return new ServletInvocableHandlerMethod(new GlobalExceptionHandler(), method)
            }
        }
        handlerExceptionResolver.afterPropertiesSet()
        handlerExceptionResolver.getMessageConverters().add(new MappingJackson2HttpMessageConverter())
        return handlerExceptionResolver
    }

    def "Test get by id rest service"() {
        setup:
        bookRepository.findOne(1) >> new Book(id: 1, title: "Book1")


        when:
        def request = mockMvc.perform(ReqBuilder.get("/books/1"))

        then:
        request.andExpect(Matcher.status().isOk())
        request.andExpect(Matcher.jsonPath("id").value(1))
        request.andExpect(Matcher.jsonPath("title").value("Book1"))
    }

    def "Test get by id rest service with user not found"() {
        setup:
        bookRepository.findOne(_) >> null


        when:
        def request = mockMvc.perform(ReqBuilder.get("/books/1"))

        then:
        request.andExpect(Matcher.status().isNotFound())
        request.andExpect(Matcher.jsonPath("message").value("Book not found"))
        request.andExpect(Matcher.jsonPath("request").value("/books/1"))
    }
}
