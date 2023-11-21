package com.example.hab.repository;

import com.example.hab.entity.Patient;
import com.example.hab.entity.User;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

@Primary
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> deleteByEmail(String email);
}
