package com.itgirl.library_project.servise;

import com.itgirl.library_project.entity.Book;
import com.itgirl.library_project.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j

public class BookService {

    @Autowired
    private BookRepository bookRepository;


    public Book addNewBook(Book book){
        return (Book) bookRepository.save(book);
    }

    public List getAllBook() {
        log.info("Get all books");
        return bookRepository.findAll();
    }

    public Book getBookById(Long id){
        System.out.println("Get book with id " + id);
        return (Book) bookRepository.getBookById(id);
    }

    public Book getBookByName(String bookName){
        System.out.println("Get book with name " + bookName);
        return (Book) bookRepository.getBookByName(bookName);
    }
}



