package com.example.musixBE.utils;

import com.example.musixBE.repositories.CommentRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor

public class CommentUtils {
    private final CommentRepository commentRepository;

    public void deleteComment(String commentId) {
        boolean isCommentExisted = commentRepository.findById(commentId).isPresent();
        if (!isCommentExisted) {
            //If no comment found, stop
            return;
        }
        var comment = commentRepository.findById(commentId).get();
        if (comment.getReplies().isEmpty()) {
            //If comment has no replies, safely delete it
            commentRepository.delete(comment);
            return;
        } else {
            var replies = comment.getReplies();
            replies.forEach(this::deleteComment);
            commentRepository.delete(comment);
        }
    }
}
