package com.example.hab.service;

import com.example.hab.dto.AppointmentDto;
import com.example.hab.dto.HealthCenterDto;
import com.example.hab.dto.PatientDto;
import com.example.hab.entity.*;
import com.example.hab.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImp implements AppointmentService{

    private final AppointmentRepository appointmentRepository;

    @Override
    public AppointmentDto createAppointment(AppointmentDto appointmentDto) {
        Optional<Appointment> foundAppo =
                appointmentRepository.findByHealthCenterEmailAndDateAndTimeAndStatus(
                        appointmentDto.getHealthCenterDto().getEmail(),
                        appointmentDto.getDate(),
                        appointmentDto.getTime(),
                        appointmentDto.getStatus());
        if(!foundAppo.isPresent()){
            Appointment newAppo = Appointment.builder()
                    .healthCenter(HealthCenter.builder()
                            .role(Role.HC)
                            .name(appointmentDto.getHealthCenterDto().getName())
                            .email(appointmentDto.getHealthCenterDto().getEmail())
                            .build())
                    .date(appointmentDto.getDate())
                    .time(appointmentDto.getTime())
                    .status(Status.EMPTY)
                    .build();
            Appointment createdAppo = appointmentRepository.save(newAppo);
            return AppointmentDto.builder()
                    .healthCenterDto(HealthCenterDto.builder()
                            .email(createdAppo.getHealthCenter().getEmail())
                            .name(createdAppo.getHealthCenter().getName())
                            .role(createdAppo.getHealthCenter().getRole())
                            .build())
                    .date(createdAppo.getDate())
                    .time(createdAppo.getTime())
                    .status(Status.EMPTY)
                    .build();
        }
        return null;
    }

    @Override
    public List<AppointmentDto> getAllAppointmentHC (String HCEmail) {
        List<Appointment> appointments = appointmentRepository.findAll();
        List<AppointmentDto> appointmentDtos = new ArrayList<AppointmentDto>();
        for (Appointment appo: appointments){
            appointmentDtos.add(
                    AppointmentDto.builder()
                            .id(appo.getId())
                            .healthCenterDto(HealthCenterDto.builder()
                                    .name(appo.getHealthCenter().getName())
                                    .email(appo.getHealthCenter().getEmail())
                                    .role(appo.getHealthCenter().getRole())
                                    .build())
                            .patientDto(appo.getPatient() != null ? PatientDto.builder()
                                    .email(appo.getPatient().getEmail())
                                    .firstName(appo.getPatient().getFirstName())
                                    .lastName(appo.getPatient().getLastName())
                                    .build() :
                                    null)
                            .date(appo.getDate())
                            .time(appo.getTime())
                            .status(appo.getStatus())
                            .build()
            );
        }
        return appointmentDtos;
    }

    @Override
    public List<AppointmentDto> getAllAppointmentPatient(String patientEmail) {
        Optional<List<Appointment>> appointments = appointmentRepository.findByPatientEmail(patientEmail);
        List<AppointmentDto> appointmentDtos = new ArrayList<AppointmentDto>();
        if(appointments.isPresent()){
            for (Appointment appo : appointments.get()){
                appointmentDtos.add(
                        AppointmentDto.builder()
                                .id(appo.getId())
                                .healthCenterDto(HealthCenterDto.builder()
                                        .email(appo.getHealthCenter().getEmail())
                                        .name(appo.getHealthCenter().getName())
                                        .build())
                                .patientDto(PatientDto.builder()
                                        .email(appo.getPatient().getEmail())
                                        .lastName(appo.getPatient().getLastName())
                                        .firstName(appo.getPatient().getFirstName())
                                        .build())
                                .date(appo.getDate())
                                .time(appo.getTime())
                                .status(appo.getStatus())
                                .build()
                );
            }
        }
        return appointmentDtos;
    }


    @Override
    public AppointmentDto getAppointmentById(String appoId) {
        Optional<Appointment> foundAppointment = appointmentRepository.findById(appoId);
        if (foundAppointment.isPresent()){
            return AppointmentDto.builder()
                    .id(foundAppointment.get().getId())
                    .healthCenterDto(HealthCenterDto.builder()
                            .name(foundAppointment.get().getHealthCenter().getName())
                            .email(foundAppointment.get().getHealthCenter().getEmail())
                            .build())
                    .patientDto(PatientDto.builder()
                            .email(foundAppointment.get().getPatient().getEmail())
                            .firstName(foundAppointment.get().getPatient().getFirstName())
                            .lastName(foundAppointment.get().getPatient().getLastName())
                            .build())
                    .date(foundAppointment.get().getDate())
                    .time(foundAppointment.get().getTime())
                    .status(foundAppointment.get().getStatus())
                    .build();
        }
        return null;
    }

    @Override
    public AppointmentDto updateAppointment(AppointmentDto appointmentDto) {
        Optional<Appointment> foundAppointment = appointmentRepository.findById(appointmentDto.getId());
        if (foundAppointment.isPresent()){
            foundAppointment.get().setStatus(appointmentDto.getStatus() != null?
                    appointmentDto.getStatus() : foundAppointment.get().getStatus());
            foundAppointment.get().setDate(appointmentDto.getDate() != null?
                    appointmentDto.getDate() : foundAppointment.get().getDate());
            foundAppointment.get().setTime(appointmentDto.getTime() != null?
                    appointmentDto.getTime() : foundAppointment.get().getTime());
            foundAppointment.get().setPatient(appointmentDto.getPatientDto() != null?
                    Patient.builder()
                            .email(appointmentDto.getPatientDto().getEmail())
                            .firstName(appointmentDto.getPatientDto().getFirstName())
                            .lastName(appointmentDto.getPatientDto().getLastName())
                            .build() :
                    foundAppointment.get().getPatient());
            Appointment createdAppointment = appointmentRepository.save(foundAppointment.get());
            return AppointmentDto.builder()
                    .id(createdAppointment.getId())
                    .healthCenterDto(HealthCenterDto.builder()
                            .email(createdAppointment.getHealthCenter().getEmail())
                            .name(createdAppointment.getHealthCenter().getName())
                            .build())
                    .patientDto(PatientDto.builder()
                            .email(createdAppointment.getPatient().getEmail())
                            .firstName(createdAppointment.getPatient().getFirstName())
                            .lastName(createdAppointment.getPatient().getLastName())
                            .build())
                    .date(createdAppointment.getDate())
                    .time(createdAppointment.getTime())
                    .status(createdAppointment.getStatus())
                    .build();
        }
        return null;
    }

    @Override
    public AppointmentDto deleteAppointment(String appoId) {
        Optional<Appointment> deletedAppointment = appointmentRepository.deleteAppointmentById(appoId);
        if (deletedAppointment.isPresent()) {
            return AppointmentDto.builder()
                    .id(deletedAppointment.get().getId())
                    .healthCenterDto(HealthCenterDto.builder()
                            .name(deletedAppointment.get().getHealthCenter().getName())
                            .email(deletedAppointment.get().getHealthCenter().getEmail())
                            .build())
                    .patientDto(deletedAppointment.get().getPatient() != null?
                            PatientDto.builder()
                            .email(deletedAppointment.get().getPatient().getEmail())
                            .firstName(deletedAppointment.get().getPatient().getFirstName())
                            .lastName(deletedAppointment.get().getPatient().getLastName())
                            .build() :
                            null)
                    .date(deletedAppointment.get().getDate())
                    .time(deletedAppointment.get().getTime())
                    .status(deletedAppointment.get().getStatus())
                    .build();
        }
        return null;
    }
}
