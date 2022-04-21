package com.xyz.parking.service;

import com.xyz.parking.entity.EntryPoint;
import com.xyz.parking.entity.ParkingSlot;
import com.xyz.parking.entity.Vehicle;
import com.xyz.parking.model.ParkVehicleRequestDto;
import com.xyz.parking.model.ParkingTicket;
import com.xyz.parking.repository.ParkingSlotRepository;
import com.xyz.parking.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ParkingSlotServiceImpl implements ParkingSlotService {

    private final ParkingSlotRepository parkingSlotRepository;

    private final VehicleRepository vehicleRepository;

    private final ParkingComputationUtil parkingComputationUtil;

    @Override
    public Optional<ParkingSlot> findBySizeAndDistance(int startingPoint, int size) {
        Optional<ParkingSlot> result = Optional.empty();
        if (parkingComputationUtil.isLargeVehicle(size)) {
            result = parkingSlotRepository.findSlotForLargeVehicle(startingPoint, size);
        } else if (parkingComputationUtil.isMediumVehicle(size)) {
            result = parkingSlotRepository.findSlotForMediumVehicle(startingPoint, size);
        } else if (parkingComputationUtil.isSmallVehicle(size)) {
            result = parkingSlotRepository.findSlotForSmallVehicle(startingPoint);
        }
        return result;
    }

    @Override
    public Optional<ParkingSlot> parkVehicle(final EntryPoint entryPoint, final ParkVehicleRequestDto vehicleRequestDto) {
        Optional<ParkingSlot> parkingSlot = findBySizeAndDistance(entryPoint.getStartPoint(), vehicleRequestDto.getVehicleSize());
        Optional<Vehicle> existingVehicle = vehicleRepository
            .findFirstByVehicleCodeAndParkOutNotNullOrderByParkInDesc(vehicleRequestDto.getVehicleCode());
        if(isForParkContinuation(existingVehicle)) {
            parkingSlot.ifPresent(slot -> {
                // park re-entry
                existingVehicle.get().setParkOut(null);
                slot.setOccupancy(true);
                slot.setVehicleId(existingVehicle.get().getVehicleId());
                parkingSlotRepository.save(slot);
            });
        } else {
            parkingSlot.ifPresent(slot -> {
                // park new entry
                Vehicle vehicle = new Vehicle();
                vehicle.setVehicleSize(vehicleRequestDto.getVehicleSize());
                vehicle.setVehicleCode(vehicleRequestDto.getVehicleCode());
                vehicle.setParkIn(LocalDateTime.now());
                vehicle.setParkOut(null);
                long id = vehicleRepository.save(vehicle).getVehicleId();

                slot.setOccupancy(true);
                slot.setVehicleId(id);
                parkingSlotRepository.save(slot);
            });
        }
        return parkingSlot;
    }

    private boolean isForParkContinuation(Optional<Vehicle> vehicle) {
        boolean parkContinuation = false;
        if (vehicle.isPresent()) {
            int reEntryMaxPeriod = parkingComputationUtil.getParkingReEntryMinutes();
            parkContinuation = reEntryMaxPeriod >= Duration.between(vehicle.get().getParkOut(), LocalDateTime.now()).toMinutes();
        }
        return parkContinuation;
    }

    @Override
    public Optional<ParkingTicket> unParkVehicle(final long vehicleId) {
        ParkingTicket parkingTicket = null;
        Optional<ParkingSlot> parkingSlot = parkingSlotRepository.findParkingSlotByVehicleId(vehicleId);
        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);

        if (vehicle.isPresent() && parkingSlot.isPresent()) {
            // Vacant parking slot
            parkingSlot.get().setOccupancy(false);
            parkingSlot.get().setVehicleId(null);

            LocalDateTime parkExitDateTime = LocalDateTime.now();
            // Get Parking Fee
            double parkingFee = parkingComputationUtil.getParkingFee(
                vehicle.get().getParkIn(), parkExitDateTime, parkingSlot.get().getSlotSize());

            // update vehicle details
            parkingFee = parkingFee - vehicle.get().getTotalPaidAmount();
            vehicle.get().setParkOut(parkExitDateTime);
            vehicle.get().setTotalPaidAmount(parkingFee + vehicle.get().getTotalPaidAmount());
            vehicleRepository.save(vehicle.get());

            // Set ParkingTicket
            parkingTicket = new ParkingTicket();
            parkingTicket.setParkingFee(parkingFee);
            parkingTicket.setFrom(vehicle.get().getParkIn());
            parkingTicket.setTo(parkExitDateTime);
        }
        return Optional.ofNullable(parkingTicket);
    }

}
