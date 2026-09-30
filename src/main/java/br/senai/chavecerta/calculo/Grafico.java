package br.senai.chavecerta.calculo;

import java.util.List;

/**
 * Dados do gráfico de resultado mensal por quantidade de oficinas (RF07).
 *
 * @param eixoXMaximo último valor do eixo X (dobro do maior volume informado)
 * @param series      uma série (linha) por proposta
 */
public record Grafico(int eixoXMaximo, List<Serie> series) {

    /**
     * Linha de uma proposta. Como o resultado é uma reta
     * (contribuição × clientes − custo fixo), poucos pontos bastam.
     */
    public record Serie(String nome, double preco, List<Ponto> pontos) {
    }

    /** Um ponto do gráfico: resultado mensal com uma dada quantidade de oficinas. */
    public record Ponto(int clientes, double resultado) {
    }
}
