package com.empresa.feriasapi.service;

import com.empresa.feriasapi.dto.SolicitacaoFeriasRequest;
import com.empresa.feriasapi.dto.SolicitacaoFeriasResponse;
import com.empresa.feriasapi.exception.RecursoNaoEncontradoException;
import com.empresa.feriasapi.exception.RegraDeNegocioException;
import com.empresa.feriasapi.model.Colaborador;
import com.empresa.feriasapi.model.SolicitacaoFerias;
import com.empresa.feriasapi.repository.ColaboradorRepository;
import com.empresa.feriasapi.repository.SolicitacaoFeriasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitacaoFeriasService {

    private static final int MINIMO_DIAS = 5;
    private static final int MAXIMO_DIAS = 30;

    private final SolicitacaoFeriasRepository solicitacaoFeriasRepository;
    private final ColaboradorRepository colaboradorRepository;

    @Transactional
    public SolicitacaoFeriasResponse criar(SolicitacaoFeriasRequest request) {
        Colaborador colaborador = colaboradorRepository.findById(request.colaboradorId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Colaborador nao encontrado com o ID: " + request.colaboradorId()
                ));

        if (!request.dataInicio().isAfter(LocalDate.now())) {
            throw new RegraDeNegocioException(
                    "A data de inicio das ferias deve ser uma data futura."
            );
        }

        if (request.quantidadeDias() < MINIMO_DIAS || request.quantidadeDias() > MAXIMO_DIAS) {
            throw new RegraDeNegocioException(
                    "A quantidade de dias de ferias deve estar entre " + MINIMO_DIAS + " e " + MAXIMO_DIAS + " dias."
            );
        }

        SolicitacaoFerias solicitacao = new SolicitacaoFerias();
        solicitacao.setColaborador(colaborador);
        solicitacao.setDataInicio(request.dataInicio());
        solicitacao.setQuantidadeDias(request.quantidadeDias());

        SolicitacaoFerias salva = solicitacaoFeriasRepository.save(solicitacao);
        return SolicitacaoFeriasResponse.from(salva);
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoFeriasResponse> listarTodas() {
        return solicitacaoFeriasRepository.findAll()
                .stream()
                .map(SolicitacaoFeriasResponse::from)
                .toList();
    }
}