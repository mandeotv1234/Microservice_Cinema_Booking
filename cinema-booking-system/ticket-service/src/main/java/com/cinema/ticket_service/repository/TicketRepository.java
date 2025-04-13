package com.cinema.ticket_service.repository;

import com.cinema.ticket_service.model.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TicketRepository extends MongoRepository<Ticket, String> {
    Optional<Ticket> findByShowIdAndSeatNumber(String showId, String seatNumber);
}
