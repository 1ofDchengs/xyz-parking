package com.xyz.parking.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class ParkingComputationUtil {

    private static final int MINUTES_PER_HOUR = 60;

    @Value("${parking.car.small:1}")
    private int smallVehicle;

    @Value("${parking.car.medium:2}")
    private int mediumVehicle;

    @Value("${parking.car.large:3}")
    private int largeVehicle;

    @Value("${parking.slot.small:1}")
    private int smallSlot;

    @Value("${parking.slot.medium:2}")
    private int mediumSlot;

    @Value("${parking.slot.large:3}")
    private int largeSlot;

    @Value("${parking.rate.base.fee:40}")
    private double flatRateFee;

    @Value("${parking.rate.base.minute:180}")
    private int flatRateMinutes;

    @Value("${parking.rate.chunk.fee:5000}")
    private int chunkFee;

    @Value("${parking.rate.chunk.minute:1440}")
    private int chunkMinutes;

    @Value("${parking.fee.small:20}")
    private int smallSlotRate;

    @Value("${parking.fee.medium:60}")
    private int mediumSlotRate;

    @Value("${parking.fee.large:100}")
    private int largeSlotRate;

    @Value("${parking.re-entry.minute:60}")
    private int parkingReEntryMinutes;

    public boolean isSmallVehicle(int vehicleSize) { return smallVehicle == vehicleSize; }

    public boolean isMediumVehicle(int vehicleSize) {
        return mediumVehicle == vehicleSize;
    }

    public boolean isLargeVehicle(int vehicleSize) { return largeVehicle == vehicleSize; }

    public double getParkingFee(final LocalDateTime parkEntryDateTime, final LocalDateTime parkExitDateTime, final int parkingSlotSize) {
        double parkingFee = flatRateFee;
        double raterPerHour = getParkingRate(parkingSlotSize);

        Duration duration = Duration.between(parkEntryDateTime, parkExitDateTime);
        double parkingMinutes = duration.toMinutes();

        // flat rate with exceeding hours
        if (parkingMinutes > flatRateMinutes) {
            double exceedingHours = Math.ceil((parkingMinutes - flatRateMinutes) / MINUTES_PER_HOUR) ;
            parkingFee = flatRateFee + (raterPerHour * exceedingHours);
        }

        // 24 hours (with/without exceeding hours)
        if (parkingMinutes >= chunkMinutes) {
            int chunkCounter = new BigDecimal(String.valueOf(parkingMinutes / chunkMinutes)).intValue();
            double exceedingHours = Math.ceil((parkingMinutes - (chunkMinutes * chunkCounter)) / MINUTES_PER_HOUR);
            parkingFee = (chunkCounter * chunkFee) + (raterPerHour * exceedingHours);
        }

        return parkingFee;
    }

    private double getParkingRate(final int parkingSlotSize) {
        double raterPerHour = smallSlotRate;
        if (largeSlot == parkingSlotSize) {
            raterPerHour = largeSlotRate;
        } else if (mediumSlot == parkingSlotSize) {
            raterPerHour = mediumSlotRate;
        }
        return raterPerHour;
    }

    public int getParkingReEntryMinutes() {
        return parkingReEntryMinutes;
    }

}











