package br.senai.chavecerta.calculo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Testes da interpretação (RF08) e da formatação usada nela. */
class InterpretacaoTest {

    private static final Premissas PADRAO = new Premissas(3000, 10, 10);

    private static String interpretar(Proposta... propostas) {
        return Interpretacao.gerar(CalculadoraPrecificacao.calcularTodas(PADRAO, List.of(propostas)));
    }

    @Test
    @DisplayName("Exemplo carregado: texto esperado da seção 9 do modelo")
    void textoDoExemplo() {
        String texto = interpretar(
                new Proposta("A", 40, 130),
                new Proposta("B", 50, 100),
                new Proposta("C", 70, 80));

        assertEquals("Nas premissas consideradas, a proposta C (R$ 70) apresenta o maior resultado mensal, "
                + "R$ 1.240,00. Folga sobre o equilíbrio: A 14 oficinas; B 14 oficinas; C 23 oficinas. "
                + "Essa conclusão depende de atingir ao menos 57 oficinas ao preço de R$ 70.", texto);
    }

    @Test
    @DisplayName("Todas negativas: informa que nenhuma cobre os custos")
    void todasNegativas() {
        String texto = interpretar(
                new Proposta("A", 40, 100),   // 26 × 100 − 3000 = −400
                new Proposta("B", 50, 80),    // T5: −200
                new Proposta("C", 11, 100));  // T4: sem equilíbrio

        assertTrue(texto.startsWith(Interpretacao.NENHUMA_COBRE));
        assertTrue(texto.contains("A -16 oficinas (abaixo do equilíbrio)"));
        assertTrue(texto.contains("B -6 oficinas (abaixo do equilíbrio)"));
        assertTrue(texto.contains("C sem equilíbrio"));
        assertTrue(texto.endsWith(Interpretacao.DEPENDE_ESTIMATIVA));
    }

    @Test
    @DisplayName("Resultado negativo não é escolhido, mesmo sendo o de maior receita")
    void ignoraNegativas() {
        String texto = interpretar(
                new Proposta("A", 50, 85),    // T7: −25
                new Proposta("B", 50, 86),    // T8: +10
                new Proposta("C", 11, 1000)); // sem equilíbrio, muito negativo

        assertTrue(texto.contains("a proposta B (R$ 50)"));
        assertTrue(texto.contains("R$ 10,00"));
        assertTrue(texto.contains("B 0 oficinas"));
    }

    @Test
    @DisplayName("Empate: fica a primeira proposta")
    void empate() {
        List<ResultadoProposta> r = CalculadoraPrecificacao.calcularTodas(PADRAO, List.of(
                new Proposta("A", 50, 100),
                new Proposta("B", 50, 100),
                new Proposta("C", 40, 100)));
        assertEquals("A", Interpretacao.melhorProposta(r).nome());
    }

    @Test
    @DisplayName("Não usa frases de certeza sobre o futuro")
    void semCerteza() {
        String texto = interpretar(
                new Proposta("A", 40, 130),
                new Proposta("B", 50, 100),
                new Proposta("C", 70, 80)).toLowerCase();

        assertFalse(texto.contains("vai lucrar"));
        assertFalse(texto.contains("com certeza"));
        assertFalse(texto.contains("garant"));
    }

    @Test
    @DisplayName("Formatação pt-BR de moeda, preço e oficinas")
    void formatacao() {
        assertEquals("R$ 1.240,00", Formatacao.moeda(1240));
        assertEquals("-R$ 3.000,00", Formatacao.moeda(-3000));
        assertEquals("R$ 0,00", Formatacao.moeda(-0.001));
        assertEquals("R$ 70", Formatacao.preco(70));
        assertEquals("R$ 49,90", Formatacao.preco(49.9));
        assertEquals("1240,00", Formatacao.numero(1240));
        assertEquals("1 oficina", Formatacao.oficinas(1));
        assertEquals("57 oficinas", Formatacao.oficinas(57));
    }
}
