package com.xyz.parking.service;

import com.xyz.parking.entity.EntryPoint;
import com.xyz.parking.entity.ParkingSlot;
import com.xyz.parking.model.ParkVehicleRequestDto;
import com.xyz.parking.model.ParkingTicket;

import java.util.Optional;

public interface ParkingSlotService {

    Optional<ParkingSlot> findBySizeAndDistance(int startingPoint, int size);

    Optional<ParkingSlot> parkVehicle(EntryPoint entryPoint, ParkVehicleRequestDto parkVehicleRequestDto);

    Optional<ParkingTicket> unParkVehicle(long vehicleId);

}
