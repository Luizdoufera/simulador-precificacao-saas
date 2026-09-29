package br.senai.chavecerta.calculo;

/**
 * Premissas comuns às três propostas. Todos os valores são mensais.
 *
 * @param custoFixo      custo fixo mensal (R$/mês)
 * @param custoVariavel  custo variável por oficina (R$/oficina/mês)
 * @param taxaPercentual taxa hipotética de tributos, em % (ex.: 10 para 10%)
 */
public record Premissas(double custoFixo, double custoVariavel, double taxaPercentual) {

    /** Taxa convertida em fração (10% vira 0,10), conforme RNF03. */
    public double taxa() {
        return taxaPercentual / 100.0;
    }
}
