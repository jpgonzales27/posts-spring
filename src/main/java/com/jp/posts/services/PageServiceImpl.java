package com.jp.posts.services;

import com.jp.posts.dtos.request.PageRequest;
import com.jp.posts.dtos.request.PostRequest;
import com.jp.posts.dtos.response.PageResponse;
import com.jp.posts.dtos.response.PostResponse;
import com.jp.posts.entities.PageEntity;
import com.jp.posts.entities.PostEntity;
import com.jp.posts.repositories.PageRepository;
import com.jp.posts.repositories.UserRepository;
import exceptions.TitleNotValidException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class PageServiceImpl implements PageService {

    private final PageRepository pageRepository;
    private final UserRepository userRepository;

    @Override
    public PageResponse create(PageRequest pageRequest) {
        validTitle(pageRequest.getTitle());
        /*
         * Recibimos un page DTO y lo mapeamos a una entidad
         */
        final var pageEntity = new PageEntity(); //create object(entity) to persist in Database
        BeanUtils.copyProperties(pageRequest, pageEntity);//Copy properties from argument(page) in entity

        final var user = this.userRepository.findById(pageRequest.getUserId())  //Search user corresponding to page
                .orElseThrow();

        /*
         * seteamos los datos que no estan en el page DTO enviado para guardar la entidad en la BD
         */
        pageEntity.setDateCreation(LocalDateTime.now()); //set date now
        pageEntity.setUser(user); //create relationship between users and page
        pageEntity.setPosts(new ArrayList<>()); //set empty list

        var pageCreated = pageRepository.save(pageEntity); //upsert id exist id update else insert

        /*
         * Mapeamos el objeto Page de respuesta a un DTO de response page
         */
        final var pageResponse = new PageResponse();//create dto for response
        BeanUtils.copyProperties(pageCreated, pageResponse);//copy properties from entity(pageCreated) in response
        return pageResponse;
    }

    @Override
    public PageResponse readByTitle(String title) {
        final var entityRepose = pageRepository.findByTitle(title).orElseThrow(() -> new IllegalArgumentException("Could not find title"));
        final var pageResponse = new PageResponse();
        BeanUtils.copyProperties(entityRepose, pageResponse);

        List<PostResponse> postResponses = entityRepose.getPosts()
                .stream()
                .map(postEntity -> PostResponse.builder()
                        .img(postEntity.getImg())
                        .content(postEntity.getContent())
                        .dateCreation(postEntity.getDateCreation())
                        .build()
                ).toList();

        pageResponse.setPosts(postResponses);
        return pageResponse;
    }

    @Override
    public PageResponse update(PageRequest pageRequest, String title) {
        validTitle(pageRequest.getTitle());
        final var entityFromDB = this.pageRepository.findByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException("Title not found")); //Find by title and handle errors

        entityFromDB.setTitle(pageRequest.getTitle()); //update fields from param page

        var pageCreated = this.pageRepository.save(entityFromDB);

        final var response = new PageResponse();//create dto for response

        BeanUtils.copyProperties(pageCreated, response);//copy properties from entity(pageCreated) in reponse
        return response;
    }

    @Override
    public void delete(String title) {
        //final var entityFromDB = this.pageRepository.findByTitle(title)
        //        .orElseThrow(() -> new IllegalArgumentException("Title not found"));
        //this.pageRepository.delete(entityFromDB);
        //
        //this.pageRepository.deleteById(1L);

        if (this.pageRepository.existsByTitle(title)) {
            log.info("Delinting page");
            this.pageRepository.deleteByTitle(title);
        } else {
            log.error("Error to delete");
            throw new IllegalArgumentException("Cant delete because id not exist");
        }
    }

    @Override
    public PageResponse createPost(PostRequest postRequest, String title) {
        final var pageToUpdate = this.pageRepository.findByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException("Title not found")); //Find by title and handle errors

        final var postEntity = new PostEntity(); // create entity to insert

        BeanUtils.copyProperties(postRequest, postEntity); // copy fields from dto to entity
        postEntity.setDateCreation(LocalDateTime.now());

        pageToUpdate.addPost(postEntity);

        final var responseEntity = this.pageRepository.save(pageToUpdate); // update

        final var response = new PageResponse(); // create response
        BeanUtils.copyProperties(responseEntity, response); // copy fields from objet updated

        final List<PostResponse> postResponses = responseEntity.getPosts() //map posts from db -> dto(response)
                .stream()// convert to stream
                .map(postE ->    //transform postEntity to postResponse
                        PostResponse
                                .builder()
                                .img(postE.getImg())
                                .content(postE.getContent())
                                .dateCreation(postE.getDateCreation())
                                .build()
                )
                .toList();  // convert to list

        response.setPosts(postResponses);
        return response;
    }

    @Override
    public void deletePost(Long idPost,String title) {
        final var pageToUpdate = this.pageRepository.findByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException("Title not found")); //Find by title and handle errors

        final var postToDelete = pageToUpdate.getPosts()
                .stream()
                .filter(post -> post.getId().equals(idPost))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("post id not found"));

        pageToUpdate.removePost(postToDelete);
        pageRepository.save(pageToUpdate); // update
    }

    private void validTitle(String title) {
        if (title.contains("hola") || title.contains("adios")) {
            throw new TitleNotValidException("Title cant contain bad words");
        }
    }
}
