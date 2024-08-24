package com.devops.accomodation_service.repository;

import com.devops.accomodation_service.model.SeasonalPricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SeasonalPricingRepository extends JpaRepository<SeasonalPricing, UUID> {
}
