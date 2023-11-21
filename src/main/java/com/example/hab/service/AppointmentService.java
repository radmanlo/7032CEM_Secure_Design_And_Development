package com.example.hab.service;

import com.example.hab.dto.AppointmentDto;

import java.util.List;

public interface AppointmentService {

    /**
     * Create a new Appointment for a Health Center
     * @param appointmentDto
     * @return AppointmentDto if it was successful
     * otherwise
     * @return null
     */
    public AppointmentDto createAppointment (AppointmentDto appointmentDto);

    /**
     * Get all appointment of a Health Center by its email
     * @param HCEmail
     * @return AppointmentDto if it was successful
     * otherwise
     * @return null
     */
    public List<AppointmentDto> getAllAppointmentHC (String HCEmail);

    /**
     * Get all appointments of a patient
     * @param patientEmail
     * @return List<AppointmentDto> if it was susccessful
     * otherwise
     * @return null
     */
    public List<AppointmentDto> getAllAppointmentPatient (String patientEmail);


    /**
     * Get an appointment by its id
     * @param appoId
     * @return AppointmentDto if it was successful
     * otherwise
     * @return null
     */
    public AppointmentDto getAppointmentById (String appoId);

    /**
     * Update an appointment by its id
     * @param appointmentDto
     * @return updated Appointment if it was successful
     * otherwise
     * @return null
     */
    public AppointmentDto updateAppointment (AppointmentDto appointmentDto);

    /**
     * Delete an Appointment by its id
     * @param appoId
     * @return deleted Appointment if it was successful
     * otherwise
     * @return null
     */
    public AppointmentDto deleteAppointment (String appoId);
}
