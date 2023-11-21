package com.example.hab.dto;

import com.example.hab.entity.Appointment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class HealthCenterDto extends UserDto{

    @NotBlank(message = "Name cannot be blank")
    @Size(min=2, max = 20, message = "Name size must be between 2 and 20 characters")
    private String name;
//    private List<Appointment> appointments;

}
