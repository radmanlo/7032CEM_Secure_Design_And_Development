package com.example.hab.service;


import com.example.hab.dto.PatientDto;
import com.example.hab.entity.Prescription;

import java.util.List;

public interface PatientService {

    /**
     * Get Patient by email
     * @param email
     * @return PatientDto if it finds Patient by that Patient
     * otherwise
     * @return null
     */
    PatientDto getUserByEmail (String email);

    /**
     * Update Allergies and Diseases
     * @param patientDto
     * @return updated PatientDto if it was successful
     * otherwise
     * @return null
     */
    PatientDto updateAllergiesAndDiseases (PatientDto patientDto);

    /**
     * Add a prescription for a user
     * @param patientEmail
     * @param prescription
     * @return updated PatientDto if it was successful
     * otherwise
     * @return null
     */
    PatientDto addPrescription(String patientEmail, Prescription prescription);

    /**
     * Get All prescriptions by patient email address
     * @param patientEmail
     * @return List<Prescription> if it was successful
     * otherwise
     * @return null
     */
    List<Prescription> getPrescriptions (String patientEmail);

    /**
     * Delete Patient by its email
     * @param email
     * @return UserDto which is deleted
     * otherwise
     * @return null
     */
    PatientDto deletePatient (String email);



}
