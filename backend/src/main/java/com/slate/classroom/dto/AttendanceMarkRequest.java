package com.slate.classroom.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AttendanceMarkRequest {

    @NotNull
    private Long classId;

    @NotNull
    private LocalDate date;

    @NotEmpty
    private List<AttendanceRecordItem> records;
}
