package com.example.hab.service;

import com.example.hab.dto.AppointmentDto;
import com.example.hab.dto.HealthCenterDto;
import com.example.hab.dto.PatientDto;
import com.example.hab.dto.UserDto;
import com.example.hab.entity.*;
import com.example.hab.repository.AppointmentRepository;
import com.example.hab.repository.HealthCenterRepository;
import com.example.hab.repository.PatientRepository;
import com.example.hab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HealthCenterServiceImp implements HealthCenterService{

    private final HealthCenterRepository healthCenterRepository;

    private final PatientRepository patientRepository;

    private final AppointmentService appointmentService;

    @Override
    public List<HealthCenterDto> getAllHealthCenters (){
        List<HealthCenterDto> healthCenterDtoList = new ArrayList<HealthCenterDto>();
        healthCenterRepository.findAllByRole("HC").forEach(
            healthCenter -> healthCenterDtoList.add(
                HealthCenterDto.builder()
                        .role(healthCenter.getRole())
                        .email(healthCenter.getEmail())
                        .name(healthCenter.getName())
                        .build()
            )
        );
        System.out.println("LISt ==>> " + healthCenterDtoList.toString());
        return healthCenterDtoList;
    }

    @Override
    public HealthCenterDto getHealthCenter(String email) {
        Optional<HealthCenter> foundHC = healthCenterRepository.findUserByEmail(email);
        if (foundHC.isPresent()){
            return HealthCenterDto.builder()
                    .email(foundHC.get().getEmail())
                    .name(foundHC.get().getName())
                    .role(foundHC.get().getRole())
                    .build();
        }
        else return null;
    }

//    @Override
//    public List<AppointmentDto> getAllAppointments (String email) {
//        List<AppointmentDto> appointmentDtos = appointmentService.getAllAppointment(email);
//        if (!appointmentDtos.isEmpty())
//            return appointmentDtos;
////        System.out.println("NOT FOUND HEALTH CENTER IN GetAllAppointments IN HealthCenterServiceImp");
//        return null;
//    }

//    @Override
//    public AppointmentDto addAppointmentBlock (String email, AppointmentDto appointmentDto) {
//        Optional<HealthCenter> foundHC = healthCenterRepository.findUserByEmail(email);
//        if (foundHC.isPresent()){
//            AppointmentDto createdAppo = appointmentService.createAppointment(email,appointmentDto);
//            if (createdAppo != null) {
//                return createdAppo;
//            }
//        }
//        return null;
//    }

//    @Override
//    public AppointmentDto updateAppointment(AppointmentDto appointmentDto) {
//        AppointmentDto updatedAppointment = appointmentService.updateAppointment()
//    }

//    @Override
//    public HealthCenterDto updateAppointmentStatus (
//            String HCEmail,
//            Appointment appointment,
//            Status status) {
//        Optional<HealthCenter> foundHC = healthCenterRepository
//                .getHCByIdDateTime(
//                        HCEmail,
//                        appointment.getDate(),
//                        appointment.getTime()
//                );
//        if (foundHC.isPresent()){
//            foundHC.get().getAppointments().forEach(appointCur -> {
//                if (appointCur.getDate().equals(appointment.getDate()) &&
//                        appointCur.getTime().equals(appointment.getTime()) &&
//                        appointCur.getStatus().equals(Status.PENDING))
//                {
//                    if (status.equals(Status.ACCEPTED))
//                        appointCur.setStatus(Status.ACCEPTED);
//                    else {
//                        appointCur.setStatus(Status.EMPTY);
//                        appointCur.setUser(null);
//                    }
//                    HealthCenter HC = healthCenterRepository.save(foundHC.get());
//                }
//            });
//            HealthCenterDto HCDto = HealthCenterDto.builder()
//                    .name(foundHC.get().getName())
//                    .appointments(foundHC.get().getAppointments())
//                    .build();
//            return HCDto;
//        }
//        return null;
//    }
//
//    @Override
//    public HealthCenterDto bookAppointment(String HCEmail,
//                                           String patientEmail,
//                                           Appointment appointment) {
//        Optional<HealthCenter> foundHC = healthCenterRepository
//                .getHCByIdDateTime(
//                        HCEmail,
//                        appointment.getDate(),
//                        appointment.getTime());
//        if (foundHC.isPresent()){
//            foundHC.get().getAppointments().forEach(appointCur -> {
//                if (appointCur.getDate().equals(appointment.getDate()) &&
//                        appointCur.getTime().equals(appointment.getTime()) &&
//                        appointCur.getStatus().equals(Status.EMPTY)) {
//                    Optional<Patient> foundPatient = patientRepository.findUserByEmail(patientEmail);
//                    if (foundPatient.isPresent()){
//                        foundPatient.get().setPassword(null);
//                        appointCur.setUser(foundPatient.get());
//                        appointCur.setStatus(Status.PENDING);
//                    }
//                }
//            });
//            HealthCenter updatedHC = healthCenterRepository.save(foundHC.get());
//            HealthCenterDto HCDto = HealthCenterDto.builder()
//                    .email(updatedHC.getEmail())
//                    .name(updatedHC.getName())
//                    .appointments(updatedHC.getAppointments())
//                    .build();
//            return HCDto;
//        }
//        return null;
//    }



//    @Override
//    public PatientDto addPrescription(String patientEmail, Prescription prescription) {
//        Optional<Patient> foundPatient = patientRepository.findUserByEmail(patientEmail);
//        if (foundPatient.isPresent()){
//            foundPatient.get().setPrescription(prescription);
//            Patient updatedPatient = patientRepository.save(foundPatient.get());
//            return PatientDto.builder()
//                    .email(updatedPatient.getEmail())
//                    .firstName(updatedPatient.getFirstName())
//                    .lastName(updatedPatient.getLastName())
//                    .prescription(updatedPatient.getPrescription())
//                    .diseases(updatedPatient.getDiseases())
//                    .allergies(updatedPatient.getAllergies())
//                    .consent(updatedPatient.isConsent())
//                    .role(updatedPatient.getRole())
//                    .build();
//        }
//        return null;
//    }


    @Override
    public HealthCenterDto deleteHealthCenter(String healthCenter_id) {
        Optional<HealthCenter> deletedHC = healthCenterRepository.deleteAndReturnDeleted(healthCenter_id);
        if (deletedHC.isPresent()){
            HealthCenterDto deletedHCDto = HealthCenterDto.builder()
                    .name(deletedHC.get().getName())
                    .build();
            return deletedHCDto;
        }
        return null;
    }

//    @Override
//    public boolean deleteAppointment(String HCEmail, Appointment appointment) {
//        System.out.println("In Service = > " + HCEmail + " " + appointment.getTime() + " " + appointment.getTime());
//        Optional<HealthCenter> healthCenter =
//                healthCenterRepository.getHCByIdDateTime(HCEmail, appointment.getDate(), appointment.getTime());
//        if(healthCenter.isPresent()){
//            healthCenterRepository.deleteByEmailAndAppointmentsDateAndTime(
//                    HCEmail, appointment.getDate(), appointment.getTime());
//            System.out.println("I am Here");
//            return true;
////            return false;
//        }
//        return false;
//    }
}
