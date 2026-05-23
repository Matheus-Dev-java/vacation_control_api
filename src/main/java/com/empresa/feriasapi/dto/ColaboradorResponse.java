package com.empresa.feriasapi.dto;

import com.empresa.feriasapi.model.Colaborador;

import java.time.LocalDate;

public record ColaboradorResponse(
        Long id,
        String nome,
        String email,
        String cargo,
        LocalDate dataAdmissao
) {
    public static ColaboradorResponse from(Colaborador colaborador) {
        return new ColaboradorResponse(
                colaborador.getId(),
                colaborador.getNome(),
                colaborador.getEmail(),
                colaborador.getCargo(),
                colaborador.getDataAdmissao()
        );
    }
}