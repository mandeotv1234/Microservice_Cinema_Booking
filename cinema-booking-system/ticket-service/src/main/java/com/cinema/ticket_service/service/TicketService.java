package com.cinema.ticket_service.service;

import com.cinema.ticket_service.model.Ticket;
import com.cinema.ticket_service.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    // Một người đặt nhiều vé
    public void bookMultipleTickets(String userId, String showId,String movieId, List<String> seatNumbers) {
        for (String seat : seatNumbers) {
            bookSingleTicket(userId, showId,movieId, seat);
        }
    }

    // Nhiều người đặt cùng 1 chỗ (handle concurrency)
    public synchronized void bookSingleTicket(String userId, String showId,String movieId, String seatNumber) {
        ticketRepository.findByShowIdAndSeatNumber(showId, seatNumber).ifPresentOrElse(ticket -> {
            if (ticket.isBooked()) {
                throw new IllegalStateException("Seat already booked!");
            } else {
                ticket.setUserId(userId);
                ticket.setBooked(true);
                ticketRepository.save(ticket);
            }
        }, () -> {
            Ticket ticket = new Ticket();
            ticket.setUserId(userId);
            ticket.setShowId(showId);
            ticket.setMovieId(movieId); // ✅ thêm movieId
            ticket.setSeatNumber(seatNumber);
            ticket.setBooked(true);
            try {
                ticketRepository.save(ticket);
            } catch (DuplicateKeyException e) {
                throw new IllegalStateException("Seat already booked!");
            }
        });
    }
}
