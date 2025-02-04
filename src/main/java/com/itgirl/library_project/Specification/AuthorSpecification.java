package com.itgirl.library_project.Specification;

import com.itgirl.library_project.entity.Author;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class AuthorSpecification {

    public static Specification<Author> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.hasText(name)) {
                return criteriaBuilder.equal(root.get("name"), name);
            }
            return null;
        };
    }

    public static Specification<Author> hasSurname(String surname) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.hasText(surname)) {
                return criteriaBuilder.equal(root.get("surname"), surname);
            }
            return null;
        };
    }
}

