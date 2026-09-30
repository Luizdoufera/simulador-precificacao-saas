package br.senai.chavecerta.calculo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testes do núcleo de cálculo. Valores esperados retirados de
 * modelo_calculos.md, seções 7 e 8. Custo fixo 3.000 e variável 10 em todos.
 */
class CalculadoraPrecificacaoTest {

    /** Tolerância de R$ 0,01 para valores monetários (RNF04). */
    private static final double TOLERANCIA = 0.01;

    private static final Premissas PADRAO = new Premissas(3000, 10, 10);

    private static ResultadoProposta calcular(Premissas premissas, double preco, int clientes) {
        return CalculadoraPrecificacao.calcular(premissas, new Proposta("X", preco, clientes));
    }

    @Test
    @DisplayName("T1 – Caso de referência: preço 50, 100 oficinas, taxa 10%")
    void t1CasoReferencia() {
        ResultadoProposta r = calcular(PADRAO, 50, 100);

        assertEquals(5000.00, r.receita(), TOLERANCIA);
        assertEquals(500.00, r.tributos(), TOLERANCIA);
        assertEquals(1000.00, r.custoVariavelTotal(), TOLERANCIA);
        assertEquals(500.00, r.resultado(), TOLERANCIA);
        assertEquals(10.00, r.margem(), TOLERANCIA);
        assertEquals(35.00, r.contribuicaoUnitaria(), TOLERANCIA);
        assertEquals(86, r.equilibrio());
        assertEquals(14, r.folga());
    }

    @Test
    @DisplayName("T2 – Taxa zero: divisão exata deve dar equilíbrio 75, não 76")
    void t2TaxaZero() {
        ResultadoProposta r = calcular(new Premissas(3000, 10, 0), 50, 100);

        assertEquals(5000.00, r.receita(), TOLERANCIA);
        assertEquals(0.00, r.tributos(), TOLERANCIA);
        assertEquals(1000.00, r.resultado(), TOLERANCIA);
        assertEquals(20.00, r.margem(), TOLERANCIA);
        assertEquals(40.00, r.contribuicaoUnitaria(), TOLERANCIA);
        assertEquals(75, r.equilibrio());
    }

    @Test
    @DisplayName("T4 – Desfavorável: preço 11 gera contribuição −0,10 e sem equilíbrio")
    void t4SemEquilibrio() {
        ResultadoProposta r = calcular(PADRAO, 11, 100);

        assertEquals(1100.00, r.receita(), TOLERANCIA);
        assertEquals(110.00, r.tributos(), TOLERANCIA);
        assertEquals(-3010.00, r.resultado(), TOLERANCIA);
        assertEquals(-273.64, r.margem(), TOLERANCIA);
        assertEquals(-0.10, r.contribuicaoUnitaria(), TOLERANCIA);
        assertFalse(r.temEquilibrio());
        assertNull(r.equilibrioBruto());
        assertNull(r.folga());
    }

    @Test
    @DisplayName("T5 – Alteração de premissa: T1 com 80 oficinas")
    void t5OitentaOficinas() {
        ResultadoProposta r = calcular(PADRAO, 50, 80);

        assertEquals(4000.00, r.receita(), TOLERANCIA);
        assertEquals(400.00, r.tributos(), TOLERANCIA);
        assertEquals(800.00, r.custoVariavelTotal(), TOLERANCIA);
        assertEquals(-200.00, r.resultado(), TOLERANCIA);
        assertEquals(-5.00, r.margem(), TOLERANCIA);
        assertEquals(86, r.equilibrio());
        assertEquals(-6, r.folga());
    }

    @Test
    @DisplayName("T6 – Receita zero: 0 oficinas, margem não se aplica")
    void t6ReceitaZero() {
        ResultadoProposta r = calcular(PADRAO, 50, 0);

        assertEquals(0.00, r.receita(), TOLERANCIA);
        assertEquals(0.00, r.tributos(), TOLERANCIA);
        assertEquals(-3000.00, r.resultado(), TOLERANCIA);
        assertFalse(r.temMargem());
        assertEquals(35.00, r.contribuicaoUnitaria(), TOLERANCIA);
        assertEquals(86, r.equilibrio());
    }

    @Test
    @DisplayName("T7 – Borda abaixo: 85 oficinas ainda dá prejuízo")
    void t7BordaAbaixo() {
        ResultadoProposta r = calcular(PADRAO, 50, 85);

        assertEquals(4250.00, r.receita(), TOLERANCIA);
        assertEquals(425.00, r.tributos(), TOLERANCIA);
        assertEquals(-25.00, r.resultado(), TOLERANCIA);
        assertEquals(-0.59, r.margem(), TOLERANCIA);
        assertEquals(86, r.equilibrio());
    }

    @Test
    @DisplayName("T8 – Borda no equilíbrio: 86 oficinas já não dá prejuízo")
    void t8BordaNoEquilibrio() {
        ResultadoProposta r = calcular(PADRAO, 50, 86);

        assertEquals(4300.00, r.receita(), TOLERANCIA);
        assertEquals(430.00, r.tributos(), TOLERANCIA);
        assertEquals(10.00, r.resultado(), TOLERANCIA);
        assertEquals(0.23, r.margem(), TOLERANCIA);
        assertEquals(86, r.equilibrio());
        assertEquals(0, r.folga());
    }

