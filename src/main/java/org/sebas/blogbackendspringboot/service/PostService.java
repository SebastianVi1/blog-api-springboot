package org.sebas.blogbackendspringboot.service;

import org.sebas.blogbackendspringboot.dto.PostDto;
import org.sebas.blogbackendspringboot.model.Post;
import org.sebas.blogbackendspringboot.repo.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class PostService {

    private PostRepo repo;

    @Autowired
    public void setRepo(PostRepo repo) {
        this.repo = repo;
    }

    // Convert the entity to a secure object to send
     private PostDto createPostDto(Post post) {
         return new PostDto (
                 post.getId(),
                 post.getTitle(),
                 post.getContent(),
                 post.getAuthor().getUsername(),
                 post.getCreatedDate(),
                 post.getCategory().getName(),
                 post.getComments()
         );
    }

    //Return a new list with dtos
    private List<PostDto> convertToDtoList(List<Post> postList){
        return postList.stream().map(this::createPostDto).toList();
    }

    public ResponseEntity<List<PostDto>> getPostsList() {
        List<Post> posts = repo.findAll();
        List<PostDto> postDtoList = convertToDtoList(posts);
        return new ResponseEntity<List<PostDto>>(postDtoList, HttpStatus.OK);
    }


    public ResponseEntity<?> postPost(Post post) {
        repo.save(post);
        return ResponseEntity.ok(post);
    }

    public ResponseEntity<PostDto> getPostByid( Long id){
         Post post  = repo.findById(id).get();
         PostDto postDto = createPostDto(post);
        return ResponseEntity.ok(postDto);
    }


    public ResponseEntity<?> editPost(Long id) {
        Optional<Post> post = repo.findById(id);

        if (post.isEmpty()){
            return new ResponseEntity<>("Post not found", HttpStatus.NOT_FOUND);
        }
        repo.save(post.get());
        return ResponseEntity.ok(post);
    }

    public ResponseEntity<?> deletePost(Long id){
       Optional<Post> post = repo.findById(id);

       if (post.isPresent()){
           return new ResponseEntity<>("Post not found", HttpStatus.BAD_REQUEST);
       }
       repo.delete(post.get());
       return new ResponseEntity<Post>(post.get(), HttpStatus.OK);
    }


    public ResponseEntity<List<PostDto>> searchPostByTitle(String title) {
        List<Post> post = repo.searchByTitle(title);
        List<PostDto> postDtoList = convertToDtoList(post);
        return ResponseEntity.ok(postDtoList);
    }
    public ResponseEntity<List<PostDto>> searchPostByAuthorId(Long id){
        List<Post> postList = repo.searchPostByAuthorId(id);
         List <PostDto> postDtoList = convertToDtoList(postList);
        return new ResponseEntity<List<PostDto>>(postDtoList, HttpStatus.OK) ;

    }

}
