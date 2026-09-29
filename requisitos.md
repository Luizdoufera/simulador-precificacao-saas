# Requisitos – Simulador de Precificação SaaS (ChaveCerta)

**Opção 2 – Simulador de precificação de um SaaS**
**Decisão apoiada:** qual preço mensal e qual volume de oficinas sustentam o SaaS de gestão de oficinas mecânicas.
**Usuário:** sócio responsável pela parte comercial.
**Dados:** fictícios, identificados como simulação. Moeda em reais (R$). Todos os valores são **mensais**.

---

## 1. Requisitos obrigatórios (versão 1)

### Entradas

**RF01 – Premissas gerais**
A tela deve ter os campos: custo fixo mensal (R$), custo variável por oficina (R$/mês), taxa hipotética de tributos (% sobre a receita) e benefício percebido pelo cliente (texto). Cada campo mostra sua unidade.

- **Critério:** Dado que o usuário abre a aplicação, quando visualiza o formulário, então os quatro campos aparecem com rótulos em português e unidades visíveis.

**RF02 – Três propostas de preço**
A tela deve permitir informar três propostas (A, B e C), cada uma com um preço mensal (R$) e uma quantidade estimada de oficinas.

- **Critério:** Dado que o usuário preenche preço e oficinas das três propostas, quando clica em "Calcular", então as três aparecem na comparação.

**RF03 – Carregar exemplo**
Um botão "Carregar exemplo" preenche o formulário com o caso de referência.

- **Critério:** Dado que o usuário clica em "Carregar exemplo", então os campos recebem: custo fixo 3.000; variável 10; taxa 10%; A = R$ 40 / 130 oficinas; B = R$ 50 / 100 oficinas; C = R$ 70 / 80 oficinas.

### Cálculos

**RF04 – Indicadores por proposta**
Para cada proposta, calcular:
- receita = preço × oficinas
- tributos = receita × taxa
- custo variável total = custo variável × oficinas
- resultado = receita − custo fixo − custo variável total − tributos
- margem (%) = 100 × resultado ÷ receita
- contribuição unitária = preço × (1 − taxa) − custo variável

- **Critério:** Dado custo fixo de 3.000, variável de 10, taxa de 10%, preço de 50 e 100 oficinas, quando o usuário calcula, então a aplicação mostra receita R$ 5.000,00, tributos R$ 500,00, resultado R$ 500,00 e margem 10,00%.

**RF05 – Ponto de equilíbrio**
Calcular equilíbrio = menor inteiro ≥ (custo fixo ÷ contribuição unitária).

- **Critério:** Com os dados do RF04, a aplicação mostra "Equilíbrio: 86 oficinas".

### Resultados

**RF06 – Tabela comparativa**
Exibir uma tabela com as três propostas lado a lado e todos os indicadores do RF04 e RF05.

- **Critério:** Após carregar o exemplo e calcular, a tabela mostra os resultados A = R$ 380,00, B = R$ 500,00 e C = R$ 1.240,00.

**RF07 – Gráfico de resultado por quantidade de oficinas**
Gráfico de linhas com o resultado mensal (eixo Y) pela quantidade de oficinas (eixo X, de 0 até o dobro do maior volume informado), com uma linha por proposta e uma linha de referência em zero.

- **Critério:** Após calcular, o gráfico mostra três linhas, e cada uma cruza o zero próximo ao ponto de equilíbrio da sua proposta.

**RF08 – Interpretação**
Exibir um texto gerado por regra, com os valores calculados:
- indicar a proposta de maior resultado entre as que têm resultado ≥ 0;
- informar a folga de cada uma das três propostas (oficinas estimadas − equilíbrio);
- se todas tiverem resultado negativo, informar que nenhuma cobre os custos nas premissas consideradas;
- lembrar que a conclusão depende da estimativa de oficinas em cada preço.

- **Critério:** Com o exemplo carregado, o texto indica a proposta C (R$ 1.240,00), informa as folgas A = 14, B = 14 e C = 23, cita que a conclusão depende de ao menos 57 oficinas e não usa frases de certeza sobre o futuro.

### Validações

**RF09 – Campos inválidos**
Campos vazios ou não numéricos, valores negativos, quantidade de oficinas não inteira ou taxa fora do intervalo de 0% a 99,99% geram uma mensagem próxima ao campo, e nenhum resultado é calculado.

