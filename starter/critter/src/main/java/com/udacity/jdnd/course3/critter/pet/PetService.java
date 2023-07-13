package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PetService {
    @Autowired
    PetRepository petRepository;

    @Autowired
    CustomerService customerService;

    public Pet savePet(Pet pet){
        //Save can be used to generate a new pet or update an existing pet
        Pet savedPet = petRepository.save(pet);
        // Need to update the parent side of the relationship
        Customer customer = savedPet.getCustomer();
        if(customer != null){
            List<Pet> customerPets = customer.getPets();
            if(customerPets == null){
                customerPets = new ArrayList<>();
            }

            if(!customerPets.contains(savedPet)){
                customerPets.add(savedPet);
                customer.setPets(customerPets);
                Customer savedCustomer = customerService.saveCustomer(customer);
            }
        }

        return savedPet;
    }

    public Optional<Pet> getPet(Long id){
        return petRepository.findById(id);
    }

    public List<Pet> getPets(){
        return petRepository.findAll();
    }

    public List<Pet> getPetsByOwner(Customer customer){
        return petRepository.findAllByCustomer(customer);
    }
}

