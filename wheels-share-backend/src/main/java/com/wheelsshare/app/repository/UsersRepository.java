package com.wheelsshare.app.repository;

import com.wheelsshare.app.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Users entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UsersRepository extends JpaRepository<Users, String> {

    @Query(value = "SELECT U from Users U WHERE U.emailAddress = :emailAddress AND U.password = :password")
    List<Users> findUserAndLogIn(@Param("emailAddress") String emailAddress,
                                 @Param("password") String password);
}
