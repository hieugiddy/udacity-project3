package com.udacity.jdnd.course3.critter.pet;


import com.udacity.jdnd.course3.critter.schedule.Schedule;
import com.udacity.jdnd.course3.critter.user.Customer;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private PetType type;

    @Nationalized
    private String name;

    @ManyToOne(targetEntity = Customer.class)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private LocalDate birthDate;

    @ManyToMany(targetEntity = Schedule.class)
    private List<Schedule> schedules;

    @Column(length = 500)
    private String notes;

    public Pet(){}

}
