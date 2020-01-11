package com.wheelsshare.app.repository;

import com.wheelsshare.app.domain.Rents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Rents entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RentsRepository extends JpaRepository<Rents, Long> {

}
