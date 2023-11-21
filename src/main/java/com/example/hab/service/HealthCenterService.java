package com.example.hab.service;

import com.example.hab.dto.AppointmentDto;
import com.example.hab.dto.HealthCenterDto;
import com.example.hab.dto.PatientDto;
import com.example.hab.dto.UserDto;
import com.example.hab.entity.Appointment;
import com.example.hab.entity.HealthCenter;
import com.example.hab.entity.Prescription;
import com.example.hab.entity.Status;

import java.util.List;

public interface HealthCenterService {


    /**
     * Get all Health Centers
     * @return list of HealthCenterDto
     * otherwise
     * @return null
     */
    public List<HealthCenterDto> getAllHealthCenters ();

    /**
     * get healthcenter by its email
     * @param email
     * @return HealthCenterDto if it finds
     * otherwise
     * @return null
     */
    public HealthCenterDto getHealthCenter(String email);

//    /**
//     * Get All Appointments of Patient health center by its id
//     * @param HCEmail Health Center Email
//     * @return list of Appointment if it is found successfully
//     * otherwise
//     * @return null
//     */
//    public List<AppointmentDto> getAllAppointments (String HCEmail);

//    /**
//     * update Appointment blocks into HealthCenter
//     * @param HCEmail Health Center Email
//     * @return updatedHealthCenter if it was successful
//     * otherwise
//     * @return null
//     */
//    public AppointmentDto addAppointmentBlock (String HCEmail,
//                                                AppointmentDto appointmentDto);

//    /**
//     * Update an appointment
//     * @param appointmentDto
//     * @return AppointmentDto if it was successful
//     * otherwise
//     * @return null
//     */
//    public AppointmentDto updateAppointment (AppointmentDto appointmentDto);

//
//    /**
//     * Update Patient health center by its id
//     * @param HCEmail Health Center Email
//     * @param appointment
//     * @param status
//     * @return HealthCenterDto which is updated successfully
//     * otherwise
//     * @return null
//     */
//    public HealthCenterDto updateAppointmentStatus (String HCEmail,
//                                                    Appointment appointment,
//                                                    Status status);
//
//    /**
//     * Add Patient into an appointment and
//     * make the appointment status pending
//     * @param HCEmail Health Center Email
//     * @param email
//     * @param appointment
//     * @return HealthCenterDto if it was successful
//     * otherwise
//     * @return null
//     */
//    public HealthCenterDto bookAppointment (String HCEmail,
//                                            String email,
//                                            Appointment appointment);

//    /**
//     *
//     * @param patientEmail
//     * @param prescription
//     * @return
//     */
//    public PatientDto addPrescription (String patientEmail, Prescription prescription);

    /**
     * Delete Patient health center
     * @param HCEmail Health Center Email
     * @return HealthCenterDto which is deleted successfully
     * otherwise
     * @return null
     */
    public HealthCenterDto deleteHealthCenter (String HCEmail);

//    /**
//     * Delete an appointment from a health center
//     * @param HCEmail
//     * @param appointment
//     * @return True if it is deleted successfully
//     * otherwise
//     * @return False
//     */
//    public boolean deleteAppointment (String HCEmail, Appointment appointment);
}