    @Test
    @DisplayName("Exemplo completo: propostas A, B e C (seção 7)")
    void exemploTresPropostas() {
        List<ResultadoProposta> r = CalculadoraPrecificacao.calcularTodas(PADRAO, List.of(
                new Proposta("A", 40, 130),
                new Proposta("B", 50, 100),
                new Proposta("C", 70, 80)));

        assertEquals(380.00, r.get(0).resultado(), TOLERANCIA);
        assertEquals(500.00, r.get(1).resultado(), TOLERANCIA);
        assertEquals(1240.00, r.get(2).resultado(), TOLERANCIA);

        assertEquals(7.31, r.get(0).margem(), TOLERANCIA);
        assertEquals(10.00, r.get(1).margem(), TOLERANCIA);
        assertEquals(22.14, r.get(2).margem(), TOLERANCIA);

        assertEquals(116, r.get(0).equilibrio());
        assertEquals(86, r.get(1).equilibrio());
        assertEquals(57, r.get(2).equilibrio());

        assertEquals(14, r.get(0).folga());
        assertEquals(14, r.get(1).folga());
        assertEquals(23, r.get(2).folga());
    }

    @Test
    @DisplayName("Identidade: resultado = contribuição × clientes − custo fixo (seção 3.1)")
    void identidadeDeConferencia() {
        int[] volumes = {0, 1, 57, 85, 86, 130, 1000};
        double[] precos = {11, 40, 50, 70, 99.9};
        for (double preco : precos) {
            for (int clientes : volumes) {
                ResultadoProposta r = calcular(PADRAO, preco, clientes);
                double pelaIdentidade = r.contribuicaoUnitaria() * clientes - PADRAO.custoFixo();
                assertEquals(pelaIdentidade, r.resultado(), TOLERANCIA,
                        "preço " + preco + ", clientes " + clientes);
            }
        }
    }

    @Test
    @DisplayName("Custo fixo zero com contribuição positiva: equilíbrio 0")
    void custoFixoZero() {
        ResultadoProposta r = calcular(new Premissas(0, 10, 10), 50, 100);
        assertEquals(0, r.equilibrio());
    }

    @Test
    @DisplayName("Contribuição exatamente zero: sem equilíbrio, mesmo com custo fixo zero")
    void contribuicaoZero() {
        // preço 10, taxa 0%, variável 10 → contribuição = 0
        ResultadoProposta r = calcular(new Premissas(0, 10, 0), 10, 100);
        assertEquals(0.00, r.contribuicaoUnitaria(), TOLERANCIA);
        assertFalse(r.temEquilibrio());
    }

    @Test
    @DisplayName("Gráfico: eixo X até o dobro do maior volume e linhas começando em −custo fixo")
    void graficoDoExemplo() {
        Grafico g = CalculadoraPrecificacao.montarGrafico(PADRAO, List.of(
                new Proposta("A", 40, 130),
                new Proposta("B", 50, 100),
                new Proposta("C", 70, 80)));

        assertEquals(260, g.eixoXMaximo());
        assertEquals(3, g.series().size());
        for (Grafico.Serie serie : g.series()) {
            Grafico.Ponto primeiro = serie.pontos().get(0);
            Grafico.Ponto ultimo = serie.pontos().get(serie.pontos().size() - 1);
            assertEquals(0, primeiro.clientes());
            assertEquals(-3000.00, primeiro.resultado(), TOLERANCIA);
            assertEquals(260, ultimo.clientes());
        }
    }

    @Test
    @DisplayName("Gráfico: a linha muda de sinal no equilíbrio (RF07)")
    void graficoCruzaZeroNoEquilibrio() {
        Grafico g = CalculadoraPrecificacao.montarGrafico(PADRAO, List.of(new Proposta("B", 50, 100)));
        List<Grafico.Ponto> pontos = g.series().get(0).pontos();

        // pontos esperados: 0, 86 (equilíbrio), 100 (estimado) e 200 (fim do eixo)
        assertEquals(List.of(0, 86, 100, 200), pontos.stream().map(Grafico.Ponto::clientes).toList());
        assertEquals(10.00, pontos.get(1).resultado(), TOLERANCIA);
        assertEquals(500.00, pontos.get(2).resultado(), TOLERANCIA);
    }

    @Test
    @DisplayName("Gráfico: sem equilíbrio, a linha só desce; volumes zerados usam eixo até 10")
    void graficoSemEquilibrio() {
        Grafico g = CalculadoraPrecificacao.montarGrafico(PADRAO, List.of(new Proposta("A", 11, 0)));

        assertEquals(10, g.eixoXMaximo());
        List<Grafico.Ponto> pontos = g.series().get(0).pontos();
        assertEquals(List.of(0, 10), pontos.stream().map(Grafico.Ponto::clientes).toList());
        assertEquals(-3001.00, pontos.get(1).resultado(), TOLERANCIA);
    }

    @Test
    @DisplayName("Teto arredondado: resíduo de ponto flutuante não aumenta o equilíbrio")
    void tetoIgnoraResiduo() {
        assertEquals(75, CalculadoraPrecificacao.tetoArredondado(75.00000000001));
        assertEquals(76, CalculadoraPrecificacao.tetoArredondado(75.001));
        assertEquals(86, CalculadoraPrecificacao.tetoArredondado(85.714285));
    }
}
