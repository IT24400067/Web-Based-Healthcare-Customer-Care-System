package com.example.healthcare_customercare.service;

import com.example.healthcare_customercare.entity.Room;
import com.example.healthcare_customercare.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    
    @Autowired
    private RoomRepository roomRepository;
    
    /**
     * Get all rooms
     * @return List of all rooms
     */
    public List<Room> getAllRooms() {
        try {
            return roomRepository.findAll();
        } catch (Exception e) {
            // If database fails, return empty list
            System.err.println("Database error in getAllRooms: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }
    
    /**
     * Get all available rooms
     * @return List of available rooms
     */
    public List<Room> getAvailableRooms() {
        try {
            // Since we don't have isAvailable field anymore, return all rooms
            return roomRepository.findAll();
        } catch (Exception e) {
            System.err.println("Database error in getAvailableRooms: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }
    
    /**
     * Get room by ID
     * @param roomId Room's ID
     * @return Optional<Room>
     */
    public Optional<Room> getRoomById(Long roomId) {
        return roomRepository.findById(roomId);
    }
    
    /**
     * Add a new room
     * @param roomCapacity Room capacity
     * @return Created room
     */
    public Room addRoom(Integer roomCapacity) {
        try {
            Room room = new Room(roomCapacity);
            return roomRepository.save(room);
        } catch (Exception e) {
            System.err.println("Database error in addRoom: " + e.getMessage());
            throw new RuntimeException("Failed to add room: " + e.getMessage(), e);
        }
    }
    
    /**
     * Update room capacity
     * @param roomId Room's ID
     * @param roomCapacity New room capacity
     * @param currentCapacity New current capacity
     * @param remainingCapacity New remaining capacity
     * @return Updated room
     */
    public Room updateRoom(Long roomId, Integer roomCapacity, Integer currentCapacity, Integer remainingCapacity) {
        Optional<Room> existingRoom = roomRepository.findById(roomId);
        if (existingRoom.isEmpty()) {
            throw new IllegalArgumentException("Room with ID " + roomId + " not found");
        }
        
        Room room = existingRoom.get();
        room.setRoomCapacity(roomCapacity);
        room.setCurrentCapacity(currentCapacity);
        room.setRemainingCapacity(remainingCapacity);
        
        return roomRepository.save(room);
    }
    
    /**
     * Delete room
     * @param roomId Room's ID
     * @return true if deleted successfully
     */
    public boolean deleteRoom(Long roomId) {
        if (roomRepository.existsById(roomId)) {
            roomRepository.deleteById(roomId);
            return true;
        }
        return false;
    }
    
    /**
     * Get total room count
     * @return Total count of rooms
     */
    public long getTotalRoomCount() {
        return roomRepository.count();
    }
    
    /**
     * Increment room capacity when appointment is booked
     * @param roomId Room's ID
     * @return Updated room
     */
    public Room incrementRoomCapacity(Long roomId) {
        Optional<Room> roomOpt = roomRepository.findById(roomId);
        if (roomOpt.isEmpty()) {
            throw new IllegalArgumentException("Room with ID " + roomId + " not found");
        }
        
        Room room = roomOpt.get();
        room.incrementCurrentCapacity();
        return roomRepository.save(room);
    }
    
    /**
     * Decrement room capacity when appointment is cancelled
     * @param roomId Room's ID
     * @return Updated room
     */
    public Room decrementRoomCapacity(Long roomId) {
        Optional<Room> roomOpt = roomRepository.findById(roomId);
        if (roomOpt.isEmpty()) {
            throw new IllegalArgumentException("Room with ID " + roomId + " not found");
        }
        
        Room room = roomOpt.get();
        room.decrementCurrentCapacity();
        return roomRepository.save(room);
    }
    
    /**
     * Get room capacity status for all rooms
     * @return List of rooms with capacity information
     */
    public List<Room> getRoomCapacityStatus() {
        return roomRepository.findAll();
    }
}
