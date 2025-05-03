package model;

import java.time.LocalDateTime;

public class Reservation {
    private String id;
    private User user;
    private Room room;
    private LocalDateTime start;
    private LocalDateTime end;
    private String status; // PENDING, APPROVED, REJECTED
    private String comment;

    public Reservation(String id, User user, Room room, LocalDateTime start, LocalDateTime end) {
        this.id = id;
        this.user = user;
        this.room = room;
        this.start = start;
        this.end = end;
        this.status = "PENDING";
        this.comment = "";
    }

    public String getId() { return id; }
    public User getUser() { return user; }
    public Room getRoom() { return room; }
    public LocalDateTime getStart() { return start; }
    public LocalDateTime getEnd() { return end; }
    public String getStatus() { return status; }
    public String getComment() { return comment; }

    public void setStatus(String status) { this.status = status; }
    public void setComment(String comment) { this.comment = comment; }
}