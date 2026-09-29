package br.senai.chavecerta.calculo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Validação das entradas (RF09 e RNF06). Regras e mensagens conforme
 * modelo_calculos.md, seção 4. Se houver qualquer erro, nada é calculado.
 */
public final class Validador {

    static final String MSG_NEGATIVO = "O valor não pode ser negativo";
    static final String MSG_TAXA = "A taxa deve estar entre 0% e 99,99%";
    static final String MSG_INTEIRO = "Informe um número inteiro de oficinas";
    static final String MSG_TRES_PROPOSTAS = "Informe as três propostas (A, B e C)";

    /** Número simples: sinal opcional, dígitos e parte decimal opcional (ponto ou vírgula). */
    private static final Pattern NUMERO = Pattern.compile("-?\\d+([.,]\\d+)?");

    private static final String[] NOMES_PADRAO = {"A", "B", "C"};

    private Validador() {
        // classe utilitária: não deve ser instanciada
    }

    /** Retorna a lista de erros; lista vazia significa entrada válida. */
    public static List<ErroValidacao> validar(EntradaSimulacao entrada) {
        List<ErroValidacao> erros = new ArrayList<>();

        validarNaoNegativo(erros, "custoFixo", entrada.custoFixo(), "Informe o custo fixo mensal");
        validarNaoNegativo(erros, "custoVariavel", entrada.custoVariavel(), "Informe o custo variável por oficina");
        validarTaxa(erros, entrada.taxaPercentual());

        List<EntradaSimulacao.EntradaProposta> propostas = entrada.propostas();
        if (propostas == null || propostas.size() != 3) {
            erros.add(new ErroValidacao("propostas", MSG_TRES_PROPOSTAS));
            return erros;
        }

        for (int i = 0; i < propostas.size(); i++) {
            EntradaSimulacao.EntradaProposta p = propostas.get(i);
            String nome = (p.nome() == null || p.nome().isBlank()) ? NOMES_PADRAO[i] : p.nome();
            String prefixo = "propostas[" + i + "].";

            validarNaoNegativo(erros, prefixo + "preco", p.preco(), "Informe o preço da proposta " + nome);
            validarClientes(erros, prefixo + "clientes", p.clientes(),
                    "Informe a quantidade de oficinas da proposta " + nome);
        }
        return erros;
    }

    /**
     * Converte o texto digitado em número. Aceita vírgula ou ponto como separador
     * decimal (sem separador de milhar). Retorna null se vazio ou não numérico.
     */
    static Double lerNumero(String texto) {
        if (texto == null) {
            return null;
        }
        String limpo = texto.trim();
        if (!NUMERO.matcher(limpo).matches()) {
            return null;
        }
        return Double.parseDouble(limpo.replace(',', '.'));
    }

    private static void validarNaoNegativo(List<ErroValidacao> erros, String campo, String texto,
                                           String msgVazio) {
        Double valor = lerNumero(texto);
        if (valor == null) {
            erros.add(new ErroValidacao(campo, msgVazio));
        } else if (valor < 0) {
            erros.add(new ErroValidacao(campo, MSG_NEGATIVO));
        }
    }

    private static void validarTaxa(List<ErroValidacao> erros, String texto) {
        Double valor = lerNumero(texto);
        if (valor == null) {
            erros.add(new ErroValidacao("taxaPercentual", "Informe a taxa de tributos"));
        } else if (valor < 0 || valor >= 100) {
            erros.add(new ErroValidacao("taxaPercentual", MSG_TAXA));
        }
    }

    private static void validarClientes(List<ErroValidacao> erros, String campo, String texto,
                                        String msgVazio) {
        Double valor = lerNumero(texto);
        if (valor == null) {
            erros.add(new ErroValidacao(campo, msgVazio));
        } else if (valor < 0) {
            erros.add(new ErroValidacao(campo, MSG_NEGATIVO));
        } else if (valor % 1 != 0 || valor > Integer.MAX_VALUE) {
            erros.add(new ErroValidacao(campo, MSG_INTEIRO));
        }
    }
}
