package com.movieticket.Movie.Magic.service;

import com.movieticket.Movie.Magic.dto.BookedSeatDTO;
import com.movieticket.Movie.Magic.dto.SeatDTO;
import com.movieticket.Movie.Magic.exception.BadRequestException;
import com.movieticket.Movie.Magic.exception.ResourceNotFoundException;
import com.movieticket.Movie.Magic.model.BookedSeat;
import com.movieticket.Movie.Magic.model.Seat;
import com.movieticket.Movie.Magic.model.Theater;
import com.movieticket.Movie.Magic.model.Showtime;
import com.movieticket.Movie.Magic.repository.BookedSeatRepository;
import com.movieticket.Movie.Magic.repository.SeatRepository;
import com.movieticket.Movie.Magic.repository.TheaterRepository;
import com.movieticket.Movie.Magic.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    private BookedSeatRepository bookedSeatRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    // Helper to convert Seat entity to SeatDTO
    private SeatDTO convertToSeatDTO(Seat seat) {
        if (seat == null) {
            return null;
        }
        return new SeatDTO(
                seat.getId(),
                seat.getTheater().getId(),
                seat.getTheater().getName(),
                seat.getSeatRow(),
                seat.getSeatNumber(),
                seat.getIsPremium()
        );
    }

    // Helper to convert BookedSeat entity to BookedSeatDTO
    private BookedSeatDTO convertToBookedSeatDTO(BookedSeat bookedSeat) {
        if (bookedSeat == null) {
            return null;
        }
        return new BookedSeatDTO(
                bookedSeat.getId(),
                bookedSeat.getSeat().getId(),
                bookedSeat.getSeat().getSeatRow(),
                bookedSeat.getSeat().getSeatNumber(),
                bookedSeat.getSeat().getIsPremium(),
                bookedSeat.getShowtime().getId(),
                bookedSeat.getStatus(),
                bookedSeat.getBookedAt()
        );
    }



    @Transactional
    public SeatDTO createSeat(SeatDTO seatDTO) {
        Theater theater = theaterRepository.findById(seatDTO.getTheaterId())
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with ID: " + seatDTO.getTheaterId()));


        if (seatRepository.findByTheaterIdAndSeatRowAndSeatNumber(
                seatDTO.getTheaterId(), seatDTO.getSeatRow(), seatDTO.getSeatNumber()).isPresent()) {
            throw new BadRequestException("Seat R" + seatDTO.getSeatRow() + "N" + seatDTO.getSeatNumber() +
                    " already exists in theater ID: " + seatDTO.getTheaterId());
        }

        Seat seat = new Seat();
        seat.setTheater(theater);
        seat.setSeatRow(seatDTO.getSeatRow());
        seat.setSeatNumber(seatDTO.getSeatNumber());
        seat.setIsPremium(seatDTO.getIsPremium());

        Seat savedSeat = seatRepository.save(seat);
        return convertToSeatDTO(savedSeat);
    }


    @Transactional(readOnly = true)
    public SeatDTO getSeatById(Long id) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with ID: " + id));
        return convertToSeatDTO(seat);
    }


    @Transactional(readOnly = true)
    public List<SeatDTO> getSeatsByTheaterId(Long theaterId) {
        if (!theaterRepository.existsById(theaterId)) {
            throw new ResourceNotFoundException("Theater not found with ID: " + theaterId);
        }
        return seatRepository.findByTheaterId(theaterId).stream()
                .map(this::convertToSeatDTO)
                .collect(Collectors.toList());
    }


    public SeatDTO updateSeat(Long id, SeatDTO seatDTO) {
        Seat existingSeat = seatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with ID: " + id));

        Theater theater = theaterRepository.findById(seatDTO.getTheaterId())
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with ID: " + seatDTO.getTheaterId()));

        // Check for duplicate seat with updated details (excluding itself)
        Optional<Seat> duplicateSeat = seatRepository.findByTheaterIdAndSeatRowAndSeatNumber(
                seatDTO.getTheaterId(), seatDTO.getSeatRow(), seatDTO.getSeatNumber());

        if (duplicateSeat.isPresent() && !duplicateSeat.get().getId().equals(id)) {
            throw new BadRequestException("Seat R" + seatDTO.getSeatRow() + "N" + seatDTO.getSeatNumber() +
                    " already exists in theater ID: " + seatDTO.getTheaterId());
        }

        existingSeat.setTheater(theater);
        existingSeat.setSeatRow(seatDTO.getSeatRow());
        existingSeat.setSeatNumber(seatDTO.getSeatNumber());
        existingSeat.setIsPremium(seatDTO.getIsPremium());

        Seat updatedSeat = seatRepository.save(existingSeat);
        return convertToSeatDTO(updatedSeat);
    }

    @Transactional
    public void deleteSeat(Long id) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with ID: " + id));

        /
        if (bookedSeatRepository.findBySeatIdAndShowtimeId(id, null).stream().anyMatch(bs -> !bs.getStatus().equals("CANCELLED"))) {

            if(bookedSeatRepository.findBySeatId(id).stream().anyMatch(bs -> bs.getStatus().equals("BOOKED"))) {
                throw new BadRequestException("Cannot delete seat with ID: " + id + " as it has active bookings associated with it.");
            }
        }


        seatRepository.delete(seat);
    }



    @Transactional(readOnly = true)
    public List<Map<String, Object>> getSeatStatusForShowtime(Long showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with ID: " + showtimeId));


        List<Seat> allSeatsInTheater = seatRepository.findByTheaterId(showtime.getTheater().getId());


        Set<Long> bookedSeatIds = bookedSeatRepository.findBookedSeatIdsByShowtimeIdAndStatusBooked(showtimeId);

        return allSeatsInTheater.stream().map(seat -> {
            boolean isBooked = bookedSeatIds.contains(seat.getId());
            SeatDTO seatDTO = convertToSeatDTO(seat);
            return Map.<String, Object>of(
                    "seat", seatDTO,
                    "status", isBooked ? "BOOKED" : "AVAILABLE"
            );
        }).collect(Collectors.toList());
    }
}
