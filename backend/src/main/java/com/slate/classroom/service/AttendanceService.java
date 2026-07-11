package com.slate.classroom.service;

import com.slate.classroom.dto.*;
import com.slate.classroom.entity.Attendance;
import com.slate.classroom.entity.AttendanceStatus;
import com.slate.classroom.entity.SchoolClass;
import com.slate.classroom.entity.Student;
import com.slate.classroom.entity.User;
import com.slate.classroom.exception.ResourceNotFoundException;
import com.slate.classroom.repository.AttendanceRepository;
import com.slate.classroom.repository.SchoolClassRepository;
import com.slate.classroom.repository.StudentRepository;
import com.slate.classroom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final SchoolClassRepository classRepository;
    private final UserRepository userRepository;

    public List<AttendanceResponse> markAttendance(AttendanceMarkRequest request) {
        SchoolClass schoolClass = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id " + request.getClassId()));

        User markedBy = currentUser().orElse(null);

        return request.getRecords().stream().map(item -> {
            Student student = studentRepository.findById(item.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + item.getStudentId()));

            Attendance attendance = attendanceRepository
                    .findByStudent_IdAndDate(student.getId(), request.getDate())
                    .orElse(Attendance.builder()
                            .student(student)
                            .schoolClass(schoolClass)
                            .date(request.getDate())
                            .build());

            attendance.setStatus(item.getStatus());
            attendance.setRemarks(item.getRemarks());
            attendance.setMarkedBy(markedBy);

            return toResponse(attendanceRepository.save(attendance));
        }).toList();
    }

    public List<AttendanceResponse> getByClassAndDate(Long classId, LocalDate date) {
        return attendanceRepository.findBySchoolClass_IdAndDate(classId, date)
                .stream().map(this::toResponse).toList();
    }

    public List<AttendanceResponse> getByStudent(Long studentId) {
        return attendanceRepository.findByStudent_IdOrderByDateDesc(studentId)
                .stream().map(this::toResponse).toList();
    }

    public AttendanceSummaryResponse getStudentSummary(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + studentId));

        long total = attendanceRepository.countByStudent_Id(studentId);
        long present = attendanceRepository.countByStudent_IdAndStatus(studentId, AttendanceStatus.PRESENT);
        double pct = total == 0 ? 0.0 : (present * 100.0) / total;

        return AttendanceSummaryResponse.builder()
                .studentId(studentId)
                .studentName(student.getFullName())
                .presentCount(present)
                .totalMarkedDays(total)
                .attendancePercentage(Math.round(pct * 100.0) / 100.0)
                .build();
    }

    private java.util.Optional<User> currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return java.util.Optional.empty();
        return userRepository.findByEmail(auth.getName());
    }

    private AttendanceResponse toResponse(Attendance a) {
        return AttendanceResponse.builder()
                .id(a.getId())
                .studentId(a.getStudent().getId())
                .studentName(a.getStudent().getFullName())
                .classId(a.getSchoolClass().getId())
                .date(a.getDate())
                .status(a.getStatus())
                .remarks(a.getRemarks())
                .build();
    }
}
