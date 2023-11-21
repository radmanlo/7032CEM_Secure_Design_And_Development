package com.example.hab.dto;

import com.example.hab.entity.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {

    private String id;


    private HealthCenterDto healthCenterDto;

    private PatientDto patientDto;

    @NotBlank(message = "Date cannot be blank")
    @Pattern(regexp = "^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$",
            message = "Invalid date format. Use yyyy-MM-dd")
    private String date;

    @NotBlank(message = "Time cannot be blank")
    @Pattern(regexp = "^([01][0-9]|2[0-3]):[0-5][0-9]$",
            message = "Invalid time format. Use HH:MM")
    private String time;
    private Status status = Status.EMPTY;

}
