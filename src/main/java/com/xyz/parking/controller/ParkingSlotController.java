package com.xyz.parking.controller;

import com.xyz.parking.entity.EntryPoint;
import com.xyz.parking.entity.ParkingSlot;
import com.xyz.parking.model.ParkVehicleRequestDto;
import com.xyz.parking.model.ParkingExitRequestDto;
import com.xyz.parking.model.ParkingTicket;
import com.xyz.parking.repository.EntryPointRepository;
import com.xyz.parking.service.ParkingSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/parking")
public class ParkingSlotController {

    private final ParkingSlotService parkingSlotService;

    private final EntryPointRepository entryPointRepository;

    @PostMapping(
        path = "/entry",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> parkingEntry(
        @RequestHeader(name = "entry-point") final String entryPointName,
        @Valid @RequestBody final ParkVehicleRequestDto parkVehicleRequestDto) {

        // validate entry-point
        Optional<EntryPoint> entryPoint = entryPointRepository.findFirstByName(entryPointName);
        if(entryPoint.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Entry Point not Found!");
        }

        Optional<ParkingSlot> slot = parkingSlotService.parkVehicle(entryPoint.get(), parkVehicleRequestDto);

        return slot.isPresent()
            ? ResponseEntity.ok(slot)
            : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to retrieve parking slot.");
    }

    @PutMapping(
        path = "/exit",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> parkingExit(
        @Valid @RequestBody final ParkingExitRequestDto parkingExitRequestDto) {

        Optional<ParkingTicket> parkingTicket = parkingSlotService.unParkVehicle(parkingExitRequestDto.getVehicleId());

        return parkingTicket.isPresent()
            ? ResponseEntity.ok(parkingTicket.get())
            : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to process parking fee.");
    }

}
