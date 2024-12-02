package com.jp.posts.services;

import com.jp.posts.dtos.request.PageRequest;
import com.jp.posts.dtos.request.PostRequest;
import com.jp.posts.dtos.response.PageResponse;
import com.jp.posts.dtos.response.PostResponse;
import com.jp.posts.entities.PageEntity;
import com.jp.posts.repositories.PageRepository;
import com.jp.posts.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class PageServiceImpl implements PageService{

    private final PageRepository pageRepository;
    private final UserRepository userRepository;

    @Override
    public PageResponse create(PageRequest pageRequest) {
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
        return null;
    }

    @Override
    public PageResponse update(PageRequest pageRequest, String title) {
        return null;
    }

    @Override
    public void delete(String title) {

    }

    @Override
    public PostResponse createPost(PostRequest postRequest) {
        return null;
    }

    @Override
    public void deletePost(Long idPost) {

    }
}
