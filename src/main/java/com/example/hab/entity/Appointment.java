package com.example.hab.entity;

import com.mongodb.lang.NonNull;
import com.mongodb.lang.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "Appointment")
public class Appointment {

    @Id
    private String id;

    @NonNull
    private HealthCenter healthCenter;

    @Nullable
    private Patient patient;

    @NonNull
    private String date;

    @NonNull
    private String time;

    @NonNull
    private Status status = Status.EMPTY;
}

