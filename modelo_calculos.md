# Modelo de Cálculos – Simulador de Precificação SaaS (ChaveCerta)

**Opção 2 – Simulador de precificação de um SaaS**
**Natureza do resultado:** saldo operacional mensal de um modelo didático, **não** é lucro contábil.

---

## 1. Convenções gerais

| Convenção | Definição |
|---|---|
| Moeda | Reais (R$) |
| Unidade de tempo | Mês. Todas as entradas e saídas são **mensais**; não misturar valores anuais. |
| Horizonte | 1 mês típico de operação (não há mês zero nem desconto no tempo) |
| Taxa | Digitada em **%** na tela; convertida em **fração** no cálculo (`taxa = taxaPercentual / 100`) |
| Tributos | Taxa **hipotética** e editável sobre a receita. Base declarada: premissa didática da equipe. Não é alíquota legal. |
| Sinais | Receita positiva; custos e tributos são **subtraídos**. Resultado positivo = sobra; negativo = falta. |
| Cliente | 1 cliente = 1 oficina assinante |
| Registro de custos | Cada custo aparece uma única vez: ou no fixo, ou no variável, nunca nos dois. |

---

## 2. Variáveis

### 2.1 Entradas (valores fornecidos pelo usuário)

| Variável | Nome no código | Unidade | Validação |
|---|---|---|---|
| Custo fixo mensal | `custoFixo` | R$/mês | obrigatório, ≥ 0 |
| Custo variável por cliente | `custoVariavel` | R$/cliente/mês | obrigatório, ≥ 0 |
| Taxa de tributos | `taxaPercentual` | % sobre a receita | obrigatório, 0 ≤ valor < 100 |
| Preço mensal | `preco` | R$/cliente/mês | obrigatório, ≥ 0 |
| Quantidade de clientes | `clientes` | oficinas | obrigatório, inteiro ≥ 0 |
| Benefício percebido | `beneficio` | texto | opcional; não entra no cálculo |

Preço e clientes são informados para **cada proposta** (A, B e C). Custo fixo, custo variável e taxa são **comuns** às três.

### 2.2 Saídas (valores calculados)

| Variável | Nome no código | Unidade |
|---|---|---|
| Receita | `receita` | R$/mês |
| Tributos | `tributos` | R$/mês |
| Custo variável total | `custoVariavelTotal` | R$/mês |
| Resultado mensal | `resultado` | R$/mês |
| Margem percentual | `margem` | % |
| Contribuição unitária | `contribuicaoUnitaria` | R$/cliente/mês |
| Equilíbrio | `equilibrio` | oficinas (inteiro) |
| Folga | `folga` | oficinas |

### 2.3 Valores estimados (premissas da equipe)

A **quantidade de clientes em cada preço** é uma estimativa. A aplicação não calcula a demanda; ela apenas mostra o efeito da estimativa. Essa é a premissa que mais influencia a decisão e deve ser justificada no relatório.

---

## 3. Fórmulas

```
taxa                 = taxaPercentual / 100
receita              = preco × clientes
tributos             = receita × taxa
custoVariavelTotal   = custoVariavel × clientes
resultado            = receita − custoFixo − custoVariavelTotal − tributos
contribuicaoUnitaria = preco × (1 − taxa) − custoVariavel
margem               = 100 × resultado / receita            (somente se receita > 0)
equilibrio           = teto(custoFixo / contribuicaoUnitaria) (somente se contribuicaoUnitaria > 0)
folga                = clientes − equilibrio
```

### 3.1 Identidade de conferência

Rearranjando a fórmula do resultado:

```
resultado = contribuicaoUnitaria × clientes − custoFixo
```

As duas formas **devem dar o mesmo valor** (tolerância de R$ 0,01). Essa identidade serve como teste de integridade do código e é a equação da reta do gráfico (RF07): a inclinação é a contribuição unitária e o ponto de partida (0 clientes) é −custo fixo.

---

## 4. Regras de validade e casos especiais

