package com.example.musixBE.repositories;

import com.example.musixBE.models.music.Music;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface MusicRepository extends MongoRepository<Music, String> {
    @Query("{\"user.username\": '?0'}")
    Optional<Music> findByUsername(String username);
}
