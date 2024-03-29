package com.example.musixBE.repositories;

import com.example.musixBE.models.token.Token;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends MongoRepository<Token, String> {
    @Query("{\"user.username\": '?0'}")
    List<Token> findAllValidTokenByUser(String username);

    @Query("{\"user.username\": '?0', tokenType: '?1'}")
    List<Token> findAllValidTokenByUserAndTokenType(String username, String tokenType);

    @Query("{token:  '?0'}")
    Optional<Token> findByToken(String token);
}
