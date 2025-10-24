package com.example.healthcare_customercare.repository;

import com.example.healthcare_customercare.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    
    /**
     * Find room by ID
     * @param roomId Room ID
     * @return Optional<Room>
     */
    Optional<Room> findById(Long roomId);
    
    /**
     * Find rooms by room capacity
     * @param roomCapacity Room capacity
     * @return List of rooms
     */
    List<Room> findByRoomCapacity(Integer roomCapacity);
    
    /**
     * Find rooms by current capacity
     * @param currentCapacity Current capacity
     * @return List of rooms
     */
    List<Room> findByCurrentCapacity(Integer currentCapacity);
    
    /**
     * Find rooms by remaining capacity
     * @param remainingCapacity Remaining capacity
     * @return List of rooms
     */
    List<Room> findByRemainingCapacity(Integer remainingCapacity);
}
