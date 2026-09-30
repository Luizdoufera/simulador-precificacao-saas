package br.senai.chavecerta.web;

import br.senai.chavecerta.calculo.CalculadoraPrecificacao;
import br.senai.chavecerta.calculo.EntradaSimulacao;
import br.senai.chavecerta.calculo.ErroValidacao;
import br.senai.chavecerta.calculo.ExportadorCsv;
import br.senai.chavecerta.calculo.Grafico;
import br.senai.chavecerta.calculo.Interpretacao;
import br.senai.chavecerta.calculo.Premissas;
import br.senai.chavecerta.calculo.Proposta;
import br.senai.chavecerta.calculo.ResultadoProposta;
import br.senai.chavecerta.calculo.Validador;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador fino: recebe os dados da tela, valida, chama o núcleo de
 * cálculo e devolve JSON ou CSV. Não contém fórmulas.
 */
@RestController
@RequestMapping("/api")
public class SimulacaoController {

    /** BOM do UTF-8: faz o Excel reconhecer os acentos do CSV. */
    private static final String BOM = "﻿";

    /**
     * POST /api/simular
     * Retorna 200 com os resultados, ou 400 com a lista de erros (RF09).
     */
    @PostMapping("/simular")
    public ResponseEntity<RespostaSimulacao> simular(@RequestBody EntradaSimulacao entrada) {
        RespostaSimulacao resposta = processar(entrada);
        return resposta.erros().isEmpty()
                ? ResponseEntity.ok(resposta)
                : ResponseEntity.badRequest().body(resposta);
    }

    /**
     * POST /api/exportar-csv (RF12)
     * Retorna o arquivo CSV para download, ou 400 com a lista de erros em JSON.
     */
    @PostMapping("/exportar-csv")
    public ResponseEntity<?> exportarCsv(@RequestBody EntradaSimulacao entrada) {
        RespostaSimulacao resposta = processar(entrada);
        if (!resposta.erros().isEmpty()) {
            return ResponseEntity.badRequest().body(resposta);
        }

        String csv = ExportadorCsv.gerar(resposta.premissas(), entrada.beneficio(),
                resposta.resultados(), resposta.interpretacao());
        return ResponseEntity.ok()
                .contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename("simulacao_chavecerta.csv").build().toString())
                .body((BOM + csv).getBytes(StandardCharsets.UTF_8));
    }

    /** Valida e, se estiver tudo certo, calcula resultados, gráfico e interpretação. */
    private static RespostaSimulacao processar(EntradaSimulacao entrada) {
        List<ErroValidacao> erros = Validador.validar(entrada);
        if (!erros.isEmpty()) {
            return RespostaSimulacao.comErros(erros);
        }

        Premissas premissas = entrada.paraPremissas();
        List<Proposta> propostas = entrada.paraPropostas();
        List<ResultadoProposta> resultados = CalculadoraPrecificacao.calcularTodas(premissas, propostas);
        Grafico grafico = CalculadoraPrecificacao.montarGrafico(premissas, propostas);
        String interpretacao = Interpretacao.gerar(resultados);
        return RespostaSimulacao.comResultados(premissas, resultados, grafico, interpretacao);
    }
}
