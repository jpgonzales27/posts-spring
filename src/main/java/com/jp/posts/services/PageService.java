package com.jp.posts.services;

import com.jp.posts.dtos.request.PageRequest;
import com.jp.posts.dtos.request.PostRequest;
import com.jp.posts.dtos.response.PageResponse;
import com.jp.posts.dtos.response.PostResponse;
import org.springframework.data.domain.Page;

public interface PageService {
    PageResponse create(PageRequest pageRequest);
    PageResponse readByTitle(String title);
    PageResponse update(PageRequest pageRequest,String title );
    void delete(String title );

    PostResponse createPost(PostRequest postRequest);
    void deletePost(Long idPost);
}
