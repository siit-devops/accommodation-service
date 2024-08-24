package com.devops.accomodation_service;

import com.devops.accomodation_service.enumerations.DayOfTheWeek;
import com.devops.accomodation_service.model.*;
import com.devops.accomodation_service.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@AllArgsConstructor
public class AccomodationServiceApplication implements CommandLineRunner {

	private final AccomodationRepository accomodationRepository;
	private final LocationRepository locationRepository;
	private final PriceRepository priceRepository;
	private final AvailabilityRepository availabilityRepository;
	private final SeasonalPricingRepository seasonalPricingRepository;
	private final SlotRepository slotRepository;

	public static void main(String[] args) {
		SpringApplication.run(AccomodationServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Location location1 = Location.builder()
				.name("loc1")
				.fullAddress("FullAddress 1")
				.lon(0.0001)
				.lat(0.0002)
				.build();

		Slot slot1 = Slot.builder()
				.startDate(LocalDate.of(2024, 1, 1))
				.endDate(LocalDate.of(2024, 6, 30))
				.build();

		Slot slot2 = Slot.builder()
				.startDate(LocalDate.of(2024, 2, 1))
				.endDate(LocalDate.of(2024, 2, 15))
				.build();

		Slot slot3 = Slot.builder()
				.startDate(LocalDate.of(2024, 3, 2))
				.endDate(LocalDate.of(2024, 3, 30))
				.build();

		Slot slotSP = Slot.builder()
				.startDate(LocalDate.of(2024, 1, 1))
				.endDate(LocalDate.of(2024, 1, 31))
				.build();

		SeasonalPricing seasonal1 = SeasonalPricing.builder()
				.slot(slotSP)
				.seasonalPrice(200)
				.daysOfTheWeek(Set.of(DayOfTheWeek.MONDAY, DayOfTheWeek.TUESDAY))
				.description("Opis")
				.build();

		Price price1 = Price.builder()
				.basePrice(100)
				.perPerson(false)
				.seasonalPricings(Set.of(seasonal1))
				.build();

		Availability availability1 = Availability.builder()
				.price(price1)
				.slot(slot1)
				.unavailableSlots(Set.of(slot2, slot3))
				.build();

		Accomodation accommodation1 = Accomodation.builder()
				.location(location1)
				.userId(1L)
				.maxGuestNum(10)
				.minGuestNum(1)
				.availabilities(Set.of(availability1))
				.tags(Set.of("WI FI", "Parking", "Bazen"))
				.name("Soba")
				.description("Neki opis kako je super i povoljno...")
				.images(Set.of("https://res.cloudinary.com/dkdue4xbo/image/upload/v1663276292/products/rbhgqm9hyljv3wjosp2h.jpg"))
				.build();

		slotRepository.saveAll(List.of(slot1, slot2, slot3, slotSP));
		seasonalPricingRepository.save(seasonal1);
		priceRepository.save(price1);
		availabilityRepository.save(availability1);
		locationRepository.save(location1);
		accomodationRepository.save(accommodation1);
	}
}
