package com.itgirl.library_project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.Id;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @ManyToMany
    @JoinTable(
            name = "author_book",
            inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id"),
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"))
    private Set<Author> authors;

    @CreationTimestamp
    @Column(name = "Book_dt")
    private Timestamp data;
}