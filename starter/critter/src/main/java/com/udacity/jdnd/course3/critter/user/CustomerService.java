package com.udacity.jdnd.course3.critter.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    public Customer saveCustomer(Customer customer){
        return customerRepository.save(customer);
    }

    public Optional<Customer> getCustomerById(Long customerId){
        return customerRepository.findById(customerId);
    }

    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerByName(String name){
        return customerRepository.findByName(name);
    }

    /*
    public Optional<Customer> getCustomerByPetId(Long petId){
        return customerRepository.findByPetId(petId);
    }

     */
}
