package com.jp.posts.repositories;

import com.jp.posts.entities.PageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PageRepository extends JpaRepository<PageEntity,Long> {

    /**
     *  Query Methods
     */
    //Select * from page where title = ?1
    Optional<PageEntity> findByTitle(String title);

    /**
     *  JPQL
     */
    @Query("from PageEntity where title = :title")
    Optional<PageEntity> findByTitle2(String title);

    /**
     *  Query Methods
     */
    //delete from page where title = ?1
    @Modifying
    @Query("delete from PageEntity where title = :title")
    void deleteByTitle(String title);

    boolean existsByTitle(String title);
}
