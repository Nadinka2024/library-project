//package com.itgirl.library_project.repository;
//
//import com.itgirl.library_project.interfac.BookRepositoryCustom;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.persistence.criteria.CriteriaBuilder;
//import jakarta.persistence.criteria.CriteriaQuery;
//import jakarta.persistence.criteria.Root;
//import org.springframework.stereotype.Repository;
//
//import java.awt.print.Book;
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public class BookRepositoryCustomImpl implements BookRepositoryCustom {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Override
//    public Optional<Book> findBookByNameUsingCriteria(String name) {
//        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);
//        Root<Book> root = criteriaQuery.from(Book.class);
//        criteriaQuery.select(root)
//                .where(criteriaBuilder.equal(root.get("name"), name));
//
//        List<Book> result = entityManager.createQuery(criteriaQuery).getResultList();
//        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
//    }
//
//    @Override
//    public List<Book> findByNameUsingCriteria(String name) {
//        return List.of();
//    }
//}



