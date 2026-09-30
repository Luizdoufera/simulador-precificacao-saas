package br.senai.chavecerta.calculo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Gera o CSV do RF12 com as entradas, as premissas e os resultados.
 * Formato pensado para abrir no Excel/LibreOffice em português:
 * separador ";", decimal com vírgula, sem separador de milhar.
 */
public final class ExportadorCsv {

    static final String SEPARADOR = ";";
    static final String FIM_DE_LINHA = "\r\n";

    private ExportadorCsv() {
        // classe utilitária: não deve ser instanciada
    }

    public static String gerar(Premissas premissas, String beneficio,
                               List<ResultadoProposta> resultados, String interpretacao) {
        List<List<String>> linhas = new ArrayList<>();

        linhas.add(List.of("ChaveCerta - Simulador de Precificação SaaS"));
        linhas.add(List.of("Dados fictícios - simulação. Valores mensais."));
        linhas.add(List.of());

        linhas.add(List.of("Premissa", "Valor"));
        linhas.add(List.of("Custo fixo mensal (R$)", Formatacao.numero(premissas.custoFixo())));
        linhas.add(List.of("Custo variável por oficina (R$)", Formatacao.numero(premissas.custoVariavel())));
        linhas.add(List.of("Taxa de tributos (%)", Formatacao.numero(premissas.taxaPercentual())));
        linhas.add(List.of("Benefício percebido", beneficio == null ? "" : beneficio));
        linhas.add(List.of());

        List<String> cabecalho = new ArrayList<>(List.of("Indicador"));
        resultados.forEach(r -> cabecalho.add("Proposta " + r.nome()));
        linhas.add(cabecalho);

        linhas.add(linha("Preço mensal (R$)", resultados, r -> Formatacao.numero(r.preco())));
        linhas.add(linha("Oficinas estimadas", resultados, r -> String.valueOf(r.clientes())));
        linhas.add(linha("Receita (R$)", resultados, r -> Formatacao.numero(r.receita())));
        linhas.add(linha("Tributos (R$)", resultados, r -> Formatacao.numero(r.tributos())));
        linhas.add(linha("Custo variável total (R$)", resultados, r -> Formatacao.numero(r.custoVariavelTotal())));
        linhas.add(linha("Custo fixo (R$)", resultados, r -> Formatacao.numero(premissas.custoFixo())));
        linhas.add(linha("Resultado mensal (R$)", resultados, r -> Formatacao.numero(r.resultado())));
        linhas.add(linha("Margem (%)", resultados,
                r -> r.temMargem() ? Formatacao.numero(r.margem()) : "não se aplica"));
        linhas.add(linha("Contribuição unitária (R$)", resultados, r -> Formatacao.numero(r.contribuicaoUnitaria())));
        linhas.add(linha("Equilíbrio (oficinas)", resultados,
                r -> r.temEquilibrio() ? String.valueOf(r.equilibrio()) : "sem equilíbrio"));
        linhas.add(linha("Folga (oficinas)", resultados,
                r -> r.temEquilibrio() ? String.valueOf(r.folga()) : "-"));
        linhas.add(List.of());

        linhas.add(List.of("Interpretação", interpretacao == null ? "" : interpretacao));

        StringBuilder csv = new StringBuilder();
        for (List<String> linha : linhas) {
            csv.append(String.join(SEPARADOR, linha.stream().map(ExportadorCsv::celula).toList()));
            csv.append(FIM_DE_LINHA);
        }
        return csv.toString();
    }

    private static List<String> linha(String rotulo, List<ResultadoProposta> resultados,
                                      Function<ResultadoProposta, String> valor) {
        List<String> linha = new ArrayList<>(List.of(rotulo));
        resultados.forEach(r -> linha.add(valor.apply(r)));
        return linha;
    }

    /**
     * Prepara uma célula: coloca entre aspas se tiver ";", aspas ou quebra de linha,
     * e neutraliza textos que a planilha interpretaria como fórmula (=, +, @).
     */
    static String celula(String texto) {
        String valor = texto;
        if (!valor.isEmpty() && "=+@".indexOf(valor.charAt(0)) >= 0) {
            valor = "'" + valor;
        }
        if (valor.contains(SEPARADOR) || valor.contains("\"") || valor.contains("\n") || valor.contains("\r")) {
            valor = "\"" + valor.replace("\"", "\"\"") + "\"";
        }
        return valor;
    }
}
