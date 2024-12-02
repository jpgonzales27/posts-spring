package com.jp.posts;

import com.jp.posts.dtos.request.PageRequest;
import com.jp.posts.services.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PostsApplication implements CommandLineRunner {

	@Autowired
	private PageService pageService;

	public static void main(String[] args) {
		SpringApplication.run(PostsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var request = PageRequest
				.builder()
				.userId(4L)
				.title("Juan Gonzales")
				.build();

		var response = pageService.create(request);

		System.out.println(response);
	}
}
