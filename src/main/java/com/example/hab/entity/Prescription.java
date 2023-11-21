package com.example.hab.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.TextIndexed;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prescription {

    private HealthCenter healthCenter;

    @NotBlank(message = "Date cannot be blank")
    private String date;

    @TextIndexed
    @NotBlank(message = "Illness cannot be blank")
    @Size(max = 200, message = "Illness is too long. Maximum 200 characters")
    private String illness;

    @TextIndexed
    @Size(max = 200, message = "Medicine is too long. Maximum 200 characters")
    private String medicine;
}
