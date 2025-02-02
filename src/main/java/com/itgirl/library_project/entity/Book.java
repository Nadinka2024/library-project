package com.itgirl.library_project.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//public class Book {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private String name;
//
//    @JsonProperty("genre")
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "genre_id")
//    @JsonBackReference
//    private Genre genre;
//
//    @ManyToMany
//    @JoinTable(
//            name = "author_book",
//            joinColumns = @JoinColumn(name = "book_id"),
//            inverseJoinColumns = @JoinColumn(name = "author_id"))
//    @JsonProperty("authors")
//    @JsonManagedReference
//    private List<Author> authors;

public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")  // Здесь связываем с жанром
    @JsonProperty("genre")  // Убедитесь, что сериализация правильно настроена
    private Genre genre;

    @ManyToMany
    @JoinTable(
            name = "author_book",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    @JsonProperty("authors")
    private List<Author> authors;
}

