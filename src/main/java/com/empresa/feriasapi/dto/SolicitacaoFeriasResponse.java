package com.empresa.feriasapi.dto;

import com.empresa.feriasapi.model.SolicitacaoFerias;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SolicitacaoFeriasResponse(
        Long id,
        Long colaboradorId,
        String nomeColaborador,
        LocalDate dataInicio,
        LocalDate dataTermino,
        Integer quantidadeDias,
        LocalDateTime criadoEm
) {
    public static SolicitacaoFeriasResponse from(SolicitacaoFerias solicitacao) {
        return new SolicitacaoFeriasResponse(
                solicitacao.getId(),
                solicitacao.getColaborador().getId(),
                solicitacao.getColaborador().getNome(),
                solicitacao.getDataInicio(),
                solicitacao.getDataInicio().plusDays(solicitacao.getQuantidadeDias() - 1),
                solicitacao.getQuantidadeDias(),
                solicitacao.getCriadoEm()
        );
    }
}