package br.senai.chavecerta.calculo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Texto de interpretação gerado por regra (RF08), conforme
 * modelo_calculos.md, seção 9. Não usa frases de certeza sobre o futuro.
 */
public final class Interpretacao {

    static final String NENHUMA_COBRE = "Nenhuma proposta cobre os custos nas premissas consideradas.";
    static final String DEPENDE_ESTIMATIVA = "Essa conclusão depende da estimativa de oficinas em cada preço.";

    private Interpretacao() {
        // classe utilitária: não deve ser instanciada
    }

    /** Gera o texto a partir dos resultados já calculados (e validados). */
    public static String gerar(List<ResultadoProposta> resultados) {
        ResultadoProposta melhor = melhorProposta(resultados);
        String folgas = "Folga sobre o equilíbrio: " + textoFolgas(resultados) + ".";

        if (melhor == null) {
            return NENHUMA_COBRE + " " + folgas + " " + DEPENDE_ESTIMATIVA;
        }

        String abertura = "Nas premissas consideradas, a proposta " + melhor.nome()
                + " (" + Formatacao.preco(melhor.preco()) + ") apresenta o maior resultado mensal, "
                + Formatacao.moeda(melhor.resultado()) + ".";

        // Condição que muda a decisão (regra 5); sem equilíbrio, cai no lembrete geral
        String condicao = melhor.temEquilibrio()
                ? "Essa conclusão depende de atingir ao menos " + Formatacao.oficinas(melhor.equilibrio())
                        + " ao preço de " + Formatacao.preco(melhor.preco()) + "."
                : DEPENDE_ESTIMATIVA;

        return abertura + " " + folgas + " " + condicao;
    }

    /**
     * Regra 2: entre as propostas com resultado ≥ 0, a de maior resultado.
     * Em caso de empate, fica a primeira (A antes de B antes de C).
     * Retorna null se todas tiverem resultado negativo (regra 4).
     */
    static ResultadoProposta melhorProposta(List<ResultadoProposta> resultados) {
        ResultadoProposta melhor = null;
        for (ResultadoProposta r : resultados) {
            if (r.resultado() >= 0 && (melhor == null || r.resultado() > melhor.resultado())) {
                melhor = r;
            }
        }
        return melhor;
    }

    /** Regra 3: folga de cada proposta, ex.: "A 14 oficinas; B −6 oficinas (abaixo do equilíbrio)". */
    private static String textoFolgas(List<ResultadoProposta> resultados) {
        return resultados.stream()
                .map(r -> r.nome() + " " + textoFolga(r))
                .collect(Collectors.joining("; "));
    }

    private static String textoFolga(ResultadoProposta r) {
        if (!r.temEquilibrio()) {
            return "sem equilíbrio";
        }
        String texto = Formatacao.oficinas(r.folga());
        return r.folga() < 0 ? texto + " (abaixo do equilíbrio)" : texto;
    }
}
