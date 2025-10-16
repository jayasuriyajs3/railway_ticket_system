package com.example.trainbookingsystem.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.trainbookingsystem.entity.Ticket;
import com.example.trainbookingsystem.entity.Train;
import com.example.trainbookingsystem.entity.User;
import com.example.trainbookingsystem.repository.TicketRepository;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TrainService trainService;

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    private String generateSeatNumber(Long trainId, LocalDateTime journeyDate) {
        // Get all tickets for this train and date
        List<Ticket> existingTickets = ticketRepository.findAll().stream()
                .filter(t -> t.getTrain().getId().equals(trainId))
                .filter(t -> t.getJourneyDate() != null && t.getJourneyDate().equals(journeyDate.toLocalDate()))
                .toList();

        // Start with seat A1, then A2, A3... B1, B2... etc.
        int seatCount = existingTickets.size();
        char row = (char) ('A' + (seatCount / 10)); // After 10 seats, move to next row
        int number = (seatCount % 10) + 1;

        return String.format("%c%d", row, number);
    }

    public Ticket createTicket(Ticket ticket) {
        if (ticket.getTrain() != null) {
            Train train = trainService.getTrainById(ticket.getTrain().getId()).orElseThrow();
            ticket.setTrain(train);

            // Generate unique seat number
            String seatNumber = generateSeatNumber(train.getId(),
                    ticket.getJourneyDate() != null
                            ? LocalDateTime.of(ticket.getJourneyDate(), LocalDateTime.now().toLocalTime())
                            : LocalDateTime.now());
            ticket.setSeatNumber(seatNumber);
        }

        if (ticket.getBookingDate() == null) {
            ticket.setBookingDate(LocalDateTime.now());
        }

        return ticketRepository.save(ticket);
    }

    // Overload used by tests: create ticket by userId and trainId
    public Ticket createTicket(Long userId, Long trainId) {
        User user = userService.getUserById(userId).orElseThrow();
        Train train = trainService.getTrainById(trainId).orElseThrow();

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setTrain(train);
        ticket.setBookingDate(LocalDateTime.now());

        // Calculate final price using train's base price and discount
        double finalPrice = calculateTicketPrice(train.getBasePrice(), train.getDiscountPercentage());
        ticket.setFinalPrice(finalPrice);

        // Generate seat number
        String seatNumber = generateSeatNumber(train.getId(), LocalDateTime.now());
        ticket.setSeatNumber(seatNumber);

        return ticketRepository.save(ticket);
    }

    public Ticket updateTicket(Long id, Ticket ticketDetails) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow();
        if (ticketDetails.getUser() != null) {
            ticket.setUser(ticketDetails.getUser());
        }
        if (ticketDetails.getTrain() != null) {
            ticket.setTrain(ticketDetails.getTrain());
            ticket.setFinalPrice(calculateTicketPrice(ticketDetails.getTrain().getBasePrice(),
                    ticketDetails.getTrain().getDiscountPercentage()));
        }
        ticket.setBookingDate(LocalDateTime.now()); // Update booking date
        return ticketRepository.save(ticket);
    }

    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    public double calculateTicketPrice(double basePrice, double discountPercentage) {
        return basePrice - (basePrice * discountPercentage / 100);
    }
}