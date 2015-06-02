package com.rna.example.controllers

import com.rna.example.entities.Book
import com.rna.example.exceptions.TechnicalException
import com.rna.example.repositories.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/books")
class BookController {

    @Autowired
    BookRepository bookRepository

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    def Book getById(@PathVariable("id") Long id) {
        def book = bookRepository.findOne(id)
        if (book) {
            return book
        }

        throw new TechnicalException("Book not found")
    }
}
