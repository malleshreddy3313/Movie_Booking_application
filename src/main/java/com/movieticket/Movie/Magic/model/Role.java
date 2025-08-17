package com.movieticket.Movie.Magic.model;

import jakarta.persistence.*;


@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 60)
    private RoleName name;


    public Role() {
    }

    // All-argument constructor

    public Role(Long id, RoleName name) {
        this.id = id;
        this.name = name;
    }

    // Constructor typically used when creating a new Role with just its name
    public Role(RoleName name) {
        this.name = name;
    }

    // GETTERS AND SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }
}