package br.senai.chavecerta.web;

import br.senai.chavecerta.calculo.CalculadoraPrecificacao;
import br.senai.chavecerta.calculo.EntradaSimulacao;
import br.senai.chavecerta.calculo.ErroValidacao;
import br.senai.chavecerta.calculo.Premissas;
import br.senai.chavecerta.calculo.ResultadoProposta;
import br.senai.chavecerta.calculo.Validador;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador fino: recebe os dados da tela, valida, chama o núcleo de
 * cálculo e devolve JSON. Não contém fórmulas.
 */
@RestController
@RequestMapping("/api")
public class SimulacaoController {

    /**
     * POST /api/simular
     * Retorna 200 com os resultados, ou 400 com a lista de erros (RF09).
     */
    @PostMapping("/simular")
    public ResponseEntity<RespostaSimulacao> simular(@RequestBody EntradaSimulacao entrada) {
        List<ErroValidacao> erros = Validador.validar(entrada);
        if (!erros.isEmpty()) {
            return ResponseEntity.badRequest().body(RespostaSimulacao.comErros(erros));
        }

        Premissas premissas = entrada.paraPremissas();
        List<ResultadoProposta> resultados =
                CalculadoraPrecificacao.calcularTodas(premissas, entrada.paraPropostas());
        return ResponseEntity.ok(RespostaSimulacao.comResultados(premissas, resultados));
    }
}
