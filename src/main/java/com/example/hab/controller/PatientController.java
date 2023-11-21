package com.example.hab.controller;

import com.example.hab.dto.PatientDto;
import com.example.hab.dto.UserDto;
import com.example.hab.entity.Prescription;
import com.example.hab.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/getByEmail")
    public ResponseEntity<PatientDto> getPatientByEmail (@RequestParam String email){
        try{
            if (email == null || !isValidEmail(email))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            PatientDto patientDto = patientService.getUserByEmail(email);
            if (patientDto != null)
                return ResponseEntity.status(HttpStatus.FOUND).body(patientDto);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e){
            System.out.println("Exception getPatientByEmail in PatientController " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/allDis")
    public ResponseEntity<PatientDto> updateAllergyDiseases (@RequestBody PatientDto patientDto){
        try {
            PatientDto updateUser = patientService.updateAllergiesAndDiseases(patientDto);
            if (updateUser != null)
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(updateUser);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e){
            System.out.println("Exception updateAllergyDiseases in PatientController " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/addPrescription")
    public ResponseEntity<PatientDto> addPrescription (@RequestParam String patientEmail,
                                                       @Valid @RequestBody Prescription prescription){
        try {
            if (patientEmail == null || !isValidEmail(patientEmail))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            PatientDto patientDto = patientService.addPrescription(patientEmail, prescription);
            if (patientDto != null){
                return ResponseEntity.status(HttpStatus.CREATED).body(patientDto);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }catch (Exception e){
            System.out.println("Exception addPrescription in PatientController ==> " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getPrescription")
    public ResponseEntity<List<Prescription>> getAllPatientPrescription (@RequestParam String patientEmail){
        try {
            if (patientEmail == null || !isValidEmail(patientEmail))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            List<Prescription> prescriptions = patientService.getPrescriptions(patientEmail);
            if (prescriptions != null)
                return ResponseEntity.status(HttpStatus.FOUND).body(prescriptions);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }catch (Exception e){
            System.out.println("Exception getAllPatientPrescription in PatientController ==> " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<PatientDto> deleteUser (@RequestParam String email){
        try {
            if (email == null || !isValidEmail(email))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            PatientDto deletedUser = patientService.deletePatient(email);
            if (deletedUser != null)
                return ResponseEntity.status(HttpStatus.GONE).body(deletedUser);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e){
            System.out.println("Exception deleteUser in PatientController " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private boolean isValidEmail(String email){
        final String emailFormat =
                "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        final Pattern emailPattern = Pattern.compile(emailFormat);
        Matcher matcher = emailPattern.matcher(email);
        return matcher.matches();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handelInvalidArgument (MethodArgumentNotValidException exception){
        Map<String, String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->{
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        return errorMap;
    }
}
