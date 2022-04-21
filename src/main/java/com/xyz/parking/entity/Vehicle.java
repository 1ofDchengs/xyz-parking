package com.xyz.parking.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity(name = "vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long vehicleId;

    @Column(name = "vehicle_code")
    private String vehicleCode;

    @Column(name = "vehicle_size")
    private int vehicleSize; // 1: Small, 2: Medium, 3: Large

    @Column(name = "park_in")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime parkIn;

    @Column(name = "park_out")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime parkOut;

    @Column(name = "total_paid_amount")
    private double totalPaidAmount;
}
