package com.udacity.jdnd.course3.critter.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findById(Long id);

    List<Employee> findAllByDaysAvailableAndSkillsIn(DayOfWeek day, Set<EmployeeSkill> skills);

    /*
    // This was my attempt using normal JpaRepository functions
    // It's not OK to pass in a set to containing! Has to be scalar
    List<Employee> findAllBySkillsContainingAndDaysAvailableContaining(Set<EmployeeSkill> skills, DayOfWeek day);
     */

    /*
    // Try with a criteria builder instead
    // I had this method in a Class that was implementing the EmployeeRepository
    // BUT! Once you implement the repository, it's not possible to use JpaRepository functions anymore
    // So then I had to implement save and findById myself, which was causing errors due to how I was managing entities
    // So after a lot of back and forth I decided to go with the answer I found in the forum here:
    // https://knowledge.udacity.com/questions/536405
    // It's waaaay easier to use the JpaRepository functions and then filter the data in the service layer!

    // These were the implementations in EmployeeRepositoryImpl

    public List<Employee> findEmployeeBySkillsAndAvailability(Set<EmployeeSkill> skills, DayOfWeek day){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<Employee> root = criteriaQuery.from(Employee.class);

        // Set up the list of predicates based on the skills needed
        Predicate[] predicates = new Predicate[skills.size()+1];
        int i = 0;
        for(EmployeeSkill skill : skills){
            predicates[i] = criteriaBuilder.isMember(skill, root.get("skills"));
            i++;
        }

        predicates[i] = criteriaBuilder.isMember(day, root.get("availability"));
        criteriaQuery.select(root).where(predicates);

        Query query = entityManager.createQuery(criteriaQuery);

        return query.getResultList();
    }

    @Override
    public Employee save(Employee employee){
        if(employee.getId() == null || employee.getId() <= 0){
            entityManager.persist(employee);
        } else {
            employee = entityManager.merge(employee);
        }
        return employee;
    }

    @Override
    public Employee findById(Long employeeId){
        return entityManager.find(Employee.class, employeeId);
    }

     */
}