- **Critério:** Dado o campo "custo fixo" vazio, quando o usuário clica em "Calcular", então aparece a mensagem "Informe o custo fixo mensal" e a tabela não é exibida.

**RF10 – Contribuição unitária não positiva**
Se a contribuição unitária for ≤ 0, informar que não há equilíbrio por aumento de volume, sem mostrar número de oficinas (vale também com custo fixo = 0, para evitar a divisão 0 ÷ 0).

- **Critério:** Dado preço de R$ 11 (custo fixo 3.000, variável 10, taxa 10%), quando o usuário calcula, então o equilíbrio aparece como "Sem equilíbrio: cada oficina aumenta o prejuízo".

**RF11 – Receita zero**
A margem só é calculada com receita positiva.

- **Critério:** Dado 0 oficinas, quando o usuário calcula, então o resultado é −R$ 3.000,00 e a margem aparece como "não se aplica", sem erro na tela.

### Exportação e identificação

**RF12 – Exportar CSV**
Um botão "Exportar CSV" gera um arquivo com as entradas, as premissas e os resultados das três propostas.

- **Critério:** Após calcular, ao clicar em "Exportar CSV", é baixado um arquivo que, aberto em planilha, contém os valores exibidos na tabela.

**RF13 – Identificação e explicação**
A tela mostra o nome do grupo e dos integrantes, a indicação "Dados fictícios – simulação" e uma breve explicação de cada indicador.

- **Critério:** Ao abrir a aplicação, essas informações estão visíveis sem necessidade de calcular.

---

## 2. Requisitos não funcionais

- **RNF01** – Aplicação web em Java 23 + Spring Boot 3 (Maven), executável localmente com `mvn spring-boot:run` e acessada em `http://localhost:8080`, sem serviços pagos nem banco de dados.
- **RNF02** – Cálculos em classes Java separadas da interface e do controlador (pacote `calculo`, sem dependência do Spring), em métodos que recebem dados e retornam resultados.
- **RNF03** – Taxa informada em % na tela e convertida em fração no cálculo.
- **RNF04** – Valores monetários comparados com tolerância de R$ 0,01; arredondamento apenas na apresentação.
- **RNF05** – Pelo menos três testes automatizados do núcleo de cálculo em JUnit 5, executados com `mvn test`.
- **RNF06** – As validações do RF09 são feitas no servidor (Java), para poderem ser testadas; a tela apenas exibe as mensagens retornadas.

---

## 3. Casos de teste mínimos

| # | Tipo | Entrada | Resultado esperado |
|---|---|---|---|
| T1 | Normal (referência) | Fixo 3.000; var. 10; taxa 10%; preço 50; 100 oficinas | Receita 5.000; tributos 500; resultado 500; margem 10%; equilíbrio 86 |
| T2 | Limite (taxa zero) | Fixo 3.000; var. 10; taxa 0%; preço 50; 100 oficinas | Resultado 1.000; margem 20%; equilíbrio 75 |
| T3 | Entrada inválida | Custo fixo vazio | Mensagem de erro; nenhum resultado |
| T4 | Cenário desfavorável | Fixo 3.000; var. 10; taxa 10%; preço 11; 100 oficinas | Contribuição −0,10; "sem equilíbrio" |
| T5 | Alteração de premissa | T1 com 80 oficinas | Resultado −200 (receita 4.000; tributos 400; var. 800); margem −5% |

A tabela completa (T1 a T8, com receita zero e as bordas do equilíbrio) está em `modelo_calculos.md`, seção 8. Os testes JUnit devem cobrir **T1 a T8**, pois todos são baratos de automatizar.

---

## 4. Extensões opcionais (versão 2)

- **EXT01** – Calcular automaticamente o preço que zera o resultado para um volume informado (análise de sensibilidade).
- **EXT02** – Exportação também em JSON.
- **EXT03** – Gráfico de barras comparando o resultado das três propostas.
- **EXT04** – Número livre de propostas (adicionar e remover).
- **EXT05** – Planos por faixa (básico, completo) com mix de clientes em cada plano.
- **EXT06** – Projeção de 12 meses com crescimento e cancelamento (churn) de oficinas.
- **EXT07** – Salvar e reabrir simulações.
