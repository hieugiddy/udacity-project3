package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
@Transactional
public class PetController {

    @Autowired
    PetService petService;

    @Autowired
    CustomerService customerService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = petService.savePet(convertPetDTOToEntity(petDTO));
        return convertEntityToPetDTO(pet);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Optional<Pet> optional = petService.getPet(petId);
        if(optional.isPresent()){
            return convertEntityToPetDTO(optional.get());
        }else{
            throw new UnsupportedOperationException();
        }
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<Pet> pets = petService.getPets();
        List<PetDTO> petDTOs = new ArrayList<PetDTO>();
        pets.forEach((pet) -> petDTOs.add(convertEntityToPetDTO(pet)));
        return petDTOs;
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        Optional<Customer> optional = customerService.getCustomerById(ownerId);
        if(optional.isPresent()){
            List<Pet> pets = petService.getPetsByOwner(optional.get());
            List<PetDTO> petDTOs = new ArrayList<PetDTO>();
            pets.forEach((pet) -> petDTOs.add(convertEntityToPetDTO(pet)));
            return petDTOs;
        }else{
            throw new UnsupportedOperationException();
        }
    }

    public Pet convertPetDTOToEntity(PetDTO petDTO){
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        // Manual settings for owner necessary since different types are used in Entity and DTO
        // Could be the case that no owner has been set
        if(petDTO.getOwnerId() >= 0) {
            Optional<Customer> optional = customerService.getCustomerById(petDTO.getOwnerId());
            if (optional.isPresent()) {
                pet.setCustomer(optional.get());
            }
        }
        return pet;
    }

    public PetDTO convertEntityToPetDTO(Pet pet){
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        // Manual settings for owner necessary since different types are used in Entity and DTO
        // Could be the case that no owner has been set
        if(pet.getCustomer() != null){
            petDTO.setOwnerId(pet.getCustomer().getId());
        }
        return petDTO;
    }
}
