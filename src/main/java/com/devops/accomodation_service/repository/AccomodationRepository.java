package com.devops.accomodation_service.repository;

import com.devops.accomodation_service.model.Accomodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccomodationRepository extends JpaRepository<Accomodation, UUID> {
    void deleteAllByUserId(UUID userId);

    List<Accomodation> findAllByUserId(UUID hostId);

    @Query("select a from Accomodation a where a.minGuestNum <= ?1 and a.maxGuestNum >= ?1 and a.id not in ?2")
    List<Accomodation> filter(long guestsNum, List<UUID> unavailableAccommodations);
}
