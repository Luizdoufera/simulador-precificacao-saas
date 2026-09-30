package br.senai.chavecerta.web;

import br.senai.chavecerta.calculo.ErroValidacao;
import br.senai.chavecerta.calculo.Grafico;
import br.senai.chavecerta.calculo.Premissas;
import br.senai.chavecerta.calculo.ResultadoProposta;
import java.util.List;

/**
 * Corpo JSON devolvido por POST /api/simular.
 * Com erros: "erros" preenchido, demais campos nulos e "resultados" vazio.
 * Sem erros: "erros" vazio e os resultados das três propostas.
 */
public record RespostaSimulacao(
        List<ErroValidacao> erros,
        Premissas premissas,
        List<ResultadoProposta> resultados,
        Grafico grafico,
        String interpretacao) {

    static RespostaSimulacao comErros(List<ErroValidacao> erros) {
        return new RespostaSimulacao(erros, null, List.of(), null, null);
    }

    static RespostaSimulacao comResultados(Premissas premissas, List<ResultadoProposta> resultados,
                                           Grafico grafico, String interpretacao) {
        return new RespostaSimulacao(List.of(), premissas, resultados, grafico, interpretacao);
    }
}
