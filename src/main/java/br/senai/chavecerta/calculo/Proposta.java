package br.senai.chavecerta.calculo;

/**
 * Uma proposta de preço (A, B ou C).
 *
 * @param nome     identificação da proposta (ex.: "A")
 * @param preco    preço mensal (R$/oficina/mês)
 * @param clientes quantidade estimada de oficinas (premissa da equipe)
 */
public record Proposta(String nome, double preco, int clientes) {
}
