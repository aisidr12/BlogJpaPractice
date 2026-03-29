package com.devskiller.tasks.blog.rest;

import com.devskiller.tasks.blog.model.dto.CommentDto;
import com.devskiller.tasks.blog.model.dto.NewCommentDto;
import com.devskiller.tasks.blog.service.CommentService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.devskiller.tasks.blog.model.dto.PostDto;
import com.devskiller.tasks.blog.service.PostService;

@Controller
@RestController
@RequestMapping("/posts")
public class PostController {

	private final PostService postService;
	private final CommentService commentService;


	public PostController(PostService postService, CommentService commentService) {
		this.postService = postService;
		this.commentService = commentService;
	}

	@GetMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public PostDto getPost(@PathVariable Long id) {
		return postService.getPost(id);
	}

	@PostMapping(value = "/post")
	@ResponseStatus(HttpStatus.CREATED)
	public Long createPost(@RequestBody PostDto postDto) {
		return postService.createPost(postDto);
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<PostDto> getPost() {
		return postService.getPosts();
	}


	@PostMapping("/{id}/comments")
	@ResponseStatus(HttpStatus.CREATED)
	public Long postComments(@PathVariable Long id, @RequestBody NewCommentDto newCommentDto) {
		return commentService.addComment(id, newCommentDto);
	}

	@GetMapping(value = "/{id}/comments")
	@ResponseStatus(HttpStatus.OK)
	public List<CommentDto> getComments(@PathVariable Long id) {
		return commentService.getCommentsForPost(id);
	}


}
