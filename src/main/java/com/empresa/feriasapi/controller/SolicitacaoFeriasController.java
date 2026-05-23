package com.empresa.feriasapi.controller;

import com.empresa.feriasapi.dto.SolicitacaoFeriasRequest;
import com.empresa.feriasapi.dto.SolicitacaoFeriasResponse;
import com.empresa.feriasapi.service.SolicitacaoFeriasService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitacoes-ferias")
@RequiredArgsConstructor
public class SolicitacaoFeriasController {

    private final SolicitacaoFeriasService solicitacaoFeriasService;

    @PostMapping
    public ResponseEntity<SolicitacaoFeriasResponse> criar(@RequestBody @Valid SolicitacaoFeriasRequest request) {
        SolicitacaoFeriasResponse response = solicitacaoFeriasService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SolicitacaoFeriasResponse>> listarTodas() {
        return ResponseEntity.ok(solicitacaoFeriasService.listarTodas());
    }
}