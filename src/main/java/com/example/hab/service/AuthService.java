package com.example.hab.service;

import com.example.hab.dto.HealthCenterDto;
import com.example.hab.dto.PatientDto;
import com.example.hab.entity.HealthCenter;

public interface AuthService {

    /**
     * Create a new Patient store in mongoDB
     * then created a token for that
     * @param patientDto
     * @return PatientDto if it was successful
     * otherwise
     * @return null
     */
    PatientDto createPatient (PatientDto patientDto);

    /**
     * Create a new HC and store in mongoDB
     * then create a tokent for that
     * @param healthCenterDto
     * @return HealthCenterDto if it was successful
     * otherwise
     * @return null
     */
    HealthCenterDto createHC(HealthCenterDto healthCenterDto);

    /**
     * Authorize a patient based on its email and password
     * @param email
     * @param password
     * @return PatientDto if they were match
     * otherwise
     * @return null
     */
    PatientDto authPatient (String email, String password);

    /**
     * Authorize a Health Center based on its email and password
     * @param email
     * @param password
     * @return HealthCenterDto if they were match
     * otherwise
     * @return null
     */
    HealthCenterDto authHC (String email, String password);


}
