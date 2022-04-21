package com.xyz.parking.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ParkVehicleRequestDto {

    @NotBlank
    private String vehicleCode;

    @NotNull
    private Integer vehicleSize;
}
