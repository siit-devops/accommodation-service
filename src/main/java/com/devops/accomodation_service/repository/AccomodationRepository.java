package com.devops.accomodation_service.repository;

import com.devops.accomodation_service.model.Accomodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface AccomodationRepository extends JpaRepository<Accomodation, UUID> {
    void deleteAllByUserId(UUID userId);
}
