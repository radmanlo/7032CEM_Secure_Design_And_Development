package com.example.hab.service;

import com.example.hab.dto.PatientDto;
import com.example.hab.dto.UserDto;
import com.example.hab.entity.Patient;
import com.example.hab.entity.Prescription;
import com.example.hab.entity.Role;
import com.example.hab.entity.User;
import com.example.hab.repository.PatientRepository;
import com.example.hab.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServiceImp implements PatientService {
//    private final JwtService jwtService;
//    private final HttpServletResponse httpServletResponse;

    private final PatientRepository patientRepository;


    private final EncryptionService encryptionService;
    @Override
    public PatientDto getUserByEmail(String email) {
        Optional<Patient> foundPatient = patientRepository.findUserByEmail(email);
        if (foundPatient.isPresent()) {
            String allergiesDecrypted = "";
            String diseasesDecrypted = "";
            if (!foundPatient.get().getAllergies().isEmpty()) {
                try {
                    allergiesDecrypted = encryptionService.decrypt(foundPatient.get().getAllergies());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            if (!foundPatient.get().getDiseases().isEmpty()) {
                try {
                    diseasesDecrypted = encryptionService.decrypt(foundPatient.get().getDiseases());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            return PatientDto.builder()
                    .role(foundPatient.get().getRole())
                    .email(foundPatient.get().getEmail())
                    .firstName(foundPatient.get().getFirstName())
                    .lastName(foundPatient.get().getLastName())
                    .allergies(allergiesDecrypted.isEmpty() ? foundPatient.get().getAllergies() : allergiesDecrypted )
                    .diseases(diseasesDecrypted.isEmpty() ? foundPatient.get().getDiseases() : diseasesDecrypted )
                    .consent(foundPatient.get().isConsent())
                    .build();
        }
        return null;
    }

    @Override
    public PatientDto updateAllergiesAndDiseases(PatientDto patientDto){
        Optional<Patient> foundPatient = patientRepository.findUserByEmail(patientDto.getEmail());
        if (foundPatient.isPresent()){
            String allergiesEncrypted = "";
            String diseasesEncrypted = "";
            if (!patientDto.getAllergies().isEmpty()){
                try {
                    allergiesEncrypted = encryptionService.encrypt(patientDto.getAllergies());
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if (!patientDto.getDiseases().isEmpty()){
                try {
                    diseasesEncrypted = encryptionService.encrypt(patientDto.getDiseases());
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            foundPatient.get().setAllergies(!allergiesEncrypted.isEmpty()?
                    allergiesEncrypted : foundPatient.get().getAllergies());
            foundPatient.get().setDiseases(!diseasesEncrypted.isEmpty()?
                    diseasesEncrypted : foundPatient.get().getDiseases());
            Patient updatedPatient = patientRepository.save(foundPatient.get());
            return PatientDto.builder()
                    .role(updatedPatient.getRole())
                    .email(updatedPatient.getEmail())
                    .firstName(updatedPatient.getFirstName())
                    .lastName(updatedPatient.getLastName())
                    .allergies(updatedPatient.getAllergies())
                    .diseases(updatedPatient.getDiseases())
                    .consent(updatedPatient.isConsent())
                    .build();
        }
        return null;
    }

    @Override
    public PatientDto addPrescription(String patientEmail, Prescription prescription) {
        Optional<Patient> foundPatient = patientRepository.findUserByEmail(patientEmail);
        if (foundPatient.isPresent()){
            if (!prescription.getIllness().isEmpty()){
                try{
                    prescription.setIllness(encryptionService.encrypt(prescription.getIllness()));
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if (!prescription.getMedicine().isEmpty()){
                try {
                    prescription.setMedicine(encryptionService.encrypt(prescription.getMedicine()));
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            foundPatient.get().getPrescription().add(prescription);
            Patient updatedPatient = patientRepository.save(foundPatient.get());
            return PatientDto.builder()
                    .email(updatedPatient.getEmail())
                    .firstName(updatedPatient.getFirstName())
                    .lastName(updatedPatient.getLastName())
                    .prescription(updatedPatient.getPrescription())
                    .diseases(updatedPatient.getDiseases())
                    .allergies(updatedPatient.getAllergies())
                    .consent(updatedPatient.isConsent())
                    .role(updatedPatient.getRole())
                    .build();
        }
        return null;
    }

    @Override
    public List<Prescription> getPrescriptions(String patientEmail) {
        Optional<Patient> foundPatient = patientRepository.findUserByEmail(patientEmail);
        if (foundPatient.isPresent()){
            List<Prescription> prescriptions = new ArrayList<Prescription>();
            for (Prescription presc : foundPatient.get().getPrescription()){
                if (!presc.getMedicine().isEmpty()){
                    try{
                        presc.setMedicine(encryptionService.decrypt(presc.getMedicine()));
                    } catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                if (!presc.getIllness().isEmpty()){
                    try {
                        presc.setIllness(encryptionService.decrypt(presc.getIllness()));
                    } catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                prescriptions.add(presc);
            }
            return prescriptions;
        }
        return null;
    }

    @Override
    public PatientDto deletePatient(String email) {
        Optional<Patient> deletedPatient = patientRepository.deleteAndReturnDeleted(email);
        if (deletedPatient.isPresent()){
            return PatientDto.builder()
                    .role(deletedPatient.get().getRole())
                    .email(deletedPatient.get().getEmail())
                    .firstName(deletedPatient.get().getFirstName())
                    .lastName(deletedPatient.get().getLastName())
                    .allergies(deletedPatient.get().getAllergies())
                    .diseases(deletedPatient.get().getDiseases())
                    .consent(deletedPatient.get().isConsent())
                    .build();

        }
        return null;
    }

}
