package com.empresa.feriasapi.service;

import com.empresa.feriasapi.dto.ColaboradorRequest;
import com.empresa.feriasapi.dto.ColaboradorResponse;
import com.empresa.feriasapi.exception.RegraDeNegocioException;
import com.empresa.feriasapi.model.Colaborador;
import com.empresa.feriasapi.repository.ColaboradorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColaboradorService {

    private final ColaboradorRepository colaboradorRepository;

    public ColaboradorResponse cadastrar(ColaboradorRequest request) {
        if (colaboradorRepository.existsByEmail(request.email())) {
            throw new RegraDeNegocioException(
                    "Ja existe um colaborador cadastrado com o e-mail: " + request.email()
            );
        }

        Colaborador colaborador = new Colaborador();
        colaborador.setNome(request.nome());
        colaborador.setEmail(request.email());
        colaborador.setCargo(request.cargo());
        colaborador.setDataAdmissao(request.dataAdmissao());

        Colaborador salvo = colaboradorRepository.save(colaborador);
        return ColaboradorResponse.from(salvo);
    }

    public List<ColaboradorResponse> listarTodos() {
        return colaboradorRepository.findAll()
                .stream()
                .map(ColaboradorResponse::from)
                .toList();
    }
}