package com.udacity.jdnd.course3.critter.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByName(String name);

    //Optional<Customer> findByPetsPetId(Long petId);

    /*
    @Query("select c.customer from Customer c where :petId in c.pets")
    Optional<Customer> findByPetId(Long petId);

     */
}
