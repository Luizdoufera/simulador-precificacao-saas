package br.senai.chavecerta.calculo;

/**
 * Indicadores calculados para uma proposta. Valores sem arredondamento
 * (o arredondamento é feito só na exibição, conforme RNF04).
 *
 * Campos que podem ser {@code null}:
 * - margem: null quando a receita é 0 ("não se aplica" – RF11);
 * - equilibrioBruto, equilibrio e folga: null quando a contribuição
 *   unitária é menor ou igual a 0 ("sem equilíbrio" – RF10).
 */
public record ResultadoProposta(
        String nome,
        double preco,
        int clientes,
        double receita,
        double tributos,
        double custoVariavelTotal,
        double resultado,
        Double margem,
        double contribuicaoUnitaria,
        Double equilibrioBruto,
        Integer equilibrio,
        Integer folga) {

    public boolean temMargem() {
        return margem != null;
    }

    public boolean temEquilibrio() {
        return equilibrio != null;
    }
}
