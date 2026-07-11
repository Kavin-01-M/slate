package com.slate.classroom.repository;

import com.slate.classroom.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findBySchoolClass_IdAndDate(Long classId, LocalDate date);

    List<Attendance> findByStudent_IdOrderByDateDesc(Long studentId);

    List<Attendance> findByStudent_IdAndDateBetween(Long studentId, LocalDate start, LocalDate end);

    Optional<Attendance> findByStudent_IdAndDate(Long studentId, LocalDate date);

    long countByStudent_IdAndStatus(Long studentId, com.slate.classroom.entity.AttendanceStatus status);

    long countByStudent_Id(Long studentId);
}
