package br.senai.chavecerta.calculo;

/**
 * Erro de um campo da tela.
 *
 * @param campo    identificador do campo (ex.: "custoFixo", "propostas[0].preco"),
 *                 usado pela tela para mostrar a mensagem junto ao campo
 * @param mensagem texto mostrado ao usuário
 */
public record ErroValidacao(String campo, String mensagem) {
}
