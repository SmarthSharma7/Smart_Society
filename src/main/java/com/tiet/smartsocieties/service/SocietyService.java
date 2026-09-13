package com.tiet.smartsocieties.service;

import com.tiet.smartsocieties.dto.society.SocietyRequest;
import com.tiet.smartsocieties.entity.Society;
import com.tiet.smartsocieties.repository.SocietyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SocietyService {

    private final SocietyRepository societyRepository;

    public List<Society> getAllSocieties() {
        return societyRepository.findAll();
    }

    public Society getSociety(Long societyId) {

        return societyRepository.findById(societyId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Society not found"));
    }

    @Transactional
    public Society createSociety(SocietyRequest request) {

        Society society = new Society();

        society.setName(request.name());
        society.setDescription(request.description());
        society.setContactEmail(request.contactEmail());

        if (request.status() != null &&
                !request.status().isBlank()) {

            society.setStatus(
                    Society.SocietyStatus.valueOf(
                            request.status().toUpperCase()
                    )
            );

        } else {
            society.setStatus(Society.SocietyStatus.ACTIVE);
        }

        return societyRepository.save(society);
    }

    @Transactional
    public Society updateSociety(
            Long societyId,
            SocietyRequest request) {

        Society society = getSociety(societyId);

        society.setName(request.name());
        society.setDescription(request.description());
        society.setContactEmail(request.contactEmail());

        if (request.status() != null &&
                !request.status().isBlank()) {

            society.setStatus(
                    Society.SocietyStatus.valueOf(
                            request.status().toUpperCase()
                    )
            );
        }

        return societyRepository.save(society);
    }

    @Transactional
    public void deleteSociety(Long societyId) {

        Society society = getSociety(societyId);

        societyRepository.delete(society);
    }

    public List<Society> searchSocieties(String name) {

        if (name == null || name.isBlank()) {
            return societyRepository.findAll();
        }

        return societyRepository
                .findByNameContainingIgnoreCase(name);
    }
}