package com.udacity.jdnd.course3.critter.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee){
        return employeeRepository.save(employee);
    }

    public Optional<Employee> getEmployeeById(Long id){
        return employeeRepository.findById(id);
    }

    public void setEmployeeAvailability(Set<DayOfWeek> availability, Long employeeId){
        Optional<Employee> optional = employeeRepository.findById(employeeId);
        if(optional.isPresent()){
            Employee employee = optional.get();
            employee.setDaysAvailable(availability);
            employeeRepository.save(employee);
        }

    }

    public List<Employee> getAvailableEmployees(EmployeeRequestDTO employeeRequestDTO){
        List<Employee> availableEmployees = new ArrayList<>();
        // Loop through the different skills asked for
        Set<EmployeeSkill> skills = employeeRequestDTO.getSkills();
        DayOfWeek day = employeeRequestDTO.getDate().getDayOfWeek();
        // Employees need to cover all requested skills to match the employee request

        List<Employee> employeeResults = employeeRepository.findAllByDaysAvailableAndSkillsIn(day, skills);
        for(Employee e : employeeResults){
            if(e.getSkills().containsAll(skills)){
                availableEmployees.add(e);
            }
        }
        return availableEmployees;
    }
}
