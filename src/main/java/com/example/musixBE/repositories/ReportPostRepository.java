package com.example.musixBE.repositories;

import com.example.musixBE.models.social.ReportPost;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportPostRepository extends MongoRepository<ReportPost, String> {
}
