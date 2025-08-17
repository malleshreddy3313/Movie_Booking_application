package com.movieticket.Movie.Magic.service;

import com.movieticket.Movie.Magic.dto.TheaterDTO;
import com.movieticket.Movie.Magic.model.Theater;
import com.movieticket.Movie.Magic.repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TheaterService {

    @Autowired
    private TheaterRepository theaterRepository;


    private Theater convertToEntity(TheaterDTO theaterDTO) {
        Theater theater = new Theater();
        if (theaterDTO.getId() != null) {
            theater.setId(theaterDTO.getId());
        }
        theater.setName(theaterDTO.getName());
        theater.setLocation(theaterDTO.getLocation());
        theater.setTotalSeats(theaterDTO.getTotalSeats());
        return theater;
    }


    private TheaterDTO convertToDto(Theater theater) {
        return new TheaterDTO(
                theater.getId(),
                theater.getName(),
                theater.getLocation(),
                theater.getTotalSeats()
        );
    }

    // --- CRUD Operations ---

    // Create a new theater
    public TheaterDTO createTheater(TheaterDTO theaterDTO) {
        // Business logic: Check if a theater with the same name already exists
        if (theaterRepository.findByName(theaterDTO.getName()).isPresent()) {
            throw new RuntimeException("Theater with name '" + theaterDTO.getName() + "' already exists.");
        }
        Theater theater = convertToEntity(theaterDTO);
        Theater savedTheater = theaterRepository.save(theater);
        return convertToDto(savedTheater);
    }

    // Get a theater by ID
    public Optional<TheaterDTO> getTheaterById(Long id) {
        return theaterRepository.findById(id).map(this::convertToDto);
    }

    // Get all theaters
    public List<TheaterDTO> getAllTheaters() {
        return theaterRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Update an existing theater
    public TheaterDTO updateTheater(Long id, TheaterDTO theaterDTO) {
        return theaterRepository.findById(id)
                .map(existingTheater -> {
                    // Check if the new name conflicts with another theater's name (excluding itself)
                    Optional<Theater> theaterWithSameName = theaterRepository.findByName(theaterDTO.getName());
                    if (theaterWithSameName.isPresent() && !theaterWithSameName.get().getId().equals(id)) {
                        throw new RuntimeException("Another theater with name '" + theaterDTO.getName() + "' already exists.");
                    }

                    // Update fields
                    existingTheater.setName(theaterDTO.getName());
                    existingTheater.setLocation(theaterDTO.getLocation());
                    existingTheater.setTotalSeats(theaterDTO.getTotalSeats());

                    Theater updatedTheater = theaterRepository.save(existingTheater);
                    return convertToDto(updatedTheater);
                }).orElseThrow(() -> new RuntimeException("Theater not found with ID: " + id));
    }

    // Delete a theater by ID
    public void deleteTheater(Long id) {
        if (!theaterRepository.existsById(id)) {
            throw new RuntimeException("Theater not found with ID: " + id);
        }
        theaterRepository.deleteById(id);
    }
}
