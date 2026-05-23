package com.empresa.feriasapi.service;

import com.empresa.feriasapi.dto.SolicitacaoFeriasRequest;
import com.empresa.feriasapi.dto.SolicitacaoFeriasResponse;
import com.empresa.feriasapi.exception.RecursoNaoEncontradoException;
import com.empresa.feriasapi.exception.RegraDeNegocioException;
import com.empresa.feriasapi.model.Colaborador;
import com.empresa.feriasapi.model.SolicitacaoFerias;
import com.empresa.feriasapi.repository.ColaboradorRepository;
import com.empresa.feriasapi.repository.SolicitacaoFeriasRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitacaoFeriasServiceTest {

    @Mock
    private SolicitacaoFeriasRepository solicitacaoFeriasRepository;

    @Mock
    private ColaboradorRepository colaboradorRepository;

    @InjectMocks
    private SolicitacaoFeriasService solicitacaoFeriasService;

    private Colaborador colaboradorValido;

    @BeforeEach
    void setUp() {
        colaboradorValido = new Colaborador();
        colaboradorValido.setId(1L);
        colaboradorValido.setNome("Fernanda Lima");
        colaboradorValido.setEmail("fernanda.lima@empresa.com.br");
        colaboradorValido.setCargo("Analista de Sistemas");
        colaboradorValido.setDataAdmissao(LocalDate.of(2021, 3, 15));
    }

    @Test
    @DisplayName("Deve criar solicitacao de ferias com sucesso quando os dados sao validos")
    void deveCriarSolicitacaoComSucesso() {
        LocalDate dataInicio = LocalDate.now().plusDays(20);
        SolicitacaoFeriasRequest request = new SolicitacaoFeriasRequest(1L, dataInicio, 15);

        SolicitacaoFerias solicitacaoSalva = new SolicitacaoFerias();
        solicitacaoSalva.setId(10L);
        solicitacaoSalva.setColaborador(colaboradorValido);
        solicitacaoSalva.setDataInicio(dataInicio);
        solicitacaoSalva.setQuantidadeDias(15);
        solicitacaoSalva.setCriadoEm(LocalDateTime.now());

        when(colaboradorRepository.findById(1L)).thenReturn(Optional.of(colaboradorValido));
        when(solicitacaoFeriasRepository.save(any(SolicitacaoFerias.class))).thenReturn(solicitacaoSalva);

        SolicitacaoFeriasResponse response = solicitacaoFeriasService.criar(request);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.nomeColaborador()).isEqualTo("Fernanda Lima");
        assertThat(response.quantidadeDias()).isEqualTo(15);
        assertThat(response.dataInicio()).isEqualTo(dataInicio);
        verify(solicitacaoFeriasRepository, times(1)).save(any(SolicitacaoFerias.class));
    }

    @Test
    @DisplayName("Deve lancar RecursoNaoEncontradoException quando o colaborador nao existe")
    void deveLancarExcecaoQuandoColaboradorNaoExiste() {
        SolicitacaoFeriasRequest request = new SolicitacaoFeriasRequest(
                999L, LocalDate.now().plusDays(10), 10
        );

        when(colaboradorRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> solicitacaoFeriasService.criar(request))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("999");

        verify(solicitacaoFeriasRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lancar RegraDeNegocioException quando a data de inicio e hoje")
    void deveLancarExcecaoQuandoDataInicioEHoje() {
        SolicitacaoFeriasRequest request = new SolicitacaoFeriasRequest(
                1L, LocalDate.now(), 10
        );

        when(colaboradorRepository.findById(1L)).thenReturn(Optional.of(colaboradorValido));

        assertThatThrownBy(() -> solicitacaoFeriasService.criar(request))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("data de inicio das ferias deve ser uma data futura");

        verify(solicitacaoFeriasRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lancar RegraDeNegocioException quando a data de inicio e passada")
    void deveLancarExcecaoQuandoDataInicioEPassada() {
        SolicitacaoFeriasRequest request = new SolicitacaoFeriasRequest(
                1L, LocalDate.now().minusDays(5), 10
        );

        when(colaboradorRepository.findById(1L)).thenReturn(Optional.of(colaboradorValido));

        assertThatThrownBy(() -> solicitacaoFeriasService.criar(request))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("data de inicio das ferias deve ser uma data futura");

        verify(solicitacaoFeriasRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lancar RegraDeNegocioException quando a quantidade de dias e menor que 5")
    void deveLancarExcecaoQuandoQuantidadeDiasAbaixoDoMinimo() {
        SolicitacaoFeriasRequest request = new SolicitacaoFeriasRequest(
                1L, LocalDate.now().plusDays(15), 4
        );

        when(colaboradorRepository.findById(1L)).thenReturn(Optional.of(colaboradorValido));

        assertThatThrownBy(() -> solicitacaoFeriasService.criar(request))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("entre 5 e 30 dias");

        verify(solicitacaoFeriasRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lancar RegraDeNegocioException quando a quantidade de dias e maior que 30")
    void deveLancarExcecaoQuandoQuantidadeDiasAcimaDoMaximo() {
        SolicitacaoFeriasRequest request = new SolicitacaoFeriasRequest(
                1L, LocalDate.now().plusDays(15), 31
        );

        when(colaboradorRepository.findById(1L)).thenReturn(Optional.of(colaboradorValido));

        assertThatThrownBy(() -> solicitacaoFeriasService.criar(request))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("entre 5 e 30 dias");

        verify(solicitacaoFeriasRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve aceitar solicitacao com exatamente 5 dias, o limite minimo")
    void deveAceitarSolicitacaoComCincoDias() {
        LocalDate dataInicio = LocalDate.now().plusDays(10);
        SolicitacaoFeriasRequest request = new SolicitacaoFeriasRequest(1L, dataInicio, 5);

        SolicitacaoFerias solicitacaoSalva = new SolicitacaoFerias();
        solicitacaoSalva.setId(11L);
        solicitacaoSalva.setColaborador(colaboradorValido);
        solicitacaoSalva.setDataInicio(dataInicio);
        solicitacaoSalva.setQuantidadeDias(5);
        solicitacaoSalva.setCriadoEm(LocalDateTime.now());

        when(colaboradorRepository.findById(1L)).thenReturn(Optional.of(colaboradorValido));
        when(solicitacaoFeriasRepository.save(any(SolicitacaoFerias.class))).thenReturn(solicitacaoSalva);

        SolicitacaoFeriasResponse response = solicitacaoFeriasService.criar(request);

        assertThat(response.quantidadeDias()).isEqualTo(5);
    }

    @Test
    @DisplayName("Deve aceitar solicitacao com exatamente 30 dias, o limite maximo")
    void deveAceitarSolicitacaoComTrintaDias() {
        LocalDate dataInicio = LocalDate.now().plusDays(10);
        SolicitacaoFeriasRequest request = new SolicitacaoFeriasRequest(1L, dataInicio, 30);

        SolicitacaoFerias solicitacaoSalva = new SolicitacaoFerias();
        solicitacaoSalva.setId(12L);
        solicitacaoSalva.setColaborador(colaboradorValido);
        solicitacaoSalva.setDataInicio(dataInicio);
        solicitacaoSalva.setQuantidadeDias(30);
        solicitacaoSalva.setCriadoEm(LocalDateTime.now());

        when(colaboradorRepository.findById(1L)).thenReturn(Optional.of(colaboradorValido));
        when(solicitacaoFeriasRepository.save(any(SolicitacaoFerias.class))).thenReturn(solicitacaoSalva);

        SolicitacaoFeriasResponse response = solicitacaoFeriasService.criar(request);

        assertThat(response.quantidadeDias()).isEqualTo(30);
    }

    @Test
    @DisplayName("Deve retornar lista com todas as solicitacoes de ferias")
    void deveListarTodasAsSolicitacoes() {
        SolicitacaoFerias s1 = new SolicitacaoFerias();
        s1.setId(1L);
        s1.setColaborador(colaboradorValido);
        s1.setDataInicio(LocalDate.now().plusDays(30));
        s1.setQuantidadeDias(10);
        s1.setCriadoEm(LocalDateTime.now());

        Colaborador outroColaborador = new Colaborador();
        outroColaborador.setId(2L);
        outroColaborador.setNome("Carlos Mendes");
        outroColaborador.setEmail("carlos.mendes@empresa.com.br");
        outroColaborador.setCargo("Desenvolvedor Backend");
        outroColaborador.setDataAdmissao(LocalDate.of(2022, 8, 1));

        SolicitacaoFerias s2 = new SolicitacaoFerias();
        s2.setId(2L);
        s2.setColaborador(outroColaborador);
        s2.setDataInicio(LocalDate.now().plusDays(60));
        s2.setQuantidadeDias(20);
        s2.setCriadoEm(LocalDateTime.now());

        when(solicitacaoFeriasRepository.findAll()).thenReturn(List.of(s1, s2));

        List<SolicitacaoFeriasResponse> resultado = solicitacaoFeriasService.listarTodas();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).nomeColaborador()).isEqualTo("Fernanda Lima");
        assertThat(resultado.get(1).nomeColaborador()).isEqualTo("Carlos Mendes");
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nao ha solicitacoes cadastradas")
    void deveRetornarListaVaziaQuandoNaoHaSolicitacoes() {
        when(solicitacaoFeriasRepository.findAll()).thenReturn(List.of());

        List<SolicitacaoFeriasResponse> resultado = solicitacaoFeriasService.listarTodas();

        assertThat(resultado).isEmpty();
    }
}