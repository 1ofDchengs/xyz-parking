package com.xyz.parking.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity(name = "parking_slot")
public class ParkingSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long parkingSlotId;

    @Column(name = "slot_size")
    private int slotSize; // 1: Small, 2: Medium, 3: Large

    @Column(name = "occupancy")
    private boolean occupancy;

    @Column(name = "distance_number")
    private int distanceNumber;

    @Column(name = "vehicle_id")
    private Long vehicleId;

}
