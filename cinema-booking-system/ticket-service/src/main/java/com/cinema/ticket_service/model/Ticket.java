package com.cinema.ticket_service.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "tickets")
public class Ticket {
    @Id
    private String id;

    private String userId;
    private String showId; // suất chiếu
    private String movieId;   // ID của phim
    private String seatNumber;
    private boolean booked;
}
