package com.example.hab.dto;

import com.example.hab.entity.Prescription;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.TextIndexed;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PatientDto extends UserDto{

    @NotBlank(message = "First name cannot be blank") // Validate that it is not blank
    @Size(max = 20, message = "First name is too long")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 20, message = "Last name is too long")
    private String lastName;

    @TextIndexed
    @Size(max = 250, message = "Diseases are too long")
    private String allergies;

    @TextIndexed
    @Size(max = 250, message = "Diseases are too long")
    private String diseases;

    private boolean consent;
    private List<Prescription> prescription = new ArrayList<Prescription>();
}
