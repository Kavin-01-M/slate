package com.slate.classroom.service;

import com.slate.classroom.dto.ClassRequest;
import com.slate.classroom.dto.ClassResponse;
import com.slate.classroom.entity.Role;
import com.slate.classroom.entity.SchoolClass;
import com.slate.classroom.entity.User;
import com.slate.classroom.exception.BadRequestException;
import com.slate.classroom.exception.ResourceNotFoundException;
import com.slate.classroom.repository.SchoolClassRepository;
import com.slate.classroom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassService {

    private final SchoolClassRepository classRepository;
    private final UserRepository userRepository;

    public ClassResponse create(ClassRequest request) {
        User teacher = null;
        if (request.getTeacherId() != null) {
            teacher = userRepository.findById(request.getTeacherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id " + request.getTeacherId()));
            if (teacher.getRole() != Role.TEACHER) {
                throw new BadRequestException("Assigned user is not a TEACHER");
            }
        }

        SchoolClass schoolClass = SchoolClass.builder()
                .name(request.getName())
                .section(request.getSection())
                .academicYear(request.getAcademicYear())
                .teacher(teacher)
                .build();

        return toResponse(classRepository.save(schoolClass));
    }

    public List<ClassResponse> getAll() {
        return classRepository.findAll().stream().map(this::toResponse).toList();
    }

    public ClassResponse getById(Long id) {
        return toResponse(findEntity(id));
    }

    public ClassResponse update(Long id, ClassRequest request) {
        SchoolClass schoolClass = findEntity(id);
        schoolClass.setName(request.getName());
        schoolClass.setSection(request.getSection());
        schoolClass.setAcademicYear(request.getAcademicYear());

        if (request.getTeacherId() != null) {
            User teacher = userRepository.findById(request.getTeacherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id " + request.getTeacherId()));
            schoolClass.setTeacher(teacher);
        }

        return toResponse(classRepository.save(schoolClass));
    }

    public void delete(Long id) {
        SchoolClass schoolClass = findEntity(id);
        classRepository.delete(schoolClass);
    }

    private SchoolClass findEntity(Long id) {
        return classRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id " + id));
    }

    private ClassResponse toResponse(SchoolClass sc) {
        return ClassResponse.builder()
                .id(sc.getId())
                .name(sc.getName())
                .section(sc.getSection())
                .academicYear(sc.getAcademicYear())
                .teacherName(sc.getTeacher() != null ? sc.getTeacher().getFullName() : null)
                .studentCount(sc.getStudents() == null ? 0 : sc.getStudents().size())
                .build();
    }
}
