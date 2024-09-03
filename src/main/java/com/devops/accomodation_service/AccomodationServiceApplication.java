package com.devops.accomodation_service;

import com.devops.accomodation_service.enumerations.DayOfTheWeek;
import com.devops.accomodation_service.model.*;
import com.devops.accomodation_service.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@SpringBootApplication
@AllArgsConstructor
@EnableFeignClients
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
				.name("Novi Sad")
				.fullAddress("Dunavska 1, Novi Sad, Serbia")
				.lon(19.814981)
				.lat(45.180636)
				.build();

		Slot slot1 = Slot.builder()
				.startDate(LocalDate.of(2024, 1, 1))
				.endDate(LocalDate.of(2024, 12, 30))
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
				.id(UUID.fromString("d6935b63-6fa8-4680-a0e6-21beac234c9e"))
				.location(location1)
				.userId(UUID.fromString("f4862d72-0c8f-469a-8183-5156eb4f2b3e"))
				.maxGuestNum(10)
				.minGuestNum(1)
				.availabilities(Set.of(availability1))
				.tags(Set.of("WI FI", "Parking", "Bazen"))
				.name("Soba")
				.description("Neki opis kako je super i povoljno...")
				.images(Set.of(
						"https://res.cloudinary.com/dn1hzzel2/image/upload/v1725181936/booking_images/vgzayoeltwqeaydya9qo.webp",
						"https://res.cloudinary.com/dn1hzzel2/image/upload/v1725181936/booking_images/kmztn7c7pz6yojw4cr3a.webp",
						"https://res.cloudinary.com/dn1hzzel2/image/upload/v1725181936/booking_images/vxdx09566nvikbydnsct.webp",
						"https://res.cloudinary.com/dn1hzzel2/image/upload/v1725182302/booking_images/o7rloz4tgrxmss1jaahw.jpg",
						"https://res.cloudinary.com/dn1hzzel2/image/upload/v1725182303/booking_images/myiphsu4f2jn5gybz2jw.webp",
						"https://res.cloudinary.com/dn1hzzel2/image/upload/v1725182303/booking_images/axis1dqx8v6uda7qad3x.webp"
				))
				.build();

		accomodationRepository.save(accommodation1);
	}
}
