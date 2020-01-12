package com.wheelsshare.app.repository;

import com.wheelsshare.app.domain.Rentals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Rentals entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RentalsRepository extends JpaRepository<Rentals, Long> {

    @Query(value = "select r from Rentals r where r.ongoing = true")
    List<Rentals> findAllOngoingRentals();

}
