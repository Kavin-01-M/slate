package com.slate.classroom.service;

import com.slate.classroom.dto.StudentRequest;
import com.slate.classroom.dto.StudentResponse;
import com.slate.classroom.entity.SchoolClass;
import com.slate.classroom.entity.Student;
import com.slate.classroom.exception.BadRequestException;
import com.slate.classroom.exception.ResourceNotFoundException;
import com.slate.classroom.repository.SchoolClassRepository;
import com.slate.classroom.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final SchoolClassRepository classRepository;

    public StudentResponse create(StudentRequest request) {
        if (studentRepository.existsByRollNumber(request.getRollNumber())) {
            throw new BadRequestException("Roll number already exists: " + request.getRollNumber());
        }

        SchoolClass schoolClass = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id " + request.getClassId()));

        Student student = Student.builder()
                .fullName(request.getFullName())
                .rollNumber(request.getRollNumber())
                .email(request.getEmail())
                .dateOfBirth(request.getDateOfBirth())
                .schoolClass(schoolClass)
                .build();

        return toResponse(studentRepository.save(student));
    }

    public List<StudentResponse> getAll() {
        return studentRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<StudentResponse> getByClass(Long classId) {
        return studentRepository.findBySchoolClass_Id(classId).stream().map(this::toResponse).toList();
    }

    public StudentResponse getById(Long id) {
        return toResponse(findEntity(id));
    }

    public StudentResponse update(Long id, StudentRequest request) {
        Student student = findEntity(id);

        if (!student.getRollNumber().equals(request.getRollNumber())
                && studentRepository.existsByRollNumber(request.getRollNumber())) {
            throw new BadRequestException("Roll number already exists: " + request.getRollNumber());
        }

        SchoolClass schoolClass = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id " + request.getClassId()));

        student.setFullName(request.getFullName());
        student.setRollNumber(request.getRollNumber());
        student.setEmail(request.getEmail());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setSchoolClass(schoolClass);

        return toResponse(studentRepository.save(student));
    }

    public void delete(Long id) {
        studentRepository.delete(findEntity(id));
    }

    private Student findEntity(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
    }

    private StudentResponse toResponse(Student s) {
        return StudentResponse.builder()
                .id(s.getId())
                .fullName(s.getFullName())
                .rollNumber(s.getRollNumber())
                .email(s.getEmail())
                .dateOfBirth(s.getDateOfBirth())
                .classId(s.getSchoolClass() != null ? s.getSchoolClass().getId() : null)
                .className(s.getSchoolClass() != null ? s.getSchoolClass().getName() + "-" + s.getSchoolClass().getSection() : null)
                .build();
    }
}
