package br.senai.chavecerta.calculo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Núcleo de cálculo do simulador. Métodos puros: recebem dados e retornam
 * resultados, sem acessar tela, rede ou Spring (RNF02).
 *
 * Fórmulas conforme modelo_calculos.md, seção 3.
 * Os dados devem chegar já validados (ver Validador).
 */
public final class CalculadoraPrecificacao {

    private CalculadoraPrecificacao() {
        // classe utilitária: não deve ser instanciada
    }

    /** Calcula os indicadores de uma proposta (RF04 e RF05). */
    public static ResultadoProposta calcular(Premissas premissas, Proposta proposta) {
        double taxa = premissas.taxa();
        double preco = proposta.preco();
        int clientes = proposta.clientes();

        double receita = preco * clientes;
        double tributos = receita * taxa;
        double custoVariavelTotal = premissas.custoVariavel() * clientes;
        double resultado = receita - premissas.custoFixo() - custoVariavelTotal - tributos;
        double contribuicaoUnitaria = preco * (1 - taxa) - premissas.custoVariavel();

        // RF11: margem só com receita positiva
        Double margem = receita > 0 ? 100 * resultado / receita : null;

        // RF10: sem equilíbrio quando a contribuição unitária é <= 0
        Double equilibrioBruto = null;
        Integer equilibrio = null;
        Integer folga = null;
        if (contribuicaoUnitaria > 0) {
            equilibrioBruto = premissas.custoFixo() / contribuicaoUnitaria;
            equilibrio = tetoArredondado(equilibrioBruto);
            folga = clientes - equilibrio;
        }

        return new ResultadoProposta(proposta.nome(), preco, clientes, receita, tributos,
                custoVariavelTotal, resultado, margem, contribuicaoUnitaria,
                equilibrioBruto, equilibrio, folga);
    }

    /** Calcula todas as propostas com as mesmas premissas. */
    public static List<ResultadoProposta> calcularTodas(Premissas premissas, List<Proposta> propostas) {
        return propostas.stream()
                .map(proposta -> calcular(premissas, proposta))
                .toList();
    }

    /**
     * Teto do quociente, arredondando antes para 6 casas decimais.
     * Evita que um resíduo de ponto flutuante (ex.: 75,00000000001) vire 76.
     * Ver modelo_calculos.md, seção 5.
     */
    static int tetoArredondado(double bruto) {
        double arredondado = BigDecimal.valueOf(bruto).setScale(6, RoundingMode.HALF_UP).doubleValue();
        return (int) Math.ceil(arredondado);
    }
}
