package com.slate.classroom.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentRequest {

    @NotBlank
    private String fullName;

    @NotBlank
    private String rollNumber;

    private String email;

    private LocalDate dateOfBirth;

    @NotNull
    private Long classId;
}
