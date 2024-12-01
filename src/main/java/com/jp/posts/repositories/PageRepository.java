package com.jp.posts.repositories;

import com.jp.posts.entities.PageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageRepository extends JpaRepository<PageEntity,Long> {
}
