package com.devskiller.tasks.blog.service;

import com.devskiller.tasks.blog.model.Comment;
import com.devskiller.tasks.blog.model.dto.CommentDto;
import com.devskiller.tasks.blog.model.dto.NewCommentDto;
import com.devskiller.tasks.blog.repository.CommentRepository;
import com.devskiller.tasks.blog.repository.PostRepository;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

	private final CommentRepository commentRepository;
	private final PostRepository postRepository;

	public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
		this.commentRepository = commentRepository;
		this.postRepository = postRepository;
	}

	/**
	 * Returns a list of all comments for a blog post with passed id.
	 *
	 * @param postId id of the post
	 * @return list of comments sorted by creation date descending - most recent first
	 */
	@Transactional
	public List<CommentDto> getCommentsForPost(Long postId) {
			return postRepository.findById(postId)
			.map(post -> post.getComments()
				.stream()
				.sorted(Comparator.comparing(Comment::getCreated).reversed())
				.map(t -> new CommentDto(t.getId(), t.getComment(), t.getAuthor(), t.getCreated()))
				.toList())
			.orElse(List.of());
	}

	/**
	 * Creates a new comment
	 *
	 * @param postId        id of the post
	 * @param newCommentDto data of new comment
	 * @return id of the created comment
	 * @throws IllegalArgumentException if postId is null or there is no blog post for passed
	 *                                  postId
	 */
	@Transactional
	public Long addComment(Long postId, NewCommentDto newCommentDto) {
		return postRepository.findById(postId).map(post -> {
			Comment comment = mapToComment(newCommentDto);
			post.getComments().add(comment);
			Comment newComment = commentRepository.save(comment);
			postRepository.save(post);
			return newComment.getId();
		}).orElseThrow(() -> new RuntimeException("Post Not Found"));
	}


	private Comment mapToComment(NewCommentDto newCommentDto) {
		Comment comment = new Comment();
		comment.setComment(newCommentDto.content());
		comment.setAuthor(newCommentDto.author());
		comment.setCreated(LocalDateTime.now());
		return comment;
	}
}
