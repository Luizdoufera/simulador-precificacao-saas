package br.senai.chavecerta.calculo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Testes do CSV (RF12). */
class ExportadorCsvTest {

    private static final Premissas PADRAO = new Premissas(3000, 10, 10);

    private static String csv(String beneficio, Proposta... propostas) {
        List<ResultadoProposta> r = CalculadoraPrecificacao.calcularTodas(PADRAO, List.of(propostas));
        return ExportadorCsv.gerar(PADRAO, beneficio, r, Interpretacao.gerar(r));
    }

    private static List<String> linhas(String csv) {
        return List.of(csv.split(ExportadorCsv.FIM_DE_LINHA));
    }

    @Test
    @DisplayName("Exemplo: contém premissas e os mesmos valores da tabela")
    void exemplo() {
        List<String> l = linhas(csv("Fim do papel",
                new Proposta("A", 40, 130), new Proposta("B", 50, 100), new Proposta("C", 70, 80)));

        assertTrue(l.contains("Dados fictícios - simulação. Valores mensais."));
        assertTrue(l.contains("Custo fixo mensal (R$);3000,00"));
        assertTrue(l.contains("Custo variável por oficina (R$);10,00"));
        assertTrue(l.contains("Taxa de tributos (%);10,00"));
        assertTrue(l.contains("Benefício percebido;Fim do papel"));
        assertTrue(l.contains("Indicador;Proposta A;Proposta B;Proposta C"));
        assertTrue(l.contains("Receita (R$);5200,00;5000,00;5600,00"));
        assertTrue(l.contains("Resultado mensal (R$);380,00;500,00;1240,00"));
        assertTrue(l.contains("Margem (%);7,31;10,00;22,14"));
        assertTrue(l.contains("Equilíbrio (oficinas);116;86;57"));
        assertTrue(l.contains("Folga (oficinas);14;14;23"));
        // a interpretação tem ";" entre as folgas, por isso vai entre aspas
        assertTrue(l.stream().anyMatch(s -> s.startsWith("Interpretação;\"Nas premissas consideradas, a proposta C")));
    }

    @Test
    @DisplayName("Casos especiais: margem não se aplica e sem equilíbrio")
    void casosEspeciais() {
        List<String> l = linhas(csv("",
                new Proposta("A", 11, 100), new Proposta("B", 50, 0), new Proposta("C", 50, 86)));

        assertTrue(l.contains("Resultado mensal (R$);-3010,00;-3000,00;10,00"));
        assertTrue(l.contains("Margem (%);-273,64;não se aplica;0,23"));
        assertTrue(l.contains("Equilíbrio (oficinas);sem equilíbrio;86;86"));
        assertTrue(l.contains("Folga (oficinas);-;-86;0"));
    }

    @Test
    @DisplayName("Texto com ponto e vírgula, aspas ou quebra de linha vai entre aspas")
    void escapeDeTexto() {
        assertEquals("simples", ExportadorCsv.celula("simples"));
        assertEquals("\"a;b\"", ExportadorCsv.celula("a;b"));
        assertEquals("\"diz \"\"oi\"\"\"", ExportadorCsv.celula("diz \"oi\""));
        assertEquals("\"linha 1\nlinha 2\"", ExportadorCsv.celula("linha 1\nlinha 2"));
    }

    @Test
    @DisplayName("Texto que começa como fórmula de planilha é neutralizado")
    void semFormula() {
        assertEquals("'=1+1", ExportadorCsv.celula("=1+1"));
        assertEquals("'@SOMA(A1)", ExportadorCsv.celula("@SOMA(A1)"));
        assertEquals("-3010,00", ExportadorCsv.celula("-3010,00"));
    }
}
