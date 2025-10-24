package com.example.healthcare_customercare.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "room")
public class Room {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;
    
    @Column(name = "room_capacity", nullable = false)
    private Integer roomCapacity;
    
    @Column(name = "current_capacity", nullable = false)
    private Integer currentCapacity;
    
    @Column(name = "remaining_capacity", nullable = false)
    private Integer remainingCapacity;
    
    // Constructors
    public Room() {
    }
    
    public Room(Integer roomCapacity) {
        this.roomCapacity = roomCapacity;
        this.currentCapacity = 0; // Initialize current_capacity to 0 as shown in your database
        this.remainingCapacity = roomCapacity; // remaining_capacity starts equal to room_capacity
    }
    
    // Getters and Setters
    public Long getRoomId() {
        return roomId;
    }
    
    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
    
    public Integer getRoomCapacity() {
        return roomCapacity;
    }
    
    public void setRoomCapacity(Integer roomCapacity) {
        this.roomCapacity = roomCapacity;
    }
    
    public Integer getCurrentCapacity() {
        return currentCapacity;
    }
    
    public void setCurrentCapacity(Integer currentCapacity) {
        this.currentCapacity = currentCapacity;
    }
    
    public Integer getRemainingCapacity() {
        return remainingCapacity;
    }
    
    public void setRemainingCapacity(Integer remainingCapacity) {
        this.remainingCapacity = remainingCapacity;
    }
    
    // Helper methods for capacity management
    public void incrementCurrentCapacity() {
        if (this.currentCapacity < this.roomCapacity) {
            this.currentCapacity++;
            this.remainingCapacity = this.roomCapacity - this.currentCapacity;
        }
    }
    
    public void decrementCurrentCapacity() {
        if (this.currentCapacity > 0) {
            this.currentCapacity--;
            this.remainingCapacity = this.roomCapacity - this.currentCapacity;
        }
    }
    
    public boolean isAvailable() {
        return this.currentCapacity < this.roomCapacity;
    }
    
    public double getCapacityPercentage() {
        if (this.roomCapacity == 0) return 0.0;
        return (double) this.currentCapacity / this.roomCapacity * 100.0;
    }
    
    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", roomCapacity=" + roomCapacity +
                ", currentCapacity=" + currentCapacity +
                ", remainingCapacity=" + remainingCapacity +
                '}';
    }
}
