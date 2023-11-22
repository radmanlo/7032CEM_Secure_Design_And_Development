package com.example.hab.controller;

import com.example.hab.dto.AppointmentDto;
import com.example.hab.dto.HealthCenterDto;
import com.example.hab.dto.PatientDto;
import com.example.hab.entity.Appointment;
import com.example.hab.entity.Prescription;
import com.example.hab.entity.Status;
import com.example.hab.service.HealthCenterService;
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

@CrossOrigin(origins = "https://habsecurity.azurewebsites.net")
@RestController
@RequestMapping("/api/healthCenter")
@RequiredArgsConstructor
public class HealthCenterController {

    private final HealthCenterService healthCenterService;

    @GetMapping("/getAll")
    public ResponseEntity<List<HealthCenterDto>> getAllHealthCenters (){
        try {
            return ResponseEntity.status(HttpStatus.FOUND).body(healthCenterService.getAllHealthCenters());
        }catch (Exception e){
            System.out.println("Exception getAllAppointments in HealthCenterController ==> " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

//    @GetMapping("/getAllAppointments")
//    public ResponseEntity<List<Appointment>> getAllAppointments (@RequestParam String email){
//        try {
//            if (email == null || !isValidEmail(email))
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//            List<Appointment> appointments = healthCenterService.getAllAppointments(email);
//            return ResponseEntity.status(HttpStatus.FOUND).body(appointments);
//        } catch (Exception e) {
//            System.out.println("Exception getAllAppointments in HealthCenterController ==> " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

//    @PutMapping("/addAppointmentBlock")
//    public ResponseEntity<AppointmentDto> addAppointmentBlock (@RequestParam String email,
//                                                                @Valid @RequestBody AppointmentDto appointmentDto){
//        try {
//            if (email == null || !isValidEmail(email))
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//            AppointmentDto addAppointmentBlock =
//                    healthCenterService.addAppointmentBlock(email, appointmentDto);
//            if (addAppointmentBlock != null)
//                return ResponseEntity.status(HttpStatus.ACCEPTED).body(addAppointmentBlock);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }catch (Exception e) {
//            System.out.println("Exception addAppointmentBlock in HealthCenterController ==> " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

//    @PutMapping("/updateAppointmentStatus")
//    public ResponseEntity<HealthCenterDto> updateAppointmentStatus (@RequestParam String HCEmail,
//                                                                    @Valid @RequestBody Appointment appointment,
//                                                                    @RequestParam Status status){
//        try {
//            if (HCEmail == null || !isValidEmail(HCEmail))
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//            HealthCenterDto healthCenterDto =
//                    healthCenterService.updateAppointmentStatus(HCEmail, appointment, status);
//            if (healthCenterDto != null)
//                return ResponseEntity.status(HttpStatus.ACCEPTED).body(healthCenterDto);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        } catch (Exception e) {
//            System.out.println("Exception updateAppointmentStatus in HealthCenterController ==> " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

//    @PutMapping("/bookAppointment")
//    public ResponseEntity<HealthCenterDto> bookAppointment (@RequestParam String HCEmail,
//                                                            @RequestParam String patientEmail,
//                                                            @Valid @RequestBody Appointment appointment){
//        try {
//            if (HCEmail == null || patientEmail== null ||
//                    !isValidEmail(HCEmail) || !isValidEmail(patientEmail))
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//            HealthCenterDto healthCenterDto =
//                    healthCenterService.bookAppointment(HCEmail, patientEmail, appointment);
//            if (healthCenterDto != null)
//                return ResponseEntity.status(HttpStatus.ACCEPTED).body(healthCenterDto);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        } catch (Exception e) {
//            System.out.println("Exception bookAppointment in HealthCenterController ==> " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

//    @PutMapping("/prescription")
//    public ResponseEntity<PatientDto> addPrescription (@RequestParam String patientEmail,
//                                                       @Valid @RequestBody Prescription prescription){
//        try {
//            if (patientEmail== null || !isValidEmail(patientEmail))
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//            PatientDto patientDto = healthCenterService.addPrescription(patientEmail, prescription);
//            if (patientDto != null)
//                return ResponseEntity.status(HttpStatus.ACCEPTED).body(patientDto);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        } catch (Exception e){
//            System.out.println("Exception addPrescription in HealthCenterController ==> " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

    @DeleteMapping("/delete")
    public ResponseEntity<HealthCenterDto> deleteHealthCenter (@RequestParam String healthCenter_id){
        try {
            HealthCenterDto healthCenterDto = healthCenterService.deleteHealthCenter(healthCenter_id);
            if (healthCenterDto != null)
                return ResponseEntity.status(HttpStatus.GONE).body(healthCenterDto);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }catch (Exception e) {
            System.out.println("Exception deleteHealthCenter in HealthCenterController " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

//    @DeleteMapping("/deleteAppointment")
//    public ResponseEntity<String> deleteAppointment (@RequestParam String HCEmail,
//                                                     @RequestBody Appointment appointment){
//        try{
//            System.out.println("Email => " + HCEmail);
//            System.out.println("Appointment => " + appointment.getDate() + " " + appointment.getTime());
//            boolean res = healthCenterService.deleteAppointment(HCEmail, appointment);
//            if (res)
//                return ResponseEntity.status(HttpStatus.OK).body("Deleted Successfully");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }catch (Exception e){
//            System.out.println("Exception deleteAppointment in HealthCenterController ==> " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

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
