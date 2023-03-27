package com.example.musixBE.repositories;

import com.example.musixBE.models.social.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, String> {
}
