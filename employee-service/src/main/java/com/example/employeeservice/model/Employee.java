package com.example.employeeservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_id_gen")
    @SequenceGenerator(name = "employee_id_gen", sequenceName = "employee_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255)
    @NotNull
    @Column(name = "surname", nullable = false)
    private String surname;

    @NotNull
    @Column(name = "salary", nullable = false)
    private Long salary;

    @Size(max = 255)
    @NotNull
    @Column(name = "role", nullable = false)
    private String role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @Column(name = "department_id")
    private Long departmentId;

    @NotNull
    @Column(name = "registration_time", nullable = false)
    private Instant registrationTime;

    @NotNull
    @Column(name = "update_time", nullable = false)
    private Instant updateTime;

    @Column(name = "deleted")
    private Boolean deleted;

}