package com.example.hab.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class HealthCenter extends User{

    private String name;

//    private List<Appointment> appointments = new ArrayList<Appointment>();
}
