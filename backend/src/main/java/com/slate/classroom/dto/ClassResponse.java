package com.slate.classroom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassResponse {
    private Long id;
    private String name;
    private String section;
    private String academicYear;
    private String teacherName;
    private int studentCount;
}
