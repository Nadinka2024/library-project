package com.itgirl.library_project.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "books")
@Getter
@Setter
@AllArgsConstructor

public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(name = "book", nullable = false)
    private String bookName;
    @Column (name="author_name", nullable = false)
    private String authorName;
    @Column(name="author_surname",nullable = false)
    private String authorSurname;
    @Column(name = "genre",nullable = false)
    private String genreName;

    public Author() {}
}