package br.senai.chavecerta.web;

import br.senai.chavecerta.calculo.ErroValidacao;
import br.senai.chavecerta.calculo.Premissas;
import br.senai.chavecerta.calculo.ResultadoProposta;
import java.util.List;

/**
 * Corpo JSON devolvido por POST /api/simular.
 * Com erros: "erros" preenchido, "premissas" nulo e "resultados" vazio.
 * Sem erros: "erros" vazio e os resultados das três propostas.
 */
public record RespostaSimulacao(
        List<ErroValidacao> erros,
        Premissas premissas,
        List<ResultadoProposta> resultados) {

    static RespostaSimulacao comErros(List<ErroValidacao> erros) {
        return new RespostaSimulacao(erros, null, List.of());
    }

    static RespostaSimulacao comResultados(Premissas premissas, List<ResultadoProposta> resultados) {
        return new RespostaSimulacao(List.of(), premissas, resultados);
    }
}
