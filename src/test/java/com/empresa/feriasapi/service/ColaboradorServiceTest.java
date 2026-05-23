package com.empresa.feriasapi.service;

import com.empresa.feriasapi.dto.ColaboradorRequest;
import com.empresa.feriasapi.dto.ColaboradorResponse;
import com.empresa.feriasapi.exception.RegraDeNegocioException;
import com.empresa.feriasapi.model.Colaborador;
import com.empresa.feriasapi.repository.ColaboradorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ColaboradorServiceTest {

    @Mock
    private ColaboradorRepository colaboradorRepository;

    @InjectMocks
    private ColaboradorService colaboradorService;

    @Test
    @DisplayName("Deve cadastrar colaborador com sucesso quando e-mail ainda nao esta em uso")
    void deveCadastrarColaboradorComSucesso() {
        ColaboradorRequest request = new ColaboradorRequest(
                "Mariana Costa",
                "mariana.costa@empresa.com.br",
                "Product Manager",
                LocalDate.of(2020, 6, 10)
        );

        Colaborador colaboradorSalvo = new Colaborador();
        colaboradorSalvo.setId(1L);
        colaboradorSalvo.setNome("Mariana Costa");
        colaboradorSalvo.setEmail("mariana.costa@empresa.com.br");
        colaboradorSalvo.setCargo("Product Manager");
        colaboradorSalvo.setDataAdmissao(LocalDate.of(2020, 6, 10));

        when(colaboradorRepository.existsByEmail(anyString())).thenReturn(false);
        when(colaboradorRepository.save(any(Colaborador.class))).thenReturn(colaboradorSalvo);

        ColaboradorResponse response = colaboradorService.cadastrar(request);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.nome()).isEqualTo("Mariana Costa");
        assertThat(response.email()).isEqualTo("mariana.costa@empresa.com.br");
        assertThat(response.cargo()).isEqualTo("Product Manager");
        verify(colaboradorRepository, times(1)).save(any(Colaborador.class));
    }

    @Test
    @DisplayName("Deve lancar RegraDeNegocioException quando o e-mail ja esta cadastrado")
    void deveLancarExcecaoQuandoEmailJaExiste() {
        ColaboradorRequest request = new ColaboradorRequest(
                "Ricardo Alves",
                "mariana.costa@empresa.com.br",
                "Scrum Master",
                LocalDate.of(2023, 1, 20)
        );

        when(colaboradorRepository.existsByEmail("mariana.costa@empresa.com.br")).thenReturn(true);

        assertThatThrownBy(() -> colaboradorService.cadastrar(request))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("mariana.costa@empresa.com.br");

        verify(colaboradorRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve retornar todos os colaboradores cadastrados")
    void deveListarTodosOsColaboradores() {
        Colaborador c1 = new Colaborador();
        c1.setId(1L);
        c1.setNome("Mariana Costa");
        c1.setEmail("mariana.costa@empresa.com.br");
        c1.setCargo("Product Manager");
        c1.setDataAdmissao(LocalDate.of(2020, 6, 10));

        Colaborador c2 = new Colaborador();
        c2.setId(2L);
        c2.setNome("Rafael Souza");
        c2.setEmail("rafael.souza@empresa.com.br");
        c2.setCargo("DevOps Engineer");
        c2.setDataAdmissao(LocalDate.of(2019, 11, 3));

        when(colaboradorRepository.findAll()).thenReturn(List.of(c1, c2));

        List<ColaboradorResponse> resultado = colaboradorService.listarTodos();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).nome()).isEqualTo("Mariana Costa");
        assertThat(resultado.get(1).cargo()).isEqualTo("DevOps Engineer");
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nao ha colaboradores cadastrados")
    void deveRetornarListaVaziaQuandoNaoHaColaboradores() {
        when(colaboradorRepository.findAll()).thenReturn(List.of());

        List<ColaboradorResponse> resultado = colaboradorService.listarTodos();

        assertThat(resultado).isEmpty();
    }
}