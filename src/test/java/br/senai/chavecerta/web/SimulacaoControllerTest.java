package br.senai.chavecerta.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/** Testa a API POST /api/simular (sobe só a camada web do Spring). */
@WebMvcTest(SimulacaoController.class)
class SimulacaoControllerTest {

    @Autowired
    private MockMvc mvc;

    private static String json(String custoFixo) {
        return """
                {
                  "custoFixo": "%s", "custoVariavel": "10", "taxaPercentual": "10",
                  "beneficio": "teste",
                  "propostas": [
                    {"nome": "A", "preco": "40", "clientes": "130"},
                    {"nome": "B", "preco": "50", "clientes": "100"},
                    {"nome": "C", "preco": "70", "clientes": "80"}
                  ]
                }
                """.formatted(custoFixo);
    }

    @Test
    @DisplayName("Exemplo carregado: 200 com resultados A = 380, B = 500, C = 1.240 (RF06)")
    void exemploRetornaResultados() throws Exception {
        mvc.perform(post("/api/simular").contentType(MediaType.APPLICATION_JSON).content(json("3000")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.erros").isEmpty())
                .andExpect(jsonPath("$.resultados.length()").value(3))
                .andExpect(jsonPath("$.resultados[0].resultado").value(380.0))
                .andExpect(jsonPath("$.resultados[1].resultado").value(500.0))
                .andExpect(jsonPath("$.resultados[1].equilibrio").value(86))
                .andExpect(jsonPath("$.resultados[2].resultado").value(1240.0))
                .andExpect(jsonPath("$.grafico.eixoXMaximo").value(260))
                .andExpect(jsonPath("$.grafico.series.length()").value(3))
                .andExpect(jsonPath("$.grafico.series[0].pontos[0].resultado").value(-3000.0));
    }

    @Test
    @DisplayName("T3 via API: custo fixo vazio retorna 400 com a mensagem e sem resultados")
    void custoFixoVazioRetornaErro() throws Exception {
        mvc.perform(post("/api/simular").contentType(MediaType.APPLICATION_JSON).content(json("")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros[0].campo").value("custoFixo"))
                .andExpect(jsonPath("$.erros[0].mensagem").value("Informe o custo fixo mensal"))
                .andExpect(jsonPath("$.resultados").isEmpty());
    }
}
