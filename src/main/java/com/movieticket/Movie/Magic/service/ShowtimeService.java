package com.movieticket.Movie.Magic.service;

import com.movieticket.Movie.Magic.dto.ShowtimeDTO;
import com.movieticket.Movie.Magic.exception.BadRequestException;
import com.movieticket.Movie.Magic.exception.ResourceNotFoundException;
import com.movieticket.Movie.Magic.model.Movie;
import com.movieticket.Movie.Magic.model.Showtime;
import com.movieticket.Movie.Magic.model.Theater;
import com.movieticket.Movie.Magic.repository.MovieRepository;
import com.movieticket.Movie.Magic.repository.ShowtimeRepository;
import com.movieticket.Movie.Magic.repository.TheaterRepository;
import com.movieticket.Movie.Magic.repository.BookedSeatRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowtimeService {

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    private BookedSeatRepository bookedSeatRepository;


    private ShowtimeDTO convertToShowtimeDTO(Showtime showtime) {
        ShowtimeDTO dto = new ShowtimeDTO();
        dto.setId(showtime.getId());
        dto.setMovieId(showtime.getMovie() != null ? showtime.getMovie().getId() : null);
        dto.setTheaterId(showtime.getTheater() != null ? showtime.getTheater().getId() : null);
        dto.setShowtimeDate(showtime.getShowtimeDate());
        dto.setShowtimeTime(showtime.getShowtimeTime());
        dto.setAvailableSeats(showtime.getAvailableSeats());
        return dto;
    }


    @Transactional
    public ShowtimeDTO createShowtime(ShowtimeDTO showtimeDTO) {
        Movie movie = movieRepository.findById(showtimeDTO.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with ID: " + showtimeDTO.getMovieId()));

        Theater theater = theaterRepository.findById(showtimeDTO.getTheaterId())
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with ID: " + showtimeDTO.getTheaterId()));

        Showtime showtime = new Showtime();
        showtime.setMovie(movie);
        showtime.setTheater(theater);
        showtime.setShowtimeDate(showtimeDTO.getShowtimeDate());
        showtime.setShowtimeTime(showtimeDTO.getShowtimeTime());
        showtime.setAvailableSeats(showtimeDTO.getAvailableSeats()); // Set initial available seats

        Showtime savedShowtime = showtimeRepository.save(showtime);
        return convertToShowtimeDTO(savedShowtime);
    }


    @Transactional(readOnly = true)
    public ShowtimeDTO getShowtimeById(Long id) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with ID: " + id));
        return convertToShowtimeDTO(showtime);
    }


    @Transactional(readOnly = true)
    public List<ShowtimeDTO> getAllShowtimes() {
        return showtimeRepository.findAll().stream()
                .map(this::convertToShowtimeDTO)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<ShowtimeDTO> searchShowtimes(String movieTitle, String theaterName, LocalDate date) {
        List<Showtime> showtimes;
        if (movieTitle != null && !movieTitle.isEmpty()) {
            showtimes = showtimeRepository.findByMovieTitleContainingIgnoreCase(movieTitle);
        } else if (theaterName != null && !theaterName.isEmpty()) {
            showtimes = showtimeRepository.findByTheaterNameContainingIgnoreCase(theaterName);
        } else if (date != null) {
            showtimes = showtimeRepository.findByShowtimeDate(date);
        } else {
            showtimes = showtimeRepository.findAll();
        }
        return showtimes.stream()
                .map(this::convertToShowtimeDTO)
                .collect(Collectors.toList());
    }


    @Transactional
    public ShowtimeDTO updateShowtime(Long id, ShowtimeDTO showtimeDTO) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with ID: " + id));

        if (showtimeDTO.getMovieId() != null) {
            Movie movie = movieRepository.findById(showtimeDTO.getMovieId())
                    .orElseThrow(() -> new ResourceNotFoundException("Movie not found with ID: " + showtimeDTO.getMovieId()));
            showtime.setMovie(movie);
        }

        if (showtimeDTO.getTheaterId() != null) {
            Theater theater = theaterRepository.findById(showtimeDTO.getTheaterId())
                    .orElseThrow(() -> new ResourceNotFoundException("Theater not found with ID: " + showtimeDTO.getTheaterId()));
            showtime.setTheater(theater);
        }

        if (showtimeDTO.getShowtimeDate() != null) {
            showtime.setShowtimeDate(showtimeDTO.getShowtimeDate());
        }
        if (showtimeDTO.getShowtimeTime() != null) {
            showtime.setShowtimeTime(showtimeDTO.getShowtimeTime());
        }
        if (showtimeDTO.getAvailableSeats() != null) {
            showtime.setAvailableSeats(showtimeDTO.getAvailableSeats());
        }


        Showtime updatedShowtime = showtimeRepository.save(showtime);
        return convertToShowtimeDTO(updatedShowtime);
    }


    @Transactional
    public void deleteShowtime(Long id) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with ID: " + id));


        if (bookedSeatRepository.findBookedSeatIdsByShowtimeIdAndStatusBooked(id).size() > 0) {
            throw new BadRequestException("Cannot delete showtime with ID: " + id + " as it has active bookings.");
        }

        showtimeRepository.delete(showtime);
    }
}