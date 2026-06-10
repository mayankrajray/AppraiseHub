package com.appraisehub.controller;

import com.appraisehub.dto.AppraisalCycleRequestDTO;
import com.appraisehub.dto.AppraisalCycleResponseDTO;
import com.appraisehub.service.AppraisalCycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appraisal-cycles")
public class AppraisalCycleController {

    @Autowired
    private AppraisalCycleService appraisalCycleService;

    @GetMapping
    public ResponseEntity<List<AppraisalCycleResponseDTO>> getAllCycles() {
        List<AppraisalCycleResponseDTO> cycles = appraisalCycleService.getAllCycles();
        return ResponseEntity.ok(cycles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppraisalCycleResponseDTO> getCycleById(@PathVariable Long id) {
        AppraisalCycleResponseDTO cycle = appraisalCycleService.getCycleById(id);
        return ResponseEntity.ok(cycle);
    }

    @PostMapping
    public ResponseEntity<AppraisalCycleResponseDTO> createCycle(@RequestBody AppraisalCycleRequestDTO requestDTO) {
        AppraisalCycleResponseDTO savedCycle = appraisalCycleService.createCycle(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCycle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppraisalCycleResponseDTO> updateCycle(@PathVariable Long id, @RequestBody AppraisalCycleRequestDTO requestDTO) {
        AppraisalCycleResponseDTO updatedCycle = appraisalCycleService.updateCycle(id, requestDTO);
        return ResponseEntity.ok(updatedCycle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCycle(@PathVariable Long id) {
        appraisalCycleService.deleteCycle(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<AppraisalCycleResponseDTO> activateCycle(@PathVariable Long id) {
        AppraisalCycleResponseDTO cycle = appraisalCycleService.activateCycle(id);
        return ResponseEntity.ok(cycle);
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<AppraisalCycleResponseDTO> closeCycle(@PathVariable Long id) {
        AppraisalCycleResponseDTO cycle = appraisalCycleService.closeCycle(id);
        return ResponseEntity.ok(cycle);
    }
}