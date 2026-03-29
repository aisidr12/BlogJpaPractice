package com.devskiller.tasks.blog.service;

import com.devskiller.tasks.blog.model.Post;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

import com.devskiller.tasks.blog.model.dto.PostDto;
import com.devskiller.tasks.blog.repository.PostRepository;

@Service
public class PostService {

	private final PostRepository postRepository;

	public PostService(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	public PostDto getPost(Long id) {
		return postRepository.findById(id)
			.map(post -> new PostDto(post.getTitle(), post.getContent(), post.getCreationDate()))
			.orElse(null);
	}

	public List<PostDto> getPosts() {
		return postRepository.findAll().stream()
			.map(t -> new PostDto(t.getTitle(), t.getContent(), t.getCreationDate())).toList();
	}

	public Long createPost(PostDto postDto) {
		Post post = new Post();
		post.setTitle(postDto.title());
		post.setContent(postDto.content());
		post.setCreationDate(LocalDateTime.now());
		return postRepository.save(post).getId();
	}
}
