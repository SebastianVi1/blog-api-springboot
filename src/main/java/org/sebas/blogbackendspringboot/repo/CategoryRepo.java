package org.sebas.blogbackendspringboot.repo;

import org.sebas.blogbackendspringboot.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
} 