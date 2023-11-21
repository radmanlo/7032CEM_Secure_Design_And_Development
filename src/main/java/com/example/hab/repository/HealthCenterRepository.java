package com.example.hab.repository;

import com.example.hab.dto.HealthCenterDto;
import com.example.hab.entity.HealthCenter;
import com.example.hab.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import javax.swing.text.html.Option;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

public interface HealthCenterRepository extends UserRepository {

    Optional<HealthCenter> findUserByEmail (String email);

    @Query(value = "{'email' : ?0}", delete = true)
    Optional<HealthCenter> deleteAndReturnDeleted(String id);

    @Query(value = "{ 'email' : ?0, 'appointments' : { $elemMatch: { 'date': ?1, 'time': ?2 } } }")
    Optional<HealthCenter> getHCByIdDateTime(String email, String date, String time);

    @Query(value = "{'email': ?0, 'appointments': {$elemMatch:  {'date':  ?1, 'time':  ?2 } } }", delete = true)
    Optional<HealthCenter> deleteByEmailAndAppointmentsDateAndTime(String email, String date, String time);


    List<HealthCenter> findAllByRole (String role);
}
