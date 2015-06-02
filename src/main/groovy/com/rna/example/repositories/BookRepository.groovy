package com.rna.example.repositories

import com.rna.example.entities.Book
import org.springframework.data.repository.CrudRepository


interface BookRepository extends CrudRepository<Book, Long> {

}
