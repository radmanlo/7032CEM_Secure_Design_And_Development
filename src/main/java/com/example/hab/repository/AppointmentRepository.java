package com.example.hab.repository;

import com.example.hab.entity.Appointment;
import com.example.hab.entity.Status;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {

    Optional<Appointment> findByHealthCenterEmailAndDateAndTimeAndStatus (String HCEmail,
                                                                String date,
                                                                String time,
                                                                Status status);
    Optional<List<Appointment>> findByPatientEmail (String patientEmail);

    Optional<Appointment> deleteAppointmentById (String appoId);
}
