package br.senai.chavecerta.calculo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.senai.chavecerta.calculo.EntradaSimulacao.EntradaProposta;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Testes das validações (RF09), com mensagens de modelo_calculos.md, seção 4. */
class ValidadorTest {

    private static final List<EntradaProposta> PROPOSTAS_EXEMPLO = List.of(
            new EntradaProposta("A", "40", "130"),
            new EntradaProposta("B", "50", "100"),
            new EntradaProposta("C", "70", "80"));

    private static EntradaSimulacao entrada(String fixo, String variavel, String taxa) {
        return new EntradaSimulacao(fixo, variavel, taxa, "", PROPOSTAS_EXEMPLO);
    }

    private static EntradaSimulacao comPropostaA(String preco, String clientes) {
        return new EntradaSimulacao("3000", "10", "10", "", List.of(
                new EntradaProposta("A", preco, clientes),
                PROPOSTAS_EXEMPLO.get(1),
                PROPOSTAS_EXEMPLO.get(2)));
    }

    /** Verifica que há exatamente um erro, no campo e com a mensagem esperados. */
    private static void assertErroUnico(EntradaSimulacao entrada, String campo, String mensagem) {
        List<ErroValidacao> erros = Validador.validar(entrada);
        assertEquals(List.of(new ErroValidacao(campo, mensagem)), erros);
    }

    @Test
    @DisplayName("Exemplo carregado é válido e converte corretamente")
    void exemploValido() {
        EntradaSimulacao e = entrada("3000", "10", "10");

        assertTrue(Validador.validar(e).isEmpty());
        assertEquals(new Premissas(3000, 10, 10), e.paraPremissas());
        assertEquals(new Proposta("A", 40, 130), e.paraPropostas().get(0));
    }

    @Test
    @DisplayName("T3 – Custo fixo vazio: \"Informe o custo fixo mensal\"")
    void t3CustoFixoVazio() {
        assertErroUnico(entrada("", "10", "10"), "custoFixo", "Informe o custo fixo mensal");
        assertErroUnico(entrada(null, "10", "10"), "custoFixo", "Informe o custo fixo mensal");
        assertErroUnico(entrada("   ", "10", "10"), "custoFixo", "Informe o custo fixo mensal");
    }

    @Test
    @DisplayName("Texto não numérico é tratado como campo não informado")
    void naoNumerico() {
        assertErroUnico(entrada("abc", "10", "10"), "custoFixo", "Informe o custo fixo mensal");
        assertErroUnico(entrada("3000", "dez", "10"), "custoVariavel", "Informe o custo variável por oficina");
        assertErroUnico(entrada("3000", "10", "NaN"), "taxaPercentual", "Informe a taxa de tributos");
    }

    @Test
    @DisplayName("Valores negativos são rejeitados")
    void negativos() {
        assertErroUnico(entrada("-1", "10", "10"), "custoFixo", Validador.MSG_NEGATIVO);
        assertErroUnico(entrada("3000", "-10", "10"), "custoVariavel", Validador.MSG_NEGATIVO);
        assertErroUnico(comPropostaA("-40", "130"), "propostas[0].preco", Validador.MSG_NEGATIVO);
        assertErroUnico(comPropostaA("40", "-1"), "propostas[0].clientes", Validador.MSG_NEGATIVO);
    }

    @Test
    @DisplayName("Taxa deve estar entre 0% e 99,99%")
    void limitesDaTaxa() {
        assertTrue(Validador.validar(entrada("3000", "10", "0")).isEmpty());
        assertTrue(Validador.validar(entrada("3000", "10", "99,99")).isEmpty());
        assertErroUnico(entrada("3000", "10", "100"), "taxaPercentual", Validador.MSG_TAXA);
        assertErroUnico(entrada("3000", "10", "-0,5"), "taxaPercentual", Validador.MSG_TAXA);
    }

    @Test
    @DisplayName("Quantidade de oficinas deve ser inteira")
    void clientesInteiro() {
        assertErroUnico(comPropostaA("40", "130,5"), "propostas[0].clientes", Validador.MSG_INTEIRO);
        assertTrue(Validador.validar(comPropostaA("40", "0")).isEmpty());
    }

    @Test
    @DisplayName("Campos vazios da proposta citam o nome da proposta")
    void propostaVazia() {
        assertErroUnico(comPropostaA("", "130"), "propostas[0].preco", "Informe o preço da proposta A");
        assertErroUnico(comPropostaA("40", ""), "propostas[0].clientes",
                "Informe a quantidade de oficinas da proposta A");
    }

    @Test
    @DisplayName("Vírgula e ponto são aceitos como separador decimal")
    void separadorDecimal() {
        assertEquals(10.5, Validador.lerNumero("10,5"));
        assertEquals(10.5, Validador.lerNumero("10.5"));
        assertEquals(null, Validador.lerNumero("1.000,50"));
    }

    @Test
    @DisplayName("Exige exatamente três propostas")
    void tresPropostas() {
        EntradaSimulacao duas = new EntradaSimulacao("3000", "10", "10", "", PROPOSTAS_EXEMPLO.subList(0, 2));
        assertErroUnico(duas, "propostas", Validador.MSG_TRES_PROPOSTAS);
    }

    @Test
    @DisplayName("Vários erros são informados de uma vez")
    void variosErros() {
        List<ErroValidacao> erros = Validador.validar(entrada("", "-10", "150"));
        assertEquals(3, erros.size());
    }
}
