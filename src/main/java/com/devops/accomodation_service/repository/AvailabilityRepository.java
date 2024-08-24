package com.devops.accomodation_service.repository;

import com.devops.accomodation_service.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, UUID> {
}
