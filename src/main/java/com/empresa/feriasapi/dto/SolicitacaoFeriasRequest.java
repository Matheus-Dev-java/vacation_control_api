package com.empresa.feriasapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record SolicitacaoFeriasRequest(

        @NotNull(message = "O ID do colaborador e obrigatorio.")
        Long colaboradorId,

        @NotNull(message = "A data de inicio das ferias e obrigatoria.")
        LocalDate dataInicio,

        @NotNull(message = "A quantidade de dias e obrigatoria.")
        @Positive(message = "A quantidade de dias deve ser um numero positivo.")
        Integer quantidadeDias
) {
}