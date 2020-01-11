package com.wheelsshare.app.repository;

import com.wheelsshare.app.domain.Cars;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Cars entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CarsRepository extends JpaRepository<Cars, Long> {

}
