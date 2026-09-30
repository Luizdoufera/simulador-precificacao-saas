package br.senai.chavecerta.calculo;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Formatação de valores no padrão brasileiro, usada nos textos gerados
 * em Java (interpretação e CSV). O arredondamento acontece só aqui (RNF04).
 */
public final class Formatacao {

    private static final DecimalFormatSymbols PT_BR = DecimalFormatSymbols.getInstance(Locale.forLanguageTag("pt-BR"));

    private Formatacao() {
        // classe utilitária: não deve ser instanciada
    }

    /** 1240 → "R$ 1.240,00"; −3000 → "−R$ 3.000,00". */
    public static String moeda(double valor) {
        String numero = new DecimalFormat("#,##0.00", PT_BR).format(Math.abs(valor));
        return (valor < 0 && !numero.equals("0,00") ? "-" : "") + "R$ " + numero;
    }

    /** Preço sem centavos quando inteiro: 70 → "R$ 70"; 49,9 → "R$ 49,90". */
    public static String preco(double valor) {
        return valor == Math.rint(valor) ? "R$ " + new DecimalFormat("#,##0", PT_BR).format(valor) : moeda(valor);
    }

    /** Número com 2 casas e vírgula, sem separador de milhar: 1240 → "1240,00" (para planilhas). */
    public static String numero(double valor) {
        return new DecimalFormat("0.00", PT_BR).format(valor);
    }

    /** 1 → "1 oficina"; 14 → "14 oficinas". */
    public static String oficinas(int quantidade) {
        return quantidade + (Math.abs(quantidade) == 1 ? " oficina" : " oficinas");
    }
}
