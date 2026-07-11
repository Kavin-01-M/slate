package com.slate.classroom.controller;

import com.slate.classroom.dto.AttendanceMarkRequest;
import com.slate.classroom.dto.AttendanceResponse;
import com.slate.classroom.dto.AttendanceSummaryResponse;
import com.slate.classroom.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/mark")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<List<AttendanceResponse>> mark(@Valid @RequestBody AttendanceMarkRequest request) {
        return ResponseEntity.ok(attendanceService.markAttendance(request));
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<List<AttendanceResponse>> getByClassAndDate(
            @PathVariable Long classId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(attendanceService.getByClassAndDate(classId, date));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AttendanceResponse>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(attendanceService.getByStudent(studentId));
    }

    @GetMapping("/student/{studentId}/summary")
    public ResponseEntity<AttendanceSummaryResponse> getStudentSummary(@PathVariable Long studentId) {
        return ResponseEntity.ok(attendanceService.getStudentSummary(studentId));
    }
}