| Situação | Regra | O que mostrar |
|---|---|---|
| Campo obrigatório vazio ou não numérico | Não calcular nada | Mensagem junto ao campo, ex.: "Informe o custo fixo mensal" |
| Valor negativo | Não calcular nada | "O valor não pode ser negativo" |
| Taxa < 0 ou ≥ 100 | Não calcular nada | "A taxa deve estar entre 0% e 99,99%" |
| Clientes não inteiro | Não calcular nada | "Informe um número inteiro de oficinas" |
| Receita = 0 (preço 0 ou 0 clientes) | Calcular resultado; **não** calcular margem | Margem: "não se aplica" |
| Contribuição unitária ≤ 0 (qualquer custo fixo, inclusive 0) | Não calcular equilíbrio nem folga | "Sem equilíbrio: cada oficina aumenta o prejuízo" |
| Custo fixo = 0 e contribuição > 0 | Equilíbrio = 0 | "Equilíbrio: 0 oficinas" |
| Resultado negativo com receita > 0 | Margem negativa é válida | Exibir normalmente (ex.: −5,00%) |

**Limites do modelo:**
- O custo variável por cliente é constante, sem ganho de escala.
- O preço não altera a demanda automaticamente; a relação vem da premissa da equipe.
- Não há inadimplência, cancelamentos (churn), custo de aquisição de clientes nem evolução mês a mês.
- Equilíbrio operacional do mês **não** é recuperação de investimento (isso é da opção 3).

---

## 5. Precisão e arredondamento

- Calcular sempre com o valor completo; **arredondar apenas na exibição** (R$ com 2 casas; margem com 2 casas).
- Comparar valores monetários nos testes com **tolerância de R$ 0,01**: `Math.abs(obtido − esperado) <= 0.01`.
- Equilíbrio: menor inteiro maior ou igual ao resultado da divisão.
- **Cuidado com `double` em Java:** operações com decimais podem gerar resíduos (ex.: 75,00000000001 viraria 76 no teto). Arredondar o quociente para 6 casas antes do teto:

```java
double bruto = custoFixo / contribuicaoUnitaria;
double arredondado = BigDecimal.valueOf(bruto).setScale(6, RoundingMode.HALF_UP).doubleValue();
int equilibrio = (int) Math.ceil(arredondado);
```

- Nos testes JUnit, usar `assertEquals(esperado, obtido, 0.01)` para valores monetários.

---

## 6. Caso resolvido passo a passo (referência da atividade)

**Valores fornecidos:** custo fixo R$ 3.000; custo variável R$ 10; taxa 10%; preço R$ 50; 100 clientes.

| Passo | Conta | Valor |
|---|---|---|
| 1. Taxa em fração | 10 ÷ 100 | 0,10 |
| 2. Receita | 50 × 100 | R$ 5.000,00 |
| 3. Tributos | 5.000 × 0,10 | R$ 500,00 |
| 4. Custo variável total | 10 × 100 | R$ 1.000,00 |
| 5. Resultado | 5.000 − 3.000 − 1.000 − 500 | **R$ 500,00** |
| 6. Margem | 100 × 500 ÷ 5.000 | **10,00%** |
| 7. Contribuição unitária | 50 × (1 − 0,10) − 10 = 45 − 10 | R$ 35,00 |
| 8. Equilíbrio (bruto) | 3.000 ÷ 35 | 85,71 |
| 9. Equilíbrio (inteiro) | teto(85,71) | **86 oficinas** |
| 10. Conferência (identidade) | 35 × 100 − 3.000 | R$ 500,00 ✔ |
| 11. Folga | 100 − 86 | 14 oficinas |

**Prova do equilíbrio:** com 85 oficinas o resultado é 35 × 85 − 3.000 = **−R$ 25,00**; com 86 é 35 × 86 − 3.000 = **+R$ 10,00**. Portanto 86 é de fato o primeiro volume sem prejuízo.

**Comparação com o enunciado:** resultado R$ 500, margem 10% e equilíbrio 86 clientes. **Sem divergência.**

---

## 7. Caso completo do exemplo (três propostas)

Premissas comuns: custo fixo R$ 3.000; custo variável R$ 10; taxa 10%.

