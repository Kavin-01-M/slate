package com.slate.classroom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {
    private Long id;
    private String fullName;
    private String rollNumber;
    private String email;
    private LocalDate dateOfBirth;
    private Long classId;
    private String className;
}
