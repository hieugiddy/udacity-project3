package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ScheduleService {
    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    CustomerRepository customerRepository;

    public Schedule saveSchedule(Schedule schedule){
        Schedule savedSchedule = scheduleRepository.save(schedule);

        // Make sure to update the pets so that they contain the reference to the schedule
        for(Pet p : savedSchedule.getPets()){
            List<Schedule> petSchedules = p.getSchedules();
            if(petSchedules != null){
                if(!petSchedules.contains(savedSchedule)) {
                    petSchedules.add(savedSchedule);
                }
            }else{
                petSchedules = new ArrayList<>();
                petSchedules.add(savedSchedule);
            }
            p.setSchedules(petSchedules);
            petRepository.save(p);
        }

        // Make sure to update the employees so that they contain a reference to the schedule
        for(Employee e : savedSchedule.getEmployees()){
            List<Schedule> empSchedules = e.getSchedules();
            if(empSchedules != null){
                if(!empSchedules.contains(savedSchedule)){
                    empSchedules.add(savedSchedule);
                }else{
                    empSchedules = new ArrayList<>();
                    empSchedules.add(savedSchedule);
                }
            }
            e.setSchedules(empSchedules);
            employeeRepository.save(e);
        }

        return savedSchedule;
    }

    public List<Schedule> getAllSchedules(){
        return scheduleRepository.findAll();
    }

    public List<Schedule> getAllSchedulesForPet(Long petId){
        Optional<Pet> optional = petRepository.findById(petId);
        if(optional.isPresent()){
            return scheduleRepository.findAllByPetsContaining(optional.get());
        }else{
            throw new UnsupportedOperationException();
        }
    }

    public List<Schedule> getAllSchedulesForEmployee(Long employeeId){
        Optional<Employee> optional = employeeRepository.findById(employeeId);
        if(optional.isPresent()){
            return scheduleRepository.findAllByEmployeesContaining(optional.get());
        }else{
            throw new UnsupportedOperationException();
        }
    }

    public List<Schedule> getAllSchedulesForCustomer(Long customerId){
        Optional<Customer> optional = customerRepository.findById(customerId);
        if(optional.isPresent()){
            List<Pet> pets = optional.get().getPets();
            List<Schedule> schedules = new ArrayList<>();
            for(Pet p : pets){
                if(p.getSchedules() != null){
                    schedules.addAll(p.getSchedules());
                }
            }
            return schedules;
        }else{
            throw new UnsupportedOperationException();
        }

    }

}

