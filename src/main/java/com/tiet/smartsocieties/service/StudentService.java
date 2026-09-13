package com.tiet.smartsocieties.service;

import com.tiet.smartsocieties.dto.student.InterestUpdateRequest;
import com.tiet.smartsocieties.entity.InterestTag;
import com.tiet.smartsocieties.entity.StudentInterest;
import com.tiet.smartsocieties.entity.StudentProfile;
import com.tiet.smartsocieties.repository.InterestTagRepository;
import com.tiet.smartsocieties.repository.StudentInterestRepository;
import com.tiet.smartsocieties.repository.StudentProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentProfileRepository studentProfileRepository;
    private final InterestTagRepository interestTagRepository;
    private final StudentInterestRepository studentInterestRepository;

    public StudentProfile getProfile(Long studentId) {

        return studentProfileRepository.findById(studentId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Student profile not found"));
    }

    @Transactional
    public void updateInterests(
            Long studentId,
            InterestUpdateRequest request) {

        studentInterestRepository.deleteByIdStudentId(studentId);

        List<Long> tagIds = request.tagIds();

        for (Long tagId : tagIds) {

            InterestTag tag = interestTagRepository.findById(tagId)
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Interest tag not found: " + tagId));

            StudentInterest studentInterest = new StudentInterest();

            StudentInterest.StudentInterestId id =
                    new StudentInterest.StudentInterestId();

            id.setStudentId(studentId);
            id.setTagId(tagId);

            studentInterest.setId(id);
            studentInterest.setWeight(1.0);

            studentInterestRepository.save(studentInterest);
        }
    }

    public List<StudentInterest> getInterests(Long studentId) {

        return studentInterestRepository.findByIdStudentId(studentId);
    }
}