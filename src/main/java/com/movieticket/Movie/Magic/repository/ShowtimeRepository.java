package com.movieticket.Movie.Magic.repository;

import com.movieticket.Movie.Magic.model.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {


    List<Showtime> findByMovieTitleContainingIgnoreCase(String movieTitle);


    List<Showtime> findByTheaterNameContainingIgnoreCase(String theaterName);


    List<Showtime> findByShowtimeDate(LocalDate showtimeDate);

}