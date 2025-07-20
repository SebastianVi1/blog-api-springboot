package org.sebas.blogbackendspringboot.repo;

import org.sebas.blogbackendspringboot.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {

    @Query("SELECT p from Post p WHERE "+
            "LOWER(p.title) LIKE (concat('%', :title, '%'))"
    )
    List<Post> searchByTitle(String title);

    @Query("SELECT p from Post p WHERE p.author.id = :id")
    List<Post> searchPostByAuthorId(Long id);
}
