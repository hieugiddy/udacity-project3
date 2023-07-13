package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
@Transactional
public class UserController {

    @Autowired
    CustomerService customerService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    PetService petService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = customerService.saveCustomer(convertCustomerDTOToEntity(customerDTO));
        return convertEntityToCustomerDTO(customer);
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<Customer> customers = customerService.getAllCustomers();
        List<CustomerDTO> customerDTOs= new ArrayList<CustomerDTO>();
        customers.forEach((customer) -> customerDTOs.add(convertEntityToCustomerDTO(customer)));
        return customerDTOs;
    }


    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Optional<Pet> optional = petService.getPet(petId);
        if(optional.isPresent()){
            Pet pet = optional.get();
            Customer customer = pet.getCustomer();
            if(customer != null){
                return convertEntityToCustomerDTO(customer);
            }else{
                throw new UnsupportedOperationException();
            }
        }else{
            throw new UnsupportedOperationException();
        }
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = employeeService.saveEmployee(convertEmployeeDTOToEntity(employeeDTO));
        return convertEntityToEmployeeDTO(employee);
    }

    // This was PostMapping from the template but should be Get
    //@PostMapping("/employee/{employeeId}")
    @GetMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Optional<Employee> optional = employeeService.getEmployeeById(employeeId);
        if(optional.isPresent()){
            return convertEntityToEmployeeDTO(optional.get());
        }else{
            throw new UnsupportedOperationException();
        }
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        employeeService.setEmployeeAvailability(daysAvailable, employeeId);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> availableEmployees = employeeService.getAvailableEmployees(employeeDTO);
        List<EmployeeDTO> employeeDTOs = new ArrayList<>();
        for(Employee employee : availableEmployees){
            employeeDTOs.add(convertEntityToEmployeeDTO(employee));
        }
        return employeeDTOs;
    }

    private Customer convertCustomerDTOToEntity(CustomerDTO customerDTO){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        // Convert petIds to pets
        List<Long> petIds = customerDTO.getPetIds();

        if (petIds != null){
            List<Pet> pets = new ArrayList<Pet>();
            for(int i = 0; i < petIds.size(); i++){
                Optional<Pet> optional = petService.getPet(petIds.get(i));
                if(optional.isPresent()){
                    pets.add(optional.get());
                }
            }
            customer.setPets(pets);
        }
        return customer;
    }

    private CustomerDTO convertEntityToCustomerDTO(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        // Convert pets to petIds
        List<Pet> pets = customer.getPets();
        if (pets != null){
            List<Long> petIds = new ArrayList<>();

            for(int i = 0; i < pets.size(); i++){
                petIds.add(pets.get(i).getId());
            }
            customerDTO.setPetIds(petIds);
        }

        return customerDTO;
    }

    private Employee convertEmployeeDTOToEntity(EmployeeDTO employeeDTO){
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        return employee;
    }

    private EmployeeDTO convertEntityToEmployeeDTO(Employee employee){
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        return employeeDTO;
    }
}
