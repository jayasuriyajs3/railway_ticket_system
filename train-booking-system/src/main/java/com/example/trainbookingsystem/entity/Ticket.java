package com.example.trainbookingsystem.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String passengerName;
    private String seatNumber;
    private LocalDate journeyDate;
    private double price;
    private double finalPrice;

    @Enumerated(EnumType.STRING)
    private TicketStatus status = TicketStatus.BOOKED;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "train_id")
    private Train train;

    private LocalDateTime bookingDate = LocalDateTime.now();

    public enum TicketStatus {
        BOOKED, CANCELLED, PENDING
    }
}