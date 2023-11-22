package com.example.hab.controller;

import com.example.hab.dto.HealthCenterDto;
import com.example.hab.dto.PatientDto;
import com.example.hab.entity.Role;
import com.example.hab.service.AuthService;
import com.example.hab.service.HealthCenterService;
import com.example.hab.service.JwtService;
import com.example.hab.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CrossOrigin(origins = "https://habsecurity.azurewebsites.net")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    private final JwtService jwtService;

    private final HealthCenterService healthCenterService;

    private final PatientService patientService;

    @PostMapping("/hc/create")
    public ResponseEntity<HealthCenterDto> createHealthCenter (@Valid @RequestBody HealthCenterDto healthCenterDto){
        try{
            HealthCenterDto newHealthCenter = authService.createHC(healthCenterDto);
            if (newHealthCenter != null)
                return ResponseEntity.status(HttpStatus.CREATED).body(newHealthCenter);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }catch (Exception e){
            System.out.println("Exception createHealthCenter in HealthCenterController " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PostMapping("/patient/create")
    public ResponseEntity<PatientDto> registerPatient (@Valid @RequestBody PatientDto patientDto){
        try{
            PatientDto patient = authService.createPatient(patientDto);
            if (patient != null){
                return ResponseEntity.status(HttpStatus.CREATED).body(patient);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e){
            System.out.println("Exception RegisterPatient => " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/patient")
    public ResponseEntity<PatientDto> authPatient (@RequestParam String email,
                                                   @RequestParam String password){
        try{
            if (email == null || !isValidEmail(email) || password == null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            PatientDto patient = authService.authPatient(email, password);
            if (patient != null)
                return ResponseEntity.status(HttpStatus.FOUND).body(patient);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }catch (Exception e){
            System.out.println("Exception authPatient => " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/hc")
    public ResponseEntity<HealthCenterDto> authHC (@RequestParam String email,
                                                   @RequestParam String password){
        try{
            if (email == null || !isValidEmail(email) || password == null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            HealthCenterDto HC = authService.authHC(email, password);
            if (HC != null)
                return ResponseEntity.status(HttpStatus.FOUND).body(HC);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }catch (Exception e){
            System.out.println("Exception authPatient => " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/isAuthenticated/HC")
    public ResponseEntity<Boolean> isAuthenticatedHC (@RequestHeader("Authorization") String token){
        try {
            String jwt = token.substring(7);
            String hcEmail = jwtService.extractUsername(jwt);
            HealthCenterDto res = healthCenterService.getHealthCenter(hcEmail);
            if (res != null && res.getRole().equals(Role.HC)) {
                return ResponseEntity.status(HttpStatus.OK).body(true);
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(false);
        }catch (Exception e){
            System.out.println("isAuthenticatedHC => " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/isAuthenticated/patient")
    public ResponseEntity<Boolean> isAuthenticatedPatient (@RequestHeader("Authorization") String token){
        try {
            String jwt = token.substring(7);
            String patientEmail = jwtService.extractUsername(jwt);
            PatientDto res = patientService.getUserByEmail(patientEmail);
            if (res != null && res.getRole().equals(Role.USER)) {
                return ResponseEntity.status(HttpStatus.OK).body(true);
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(false);
        }catch (Exception e){
            System.out.println("isAuthenticatedHC => " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private static boolean isValidEmail(String email){
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
