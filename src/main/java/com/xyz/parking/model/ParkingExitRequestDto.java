package com.xyz.parking.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ParkingExitRequestDto {

    @NotNull
    private Integer vehicleId;
}
