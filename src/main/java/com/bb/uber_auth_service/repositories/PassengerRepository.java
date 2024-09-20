package com.bb.uber_auth_service.repositories;



import com.bb.uber_entity_service.models.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    Optional<Passenger> findPassengerByEmail(String email);

}
