package com.empresa.feriasapi.controller;

import com.empresa.feriasapi.dto.ColaboradorRequest;
import com.empresa.feriasapi.dto.ColaboradorResponse;
import com.empresa.feriasapi.service.ColaboradorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colaboradores")
@RequiredArgsConstructor
public class ColaboradorController {

    private final ColaboradorService colaboradorService;

    @PostMapping
    public ResponseEntity<ColaboradorResponse> cadastrar(@RequestBody @Valid ColaboradorRequest request) {
        ColaboradorResponse response = colaboradorService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ColaboradorResponse>> listarTodos() {
        return ResponseEntity.ok(colaboradorService.listarTodos());
    }
}