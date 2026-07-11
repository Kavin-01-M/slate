package com.slate.classroom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceSummaryResponse {
    private Long studentId;
    private String studentName;
    private long presentCount;
    private long totalMarkedDays;
    private double attendancePercentage;
}
