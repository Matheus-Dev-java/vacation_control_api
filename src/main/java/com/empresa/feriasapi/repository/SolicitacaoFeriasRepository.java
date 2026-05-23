package com.empresa.feriasapi.repository;

import com.empresa.feriasapi.model.SolicitacaoFerias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitacaoFeriasRepository extends JpaRepository<SolicitacaoFerias, Long> {

    List<SolicitacaoFerias> findAllByColaboradorId(Long colaboradorId);
}