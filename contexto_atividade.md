# Contexto da Atividade

**Instituição:** Faculdade SENAI FATESG – Curso Superior de Engenharia de Software
**Disciplina:** Engenharia Econômica
**Atividade:** Aplicações de Engenharia Econômica para Engenheiros de Software – desenvolvimento de aplicações de apoio à decisão com agentes de IA
**Opção escolhida:** 2 – Simulador de precificação de SaaS
**Grupo:** Gubio Garcia dos Santos e Luiz Fernando de Pádua Paixão
**Local e ano:** Goiânia, 2026

> Fonte: `docs/Atividade_ Aplicações_de_Engenharia_Economica_GL.pdf` (documento do grupo).
> O enunciado original do professor não está na pasta. Se estiver disponível, salvar em `docs/` e citar aqui.

---

## 1. Situação-problema

A **ChaveCerta** é uma startup de três desenvolvedores que criou um sistema web de gestão para oficinas mecânicas de pequeno porte. O sistema cuida de ordens de serviço, orçamentos, histórico dos veículos e lembretes de revisão. O produto já funciona em versão beta com duas oficinas parceiras.

Antes do lançamento, a equipe precisa definir o preço da assinatura mensal e responder a duas perguntas:

1. Com o número de oficinas que esperam conquistar, a receita cobre os custos?
2. A partir de quantas oficinas o negócio deixa de dar prejuízo?

**Usuário da aplicação:** Rafael, sócio responsável pela parte comercial. Ele conhece o dia a dia das oficinas, mas não tem uma ferramenta para comparar preços.

**Decisão a tomar:** qual preço mensal adotar (R$ 40, R$ 50 ou R$ 70) e se o volume de oficinas previsto sustenta esse preço.

**Benefício percebido pelo cliente (texto):** fim das ordens de serviço em papel, orçamento enviado ao dono do carro pelo celular, histórico de cada veículo e lembretes automáticos de revisão, que trazem o cliente de volta à oficina.

---

## 2. Dados do caso (fictícios – simulação)

| Premissa | Valor |
|---|---|
| Custo fixo mensal (hospedagem, ferramentas, pró-labore mínimo) | R$ 3.000 |
| Custo variável por oficina (servidor, envio de mensagens, suporte) | R$ 10 |
| Taxa hipotética de tributos sobre a receita | 10% |
| Horizonte | 1 mês (resultado operacional mensal) |

A equipe estima que um preço mais alto atrai menos oficinas. Essa relação entre preço e demanda é **premissa da equipe**, não é calculada pela aplicação.

| Proposta | Preço | Oficinas estimadas |
|---|---|---|
| A (básico) | R$ 40 | 130 |
| B (referência da atividade) | R$ 50 | 100 |
| C (completo) | R$ 70 | 80 |

---

## 3. Solução calculada (resumo)

| Indicador | A (R$ 40) | B (R$ 50) | C (R$ 70) |
|---|---|---|---|
| Resultado mensal | 380,00 | 500,00 | 1.240,00 |
| Margem | 7,31% | 10,00% | 22,14% |
| Equilíbrio (oficinas) | 116 | 86 | 57 |
| Folga | 14 | 14 | 23 |

A coluna B reproduz o teste de referência da atividade: resultado R$ 500, margem 10% e equilíbrio de 86 clientes.
**Caso de validação:** ao preço de R$ 11, a contribuição unitária é −0,10, e por isso não há equilíbrio.

Fórmulas e detalhes: ver `modelo_calculos.md`.

---

## 4. Produto mínimo viável (exigido)

1. Tela com custo fixo, custo variável, taxa (%), benefício percebido (texto) e, para cada proposta, preço e número de oficinas.
2. Botão "Carregar exemplo" com os dados acima.
3. Tabela comparativa das propostas com os indicadores.
4. Gráfico de resultado por quantidade de oficinas, com uma linha por preço. O equilíbrio aparece onde a linha cruza o zero.
5. Validações: campo vazio, margem só com receita positiva, contribuição unitária ≤ 0.
6. Exportação em CSV ou JSON com as entradas, as premissas e os resultados.

---

## 5. Decisões técnicas do grupo

| Data | Decisão |
|---|---|
| 29/09/2026 | Implementar em **Java 23 + Spring Boot 3 + Maven**, com a tela em HTML/JS servida pelo próprio Spring e testes em JUnit 5. |
