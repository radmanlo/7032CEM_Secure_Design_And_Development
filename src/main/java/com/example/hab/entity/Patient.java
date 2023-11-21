package com.example.hab.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Patient extends User{

    private String firstName;
    private String lastName;
    private String allergies;
    private String diseases;
    private boolean consent;
    private List<Prescription> prescription = new ArrayList<Prescription>();


}
