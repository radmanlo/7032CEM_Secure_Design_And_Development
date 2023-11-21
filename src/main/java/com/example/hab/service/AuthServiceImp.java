package com.example.hab.service;

import com.example.hab.dto.HealthCenterDto;
import com.example.hab.dto.PatientDto;
import com.example.hab.dto.UserDto;
import com.example.hab.entity.HealthCenter;
import com.example.hab.entity.Patient;
import com.example.hab.entity.Role;
import com.example.hab.entity.User;
import com.example.hab.repository.HealthCenterRepository;
import com.example.hab.repository.PatientRepository;
import com.example.hab.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final HttpServletResponse httpServletResponse;
    private final AuthenticationManager authenticationManager;
    private final PatientRepository patientRepository;
    private final HealthCenterRepository healthCenterRepository;

    private final EncryptionService encryptionService;

    @Override
    public PatientDto createPatient(PatientDto patientDto) {
        Optional<User> foundPatient = userRepository.findByEmail(patientDto.getEmail());
        if (!foundPatient.isPresent()){
            String encodePassword = passwordEncoder.encode(patientDto.getPassword());
            patientDto.setPassword(encodePassword);
            if (!patientDto.getDiseases().isEmpty()){
                try {
                    patientDto.setDiseases(encryptionService.encrypt(patientDto.getDiseases()));
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if (!patientDto.getAllergies().isEmpty()){
                try {
                    patientDto.setAllergies(encryptionService.encrypt(patientDto.getAllergies()));
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            Patient patient = Patient.builder()
                    .role(Role.USER)
                    .email(patientDto.getEmail())
                    .password(patientDto.getPassword())
                    .firstName(patientDto.getFirstName())
                    .lastName(patientDto.getLastName())
                    .diseases(patientDto.getDiseases())
                    .allergies(patientDto.getAllergies())
                    .consent(patientDto.isConsent())
                    .build();
            Patient createdPatient = patientRepository.save(patient);
            PatientDto createdPatientDto = PatientDto.builder()
                    .role(createdPatient.getRole())
                    .email(createdPatient.getEmail())
                    .firstName(createdPatient.getFirstName())
                    .lastName(createdPatient.getLastName())
                    .diseases(createdPatient.getDiseases())
                    .allergies(createdPatient.getAllergies())
                    .consent(createdPatient.isConsent())
                    .build();
            createdTokenAndCookie(createdPatientDto);
            return createdPatientDto;
        }
        return null;
    }

    @Override
    public HealthCenterDto createHC(HealthCenterDto healthCenterDto) {
        Optional<User> foundPatient = userRepository.findByEmail(healthCenterDto.getEmail());
        if (!foundPatient.isPresent()){
            String encodePassword = passwordEncoder.encode(healthCenterDto.getPassword());
            healthCenterDto.setPassword(encodePassword);
            HealthCenter HC = HealthCenter.builder()
                    .role(Role.HC)
                    .email(healthCenterDto.getEmail())
                    .password(healthCenterDto.getPassword())
                    .role(Role.HC)
                    .name(healthCenterDto.getName())
                    .build();
            HealthCenter createdHC = healthCenterRepository.save(HC);
            HealthCenterDto res = HealthCenterDto.builder()
                    .role(createdHC.getRole())
                    .email(createdHC.getEmail())
                    .name(createdHC.getName())
                    .build();
            createdTokenAndCookie(res);
            return res;
        }
        return null;
    }

    @Override
    public PatientDto authPatient(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password
                )
        );
        Optional<Patient> foundPatient = patientRepository.findUserByEmail(email);
        if (foundPatient.isPresent()){
            if (foundPatient.get().getRole() == Role.USER) {
                PatientDto foundPatientDto = PatientDto.builder()
                        .role(foundPatient.get().getRole())
                        .email(foundPatient.get().getEmail())
                        .firstName(foundPatient.get().getFirstName())
                        .lastName(foundPatient.get().getLastName())
                        .allergies(foundPatient.get().getAllergies())
                        .diseases(foundPatient.get().getDiseases())
                        .consent(foundPatient.get().isConsent())
                        .build();
                createdTokenAndCookie(foundPatientDto);
                return foundPatientDto;
            }
        }
        return null;
    }

    @Override
    public HealthCenterDto authHC(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password
                )
        );
        Optional<HealthCenter> foundHC = healthCenterRepository.findUserByEmail(email);
        if (foundHC.isPresent()){
            if (foundHC.get().getRole() == Role.HC) {
                HealthCenterDto HCDto = HealthCenterDto.builder()
                        .email(foundHC.get().getEmail())
                        .name(foundHC.get().getName())
                        .build();
                createdTokenAndCookie(HCDto);
                return HCDto;
            }
        }
        return null;
    }

    private void createdTokenAndCookie(UserDto userDto) {
        String jwtToken = jwtService.generateToken(userDto);
        System.out.println(jwtToken);
        Cookie cookie = new Cookie("jwt", jwtToken);
        //cookie.setHttpOnly(true);
        cookie.setMaxAge(100000);
        cookie.setPath("/");
        //cookie.setSecure(true);
        httpServletResponse.addCookie(cookie);
        //return jwtToken;
    }
}