| Indicador | A | B | C |
|---|---|---|---|
| Preço (fornecido) | R$ 40 | R$ 50 | R$ 70 |
| Clientes (estimado) | 130 | 100 | 80 |
| Receita | 5.200,00 | 5.000,00 | 5.600,00 |
| Tributos | 520,00 | 500,00 | 560,00 |
| Custo variável total | 1.300,00 | 1.000,00 | 800,00 |
| Resultado | 380,00 | 500,00 | 1.240,00 |
| Margem | 7,31% | 10,00% | 22,14% |
| Contribuição unitária | 26,00 | 35,00 | 53,00 |
| Equilíbrio (bruto) | 115,38 | 85,71 | 56,60 |
| Equilíbrio | 116 | 86 | 57 |
| Folga | 14 | 14 | 23 |
| Conferência: contribuição × clientes − fixo | 380,00 ✔ | 500,00 ✔ | 1.240,00 ✔ |

---

## 8. Tabela de valores de referência para os testes

Todos com custo fixo R$ 3.000 e custo variável R$ 10. Valores conferidos de forma independente (calculadora e script separado da aplicação).

| # | Tipo | Taxa | Preço | Clientes | Receita | Tributos | Resultado | Margem | Contrib. | Equilíbrio |
|---|---|---|---|---|---|---|---|---|---|---|
| T1 | Normal (referência) | 10% | 50 | 100 | 5.000,00 | 500,00 | 500,00 | 10,00% | 35,00 | 86 |
| T2 | Limite: taxa zero | 0% | 50 | 100 | 5.000,00 | 0,00 | 1.000,00 | 20,00% | 40,00 | 75 |
| T3 | Entrada inválida | — | — | — | — | — | — | — | — | erro "Informe o custo fixo mensal" |
| T4 | Desfavorável | 10% | 11 | 100 | 1.100,00 | 110,00 | −3.010,00 | −273,64% | −0,10 | sem equilíbrio |
| T5 | Alteração de premissa | 10% | 50 | 80 | 4.000,00 | 400,00 | −200,00 | −5,00% | 35,00 | 86 |
| T6 | Receita zero | 10% | 50 | 0 | 0,00 | 0,00 | −3.000,00 | não se aplica | 35,00 | 86 |
| T7 | Borda abaixo | 10% | 50 | 85 | 4.250,00 | 425,00 | −25,00 | −0,59% | 35,00 | 86 |
| T8 | Borda no equilíbrio | 10% | 50 | 86 | 4.300,00 | 430,00 | 10,00 | 0,23% | 35,00 | 86 |

**Observações:**
- T2 tem divisão exata (3.000 ÷ 40 = 75): o equilíbrio deve ser **75**, e não 76. Esse teste pega o erro de arredondamento descrito na seção 5.
- T7 e T8 confirmam que o teto está correto: o resultado muda de sinal exatamente entre 85 e 86.
- Os testes automatizados mínimos (RNF05) podem ser T1, T2 e T4.

---

## 9. Regra da interpretação (RF08)

1. Considerar apenas propostas **válidas** (sem erro de entrada).
2. Entre as que têm resultado ≥ 0, indicar a de **maior resultado**.
3. Para cada proposta, informar a folga (clientes estimados − equilíbrio). Folga negativa: "abaixo do equilíbrio".
4. Se todas tiverem resultado < 0: "Nenhuma proposta cobre os custos nas premissas consideradas."
5. Sempre terminar com a condição que muda a decisão: "Essa conclusão depende de atingir ao menos [equilíbrio] oficinas ao preço de R$ [preço]."
6. Não usar frases de certeza sobre o futuro ("vai lucrar", "com certeza").

**Texto esperado com o exemplo carregado:**
> Nas premissas consideradas, a proposta C (R$ 70) apresenta o maior resultado mensal, R$ 1.240,00. Folga sobre o equilíbrio: A 14 oficinas; B 14 oficinas; C 23 oficinas. Essa conclusão depende de atingir ao menos 57 oficinas ao preço de R$ 70.
