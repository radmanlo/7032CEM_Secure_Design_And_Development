package com.example.hab.controller;

import com.example.hab.dto.AppointmentDto;
import com.example.hab.dto.HealthCenterDto;
import com.example.hab.entity.Appointment;
import com.example.hab.service.AppointmentService;
import com.example.hab.service.HealthCenterService;
import com.example.hab.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointment")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @CrossOrigin(origins = "https://habsecurity.azurewebsites.net")
    @GetMapping("/hc/allAppointments")
    public ResponseEntity<List<AppointmentDto>> allAppointmentsHC (@RequestParam String hcEmail){
        try {
            List<AppointmentDto> appointmentDtos = appointmentService.getAllAppointmentHC(hcEmail);
            return ResponseEntity.status(HttpStatus.FOUND).body(appointmentDtos);
        } catch (Exception e) {
            System.out.println("Exception allAppointmentsHC in AppointmentController ==>  " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @CrossOrigin(origins = "https://habsecurity.azurewebsites.net")
    @GetMapping("/patient/allAppointments")
    public ResponseEntity<List<AppointmentDto>> allAppointmentPatient (@RequestParam String patientEmail){
        try{
            List<AppointmentDto> appointmentDtos = appointmentService.getAllAppointmentPatient(patientEmail);
            return ResponseEntity.status(HttpStatus.OK).body(appointmentDtos);
        }catch (Exception e) {
            System.out.println("Exception allAppointmentPatient in AppointmentController ==> " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @CrossOrigin(origins = "https://habsecurity.azurewebsites.net")
    @GetMapping("/getAppointmentById")
    public ResponseEntity<AppointmentDto> getAppointmentById (@RequestParam String appoId){
        try {
            AppointmentDto appointmentDto = appointmentService.getAppointmentById(appoId);
            if (appointmentDto != null)
                return ResponseEntity.status(HttpStatus.FOUND).body(appointmentDto);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            System.out.println("Exception agetAppointmentById in AppointmentController ==> " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @CrossOrigin(origins = "https://habsecurity.azurewebsites.net")
    @PostMapping("/create")
    public ResponseEntity<AppointmentDto> createAppointment ( @Valid @RequestBody AppointmentDto appointmentDto,
                                                              @RequestHeader("Authorization") String header){
        try{
            System.out.println("=>>" + appointmentDto.toString());
            AppointmentDto createdAppo = appointmentService.createAppointment(appointmentDto);
            if (createdAppo != null)
                return ResponseEntity.status(HttpStatus.CREATED).body(createdAppo);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            System.out.println("Exception createAppointment in AppointmentController ==> " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @CrossOrigin(origins = "https://habsecurity.azurewebsites.net")
    @PutMapping("/update")
    public ResponseEntity<AppointmentDto> updateAppointment (@RequestBody AppointmentDto appointmentDto){
        try {
            AppointmentDto updatedAppo = appointmentService.updateAppointment(appointmentDto);
            if (updatedAppo != null)
                return ResponseEntity.status(HttpStatus.OK).body(updatedAppo);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }catch (Exception e) {
            System.out.println("Exception updateAppointment in AppointmentController ==> " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @CrossOrigin(origins = "https://habsecurity.azurewebsites.net")
    @DeleteMapping("/delete")
    public ResponseEntity<AppointmentDto> deleteAppointment (@RequestParam String appoId){
        try {
            AppointmentDto deletedAppo = appointmentService.deleteAppointment(appoId);
            if (deletedAppo != null)
                return ResponseEntity.status(HttpStatus.OK).body(deletedAppo);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }catch (Exception e) {
            System.out.println("Exception deleteAppointment in AppointmentController ==> " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
