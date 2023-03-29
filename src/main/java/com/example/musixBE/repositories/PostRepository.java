package com.example.musixBE.repositories;

import com.example.musixBE.models.social.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {
}
