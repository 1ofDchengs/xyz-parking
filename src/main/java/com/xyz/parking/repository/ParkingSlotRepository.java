package com.xyz.parking.repository;

import com.xyz.parking.entity.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

    @Query(value = "SELECT *, ABS(distance_number - ?1) as distance  FROM parking_slot WHERE " +
        "occupancy IS FALSE AND slot_size = ?2 " +
        "ORDER BY distance, slot_size LIMIT 1",
        nativeQuery = true
    )
    Optional<ParkingSlot> findSlotForLargeVehicle(int startingPoint, int slotSize);

    @Query(value = "SELECT *, ABS(distance_number - ?1) as distance FROM parking_slot  WHERE " +
        "occupancy IS FALSE AND slot_size >= ?2 " +
        "ORDER BY distance, slot_size LIMIT 1",
        nativeQuery = true
    )
    Optional<ParkingSlot> findSlotForMediumVehicle(int startingPoint, int slotSize);

    @Query(value = "SELECT *, ABS(distance_number - ?1) as distance   FROM parking_slot  WHERE " +
        "occupancy IS FALSE AND distance_number " +
        "ORDER BY distance, slot_size LIMIT 1",
        nativeQuery = true
    )
    Optional<ParkingSlot> findSlotForSmallVehicle(int startingPoint);

    Optional<ParkingSlot> findParkingSlotByVehicleId(long vehicleId);
}
