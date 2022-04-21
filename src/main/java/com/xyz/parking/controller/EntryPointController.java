package com.xyz.parking.controller;

import com.xyz.parking.entity.EntryPoint;
import com.xyz.parking.repository.EntryPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/entry-point")
public class EntryPointController {

    private final EntryPointRepository entryPointRepository;

    @GetMapping("names")
    public ResponseEntity<?> findAllEntryPointNames() {
        List<String> entryPoints = entryPointRepository.findAllEntryPointNames();
        return ResponseEntity.ok(entryPoints);
    }

    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> addEntryPoint(
        @RequestBody final EntryPoint entryPoint) {

        ResponseEntity response;
        try {
            response = ResponseEntity.ok(entryPointRepository.save(entryPoint));
        } catch(Exception exception) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving Entry-point.");
        }
        return response;
    }

}
