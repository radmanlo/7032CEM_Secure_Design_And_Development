package com.example.hab.repository;

import com.example.hab.entity.Patient;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface PatientRepository extends UserRepository{

    Optional<Patient> findUserByEmail(String email);

    @Query(value = "{'email' : ?0}", delete = true)
    Optional<Patient> deleteAndReturnDeleted(String email);
}
