package com.jp.posts.services;

import com.jp.posts.dtos.response.PageResponse;
import com.jp.posts.entities.PageEntity;
import com.jp.posts.entities.PostEntity;
import com.jp.posts.repositories.PageRepository;
import com.jp.posts.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class PageServiceTest {

    @MockBean //similar to @Autowired for test
    private PageRepository pageRepository;
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private PageService target;

    @Test
        //Happy path
    void readByTitle_ShouldReturnPageResponse_WhenTitleExist() {
        String title = "TestTitle";

        PostEntity postEntity = new PostEntity();
        postEntity.setImg("http://img");
        postEntity.setContent("Some content");
        postEntity.setDateCreation(LocalDateTime.MIN);

        PageEntity pageEntity = new PageEntity();
        pageEntity.setTitle(title);
        pageEntity.setDateCreation(LocalDateTime.MIN);
        pageEntity.setPosts(List.of(postEntity));

        given(pageRepository.findByTitle(title))
                .willReturn(Optional.of(pageEntity));

        PageResponse result = target.readByTitle(title);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("TestTitle");
        assertThat(result.getDateCreation()).isEqualTo(LocalDateTime.MIN);
        assertThat(result.getPosts()).hasSize(1);
    }

    @Test //Unhappy path
    void readByTitle_ShouldReturnException_WhenTitleNotExist() {
        String title = "Invalid title";

        given(pageRepository.findByTitle(title))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> target.readByTitle(title))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Could not find title");

        verify(pageRepository).findByTitle(title);
    }
}
