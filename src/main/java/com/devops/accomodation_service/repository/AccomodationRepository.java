package com.devops.accomodation_service.repository;

import com.devops.accomodation_service.model.Accomodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccomodationRepository extends JpaRepository<Accomodation, Long> {
}
