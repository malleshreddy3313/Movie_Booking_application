package com.movieticket.Movie.Magic.repository;

import com.movieticket.Movie.Magic.model.BookedSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BookedSeatRepository extends JpaRepository<BookedSeat, Long> {

    // Find all booked seats for a specific showtime
    List<BookedSeat> findByShowtimeId(Long showtimeId);

    // Find booked seats for a specific showtime and a set of seat IDs
    List<BookedSeat> findByShowtimeIdAndSeatIdIn(Long showtimeId, Set<Long> seatIds);

    // Find a specific booked seat by seat ID and showtime ID
    Optional<BookedSeat> findBySeatIdAndShowtimeId(Long seatId, Long showtimeId);

    // Find all booked seats belonging to a specific booking ID
    List<BookedSeat> findByBookingId(Long bookingId);

    List<BookedSeat> findBySeatId(Long seatId);

    // Query to get currently booked seat IDs for a given showtime (status = "BOOKED")
    @Query("SELECT bs.seat.id FROM BookedSeat bs WHERE bs.showtime.id = :showtimeId AND bs.status = 'BOOKED'")
    Set<Long> findBookedSeatIdsByShowtimeIdAndStatusBooked(@Param("showtimeId") Long showtimeId);
}
