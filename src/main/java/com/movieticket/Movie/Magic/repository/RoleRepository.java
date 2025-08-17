package com.movieticket.Movie.Magic.repository;

import com.movieticket.Movie.Magic.model.Role; // Import your Role entity
import com.movieticket.Movie.Magic.model.RoleName; // Import your RoleName enum
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {


    Optional<Role> findByName(RoleName name);


}