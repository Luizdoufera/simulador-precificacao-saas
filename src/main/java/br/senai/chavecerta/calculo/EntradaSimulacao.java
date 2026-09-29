package br.senai.chavecerta.calculo;

import java.util.List;

/**
 * Dados como foram digitados na tela (texto), antes da validação.
 * Os números chegam como String para que o Validador possa distinguir
 * campo vazio, texto não numérico e número decimal onde se espera inteiro.
 *
 * @param custoFixo      custo fixo mensal (R$)
 * @param custoVariavel  custo variável por oficina (R$)
 * @param taxaPercentual taxa de tributos em %
 * @param beneficio      benefício percebido pelo cliente (texto; não entra no cálculo)
 * @param propostas      as três propostas (A, B e C)
 */
public record EntradaSimulacao(
        String custoFixo,
        String custoVariavel,
        String taxaPercentual,
        String beneficio,
        List<EntradaProposta> propostas) {

    /**
     * Uma proposta como digitada na tela.
     *
     * @param nome     identificação (ex.: "A")
     * @param preco    preço mensal (R$)
     * @param clientes quantidade estimada de oficinas
     */
    public record EntradaProposta(String nome, String preco, String clientes) {
    }

    /** Converte as premissas. Chamar somente depois de Validador.validar não retornar erros. */
    public Premissas paraPremissas() {
        return new Premissas(
                Validador.lerNumero(custoFixo),
                Validador.lerNumero(custoVariavel),
                Validador.lerNumero(taxaPercentual));
    }

    /** Converte as propostas. Chamar somente depois de Validador.validar não retornar erros. */
    public List<Proposta> paraPropostas() {
        return propostas.stream()
                .map(p -> new Proposta(p.nome(), Validador.lerNumero(p.preco()),
                        (int) (double) Validador.lerNumero(p.clientes())))
                .toList();
    }
}
