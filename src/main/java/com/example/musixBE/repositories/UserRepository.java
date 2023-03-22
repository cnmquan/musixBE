package com.example.musixBE.repositories;

import com.example.musixBE.models.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    @Query("{username:  '?0'}")
    Optional<User>findByUsername(String username);

    @Query("{$or: [{\"username\": '?0'},{\"email\": '?1'}]}")
    Optional<User>findByUsernameOrEmail(String username, String email);

    @Query("{$or: [{\"username\": '?0'}, {\"fullName\" :  '?1'}]}")
    Optional<User>findByUsernameOrFullName(String username, String fullName);

    @Query("{'profile.fullName': {$regex: RegExp('?0', 'i')}}")
    List<Optional<User>> findByFullName(String fullName);
}
