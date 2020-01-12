package com.wheelsshare.app.repository;

import com.wheelsshare.app.domain.Rentals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Rentals entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RentalsRepository extends JpaRepository<Rentals, Long> {
    List<Rentals> findByUserEmailAddressAndOngoingOrderByIdAsc(String userEmailAddress, Boolean ongoing);

    List<Rentals> findByOngoingOrderByIdAsc(Boolean ongoing);

    List<Rentals> findByOngoingAndCarIdOrderByIdAsc(Boolean ongoing, Long carId);

    List<Rentals> findByUserEmailAddressOrderByIdAsc(String userEmailAddress);

}
