package com.slate.classroom.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClassRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String section;

    private String academicYear;

    private Long teacherId;
}
