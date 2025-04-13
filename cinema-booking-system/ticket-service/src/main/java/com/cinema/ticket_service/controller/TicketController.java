package com.cinema.ticket_service.controller;

import com.cinema.ticket_service.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/book")
    public String bookTickets(
            @RequestParam String userId,
            @RequestParam String showId,
            @RequestParam String movieId,
            @RequestBody List<String> seatNumbers) {
        for (String seat : seatNumbers) {
            ticketService.bookSingleTicket(userId, showId, movieId, seat);
        }
        return "Booking successful!";
    }

}
