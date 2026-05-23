package com.empresa.feriasapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record ColaboradorRequest(

        @NotBlank(message = "O nome do colaborador e obrigatorio.")
        String nome,

        @NotBlank(message = "O e-mail e obrigatorio.")
        @Email(message = "Formato de e-mail invalido.")
        String email,

        @NotBlank(message = "O cargo e obrigatorio.")
        String cargo,

        @NotNull(message = "A data de admissao e obrigatoria.")
        @Past(message = "A data de admissao deve ser uma data passada.")
        LocalDate dataAdmissao
) {
}