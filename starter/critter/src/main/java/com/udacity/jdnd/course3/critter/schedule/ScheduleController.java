package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.*;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
@Transactional
public class ScheduleController {

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    PetService petService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule savedSchedule = scheduleService.saveSchedule(convertScheduleDTOToEntity(scheduleDTO));
        return convertEntityToScheduleDTO(savedSchedule);
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = scheduleService.getAllSchedules();
        return convertEntityToScheduleDTOList(schedules);
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> schedules =  scheduleService.getAllSchedulesForPet(petId);
        return convertEntityToScheduleDTOList(schedules);
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> schedules = scheduleService.getAllSchedulesForEmployee(employeeId);
        return convertEntityToScheduleDTOList(schedules);
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Schedule> schedules = scheduleService.getAllSchedulesForCustomer(customerId);
        return convertEntityToScheduleDTOList(schedules);
    }

    public List<ScheduleDTO> convertEntityToScheduleDTOList(List<Schedule> schedules){
        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
        if(schedules != null) {
            for (Schedule s : schedules) {
                scheduleDTOs.add(convertEntityToScheduleDTO(s));
            }
            return scheduleDTOs;
        }else{
            throw new UnsupportedOperationException();
        }
    }

    public ScheduleDTO convertEntityToScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);
        Set<Employee> employees = schedule.getEmployees();
        // Convert employees to employeeIds
        if(employees != null){
            List<Long> employeeIds = new ArrayList<>();
            for(Employee e : employees){
                employeeIds.add(e.getId());
            }
            scheduleDTO.setEmployeeIds(employeeIds);
        }

        Set<Pet> pets = schedule.getPets();
        if(pets != null){
            List<Long> petIds = new ArrayList<>();
            for(Pet p : pets){
                petIds.add(p.getId());
            }
            scheduleDTO.setPetIds(petIds);
        }
        return scheduleDTO;
    }

    public Schedule convertScheduleDTOToEntity(ScheduleDTO scheduleDTO){
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);
        // Convert list of employee ids to employees
        List<Long> employeeIds = scheduleDTO.getEmployeeIds();
        if(employeeIds != null){
            Set<Employee> employees = new HashSet<>();
            for(Long id : employeeIds){
                Optional<Employee> optional = employeeService.getEmployeeById(id);
                if(optional.isPresent()){
                    employees.add(optional.get());
                }
            }
            schedule.setEmployees(employees);
        }

        // Convert list of pet ids to pets
        List<Long> petIds = scheduleDTO.getPetIds();
        if(petIds != null){
            Set<Pet> pets = new HashSet<>();
            for(Long id : petIds){
                Optional<Pet> optional = petService.getPet(id);
                if(optional.isPresent()){
                    pets.add(optional.get());
                }
            }
            schedule.setPets(pets);
        }
        return schedule;
    }

}
