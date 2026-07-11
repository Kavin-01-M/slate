package com.slate.classroom.dto;

import com.slate.classroom.entity.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AttendanceRecordItem {

    @NotNull
    private Long studentId;

    @NotNull
    private AttendanceStatus status;

    private String remarks;
}
