package com.movieticket.Movie.Magic.repository;

import com.movieticket.Movie.Magic.model.Booking;
import com.movieticket.Movie.Magic.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByShowtimeId(Long showtimeId);
    List<Booking> findByUser(User user);
}