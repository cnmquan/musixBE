package com.example.musixBE.repositories;

import com.example.musixBE.models.Token;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends MongoRepository<Token, String> {
    @Query("{\"user.username\": '?0'}")
    List<Token> findAllValidTokenByUser(String username);

    @Query("{token:  '?0'}")
    Optional<Token> findByToken(String token);
}
