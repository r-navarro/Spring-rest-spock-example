package com.rna.example.entities

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Book {

    @Id
    @GeneratedValue
    Long id

    String title

    String author

    float price
}
