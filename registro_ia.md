# Registro de Interações com IA

**Projeto:** ChaveCerta – Simulador de Precificação SaaS (opção 2)
**Grupo:** Gubio Garcia dos Santos e Luiz Fernando de Pádua Paixão
**Ferramenta:** Claude Code (agente de IA), extensão do VS Code
**Formato:** conforme o enunciado, cada entrada traz data, integrante responsável, objetivo, prompt utilizado, resposta da IA, arquivos alterados, verificação realizada e decisão humana. Os prompts estão resumidos e com a redação corrigida, mantendo o pedido original.

## Resumo: interações e commits

| Nº | Data | Etapa | Commits |
|---|---|---|---|
| 01 | 29/09/2026 | Análise do projeto e dos documentos | – |
| 02 | 29/09/2026 | Contexto, registro e escolha da tecnologia | `f482805` |
| 03 | 29/09/2026 | Repositório Git e esqueleto Spring Boot (Passo 4) | `dab4aef` |
| 04 | 29/09/2026 | Núcleo de cálculo e validação (Passo 5) | `c9fd180`, `d9588dc` |
| 05 | 29/09/2026 | Correção: erro ao executar o projeto | – |
| 06 | 29/09/2026 | API e interface (Passo 6) | `08849e9`, `7366ec8` |
| 07 | 29/09/2026 | Gráfico, interpretação e exportação CSV | `733c672`, `a95a174`, `6281aa4`, `fd8e80d`, `17b1267`, `a982d22` |

## Sugestões da IA revisadas, corrigidas ou rejeitadas

| Entrada | Sugestão ou resultado da IA | Decisão da equipe | Justificativa |
|---|---|---|---|
| 02 | Recomendou Java desktop com Swing (sem frameworks) | **Rejeitada:** a equipe escolheu Java web com Spring Boot | Manter uma aplicação web acessada pelo navegador, como a tela já planejada nos requisitos |
| 01/02 | Apontou divergências entre `requisitos.md` e `modelo_calculos.md` (limite da taxa; custo fixo 0 com contribuição ≤ 0; folgas na interpretação) | **Aceita com revisão:** documentos corrigidos antes do código | O modelo de cálculos é a fonte oficial; o RF09 foi alinhado a ele |
| 05 | Servidor de teste da IA deixado ligado, ocupando a porta 8080 | **Corrigido** após o erro relatado pela equipe | A IA passou a desligar o servidor após cada verificação |
| 07 | Primeira versão do gráfico apareceu com as linhas presas em x = 0 | **Corrigida:** animação do Chart.js desligada | Conferido por captura de tela: as linhas cruzam o zero em 57, 86 e 116 |
| 07 | Teste do CSV falhou | **Revisado:** o arquivo estava certo e o teste foi ajustado | A interpretação contém ";" e, pela regra do CSV, vai entre aspas |

---

## Passos 1 a 3 (delimitação, requisitos e modelo de cálculos)

Feitos antes deste registro. Os documentos resultantes são `docs/Atividade_ Aplicações_de_Engenharia_Economica_GL.pdf`, `requisitos.md` e `modelo_calculos.md`. O caso de referência (fixo 3.000, variável 10, taxa 10%, preço 50, 100 clientes → resultado 500, margem 10%, equilíbrio 86) foi resolvido pela equipe no documento do grupo, independentemente do agente.

---

### 01 – 29/09/2026 – Análise do projeto e dos documentos
- **Integrante:** Luiz Fernando
- **Objetivo:** contextualizar o agente antes de implementar.
- **Prompt:** "Leia todos os arquivos do projeto (instruções do agente, requisitos, modelo de cálculos e documento do grupo) para se contextualizar antes de iniciarmos o desenvolvimento."
- **Resposta da IA:** leu `CLAUDE.md`, `requisitos.md`, `modelo_calculos.md` e o PDF do grupo; conferiu os valores de referência (margens, equilíbrios e casos T4, T7 e T8); apontou cinco divergências:
  - arquivos citados que não existiam;
  - limite da taxa diferente entre o RF09 e o modelo;
  - caso de custo fixo 0 com contribuição ≤ 0 não coberto;
  - texto esperado da interpretação sem as folgas de todas as propostas;
  - conjuntos de testes T1–T5 e T1–T8 diferentes entre os dois documentos.
- **Arquivos alterados:** nenhum.
- **Verificação:** a equipe comparou as divergências com os documentos.
- **Decisão humana:** corrigir as divergências na etapa seguinte.

### 02 – 29/09/2026 – Contexto, registro e escolha da tecnologia
- **Integrante:** Luiz Fernando
- **Objetivo:** organizar a documentação de apoio e definir a tecnologia.
- **Prompt:** "Adicione ao projeto um documento com o contexto da atividade e o arquivo registro_ia.md para registrar as interações com a IA. Decidimos desenvolver a aplicação em Java: sugira como iniciar e corrija nos documentos o que for necessário para essa mudança."
- **Resposta da IA:** perguntou qual tipo de projeto Java seria feito e recomendou Swing. Após a escolha da equipe, criou `contexto_atividade.md` e `registro_ia.md`, adaptou `CLAUDE.md` e os RNF para Java/Spring Boot, corrigiu RF08, RF09 e RF10 e o modelo de cálculos (arredondamento do equilíbrio em Java com `BigDecimal`), e propôs a estrutura do projeto em etapas.
- **Arquivos alterados:** `CLAUDE.md`, `requisitos.md`, `modelo_calculos.md`; criados `contexto_atividade.md` e `registro_ia.md`.
- **Verificação:** a equipe revisou as correções e a estrutura proposta.
- **Decisão humana:** **rejeitou a sugestão de Swing** e escolheu Java web com Spring Boot; aprovou a estrutura.
- **Commit:** `f482805` – Documentação inicial do projeto.

### 03 – 29/09/2026 – Repositório Git e esqueleto Spring Boot (Passo 4)
- **Integrante:** Luiz Fernando
- **Objetivo:** publicar o projeto no GitHub com evolução gradual e criar um exemplo mínimo executável.
- **Prompt:** "O repositório no GitHub (github.com/Luizdoufera/simulador-precificacao-saas) deve mostrar a evolução do projeto em etapas, e não um único envio. Prossiga com o Passo 4 de forma incremental. Padrão das mensagens de commit: nome, data e descrição breve da implementação."
- **Resposta da IA:** ligou a pasta ao repositório, que já tinha o README criado pelo GitHub; fez o commit da documentação e criou o esqueleto: `pom.xml` (Spring Boot 3.5.16, Java 23), classe principal, página provisória, `.gitignore` e README com instruções.
- **Arquivos alterados:** `pom.xml`, `ChaveCertaApplication.java`, `application.properties`, `static/index.html`, `.gitignore`, `README.md`.
- **Verificação:**
  - `mvn -q -B package -DskipTests` terminou com código 0;
  - o servidor iniciou ("Tomcat started on port 8080");
  - `http://localhost:8080` respondeu HTTP 200.
- **Decisão humana:** aceito; seguir para o núcleo de cálculo.
- **Commit:** `dab4aef` – Esqueleto do projeto Spring Boot.

### 04 – 29/09/2026 – Núcleo de cálculo e validação (Passo 5)
- **Integrante:** Luiz Fernando
- **Objetivo:** implementar as fórmulas e as validações em funções testáveis, separadas da tela.
- **Prompt:** "Implemente a etapa 4b (núcleo de cálculo e validação das entradas) em dois commits e, ao final, demonstre o resultado para avaliarmos."
- **Resposta da IA:** criou o pacote `calculo` em Java puro.
  - Commit 1: `Premissas`, `Proposta`, `ResultadoProposta` e `CalculadoraPrecificacao`, com as fórmulas do `modelo_calculos.md` e o teto do equilíbrio arredondado a 6 casas.
  - Commit 2: `EntradaSimulacao`, `ErroValidacao` e `Validador`, com as regras do RF09.
  - Demonstrou o núcleo com os dados do exemplo.
- **Arquivos alterados:** 7 classes em `src/main/java/.../calculo/`; `CalculadoraPrecificacaoTest.java` e `ValidadorTest.java`.
- **Verificação:**
  - `mvn -B test` → `Tests run: 22, Failures: 0, Errors: 0`;
  - a demonstração mostrou A = 380, B = 500 e C = 1.240, e equilíbrios 116, 86 e 57, iguais ao documento do grupo;
  - os casos especiais (preço 11 sem equilíbrio, 0 oficinas com margem "não se aplica") e as mensagens de erro estavam corretos.
- **Decisão humana:** aceito.
- **Commits:** `c9fd180` – Núcleo de cálculo e testes T1, T2, T4-T8; `d9588dc` – Validação das entradas e teste T3.

### 05 – 29/09/2026 – Correção: erro ao executar o projeto
- **Integrante:** Luiz Fernando
- **Objetivo:** corrigir a falha ao rodar `mvn spring-boot:run`.
- **Prompt:** "Mostre como executar o projeto no estado atual." Em seguida: "Ao executar `mvn spring-boot:run`, obtivemos o erro `Failed to execute goal ... spring-boot-maven-plugin:3.5.16:run ... Process terminated with exit code: 1`. Investigue a causa."
- **Resposta da IA:** identificou que a porta 8080 estava ocupada por um servidor que a própria IA havia deixado ligado após a demonstração; encerrou o processo e confirmou a porta livre.
- **Arquivos alterados:** `CLAUDE.md` (o registro de IA passa a ser atualizado sob pedido da equipe).
- **Verificação:** a porta 8080 foi confirmada livre; o projeto passou a iniciar na máquina do integrante.
- **Decisão humana:** a correção foi aceita, e a IA deixou de manter servidores ligados após as verificações.

### 06 – 29/09/2026 – API e interface (Passo 6)
- **Integrante:** Luiz Fernando
- **Objetivo:** conectar os cálculos validados a uma tela.
- **Prompt:** "Prossiga para a próxima etapa (4c): API de simulação e interface com formulário, botão de exemplo e tabela comparativa."
- **Resposta da IA:**
  - criou o endpoint `POST /api/simular`, que valida, calcula e devolve JSON ou erros;
  - criou a tela com premissas, três propostas, botão "Carregar exemplo", tabela comparativa, mensagens de erro junto aos campos, identificação do grupo e explicação dos indicadores;
  - a tela não faz cálculos.
- **Arquivos alterados:** `SimulacaoController.java`, `RespostaSimulacao.java`, `SimulacaoControllerTest.java`, `index.html`, `app.js`, `estilo.css`.
- **Verificação:**
  - `mvn -B test` → 24 testes, 0 falhas;
  - a API retornou HTTP 200 com o exemplo e HTTP 400 com o custo fixo vazio;
  - capturas de tela mostraram a tabela igual ao documento do grupo, os erros embaixo dos campos e os casos especiais.
- **Decisão humana:** aceito; os textos da explicação dos indicadores ficaram para revisão da equipe.
- **Commits:** `08849e9` – API REST de simulação; `7366ec8` – Tela com formulário, exemplo e tabela.

### 07 – 29/09/2026 – Gráfico, interpretação e exportação CSV
- **Integrante:** Luiz Fernando
- **Objetivo:** concluir os requisitos restantes da versão 1 (RF07, RF08 e RF12).
- **Prompt:** "Implemente as etapas restantes da versão 1 (gráfico, interpretação e exportação CSV), com dois commits por tópico e descrição detalhada da implementação em cada commit."
- **Resposta da IA:**
  - **Gráfico:** pontos calculados no núcleo e desenhados com Chart.js.
  - **Interpretação:** regra da seção 9 do modelo, em Java.
  - **CSV:** gerado em Java, com separador ";" e endpoint `/api/exportar-csv`.
- **Problemas encontrados e corrigidos:**
  - o gráfico apareceu vazio na primeira verificação por causa da animação; ela foi desligada;
  - um teste do CSV falhou porque a interpretação contém ";" e vai entre aspas; o arquivo estava correto e o teste foi ajustado.
- **Arquivos alterados:** `Grafico.java`, `Interpretacao.java`, `Formatacao.java`, `ExportadorCsv.java` e testes; `CalculadoraPrecificacao.java`, controlador, `index.html`, `app.js`, `estilo.css`, `README.md`.
- **Verificação:**
  - `mvn -B test` → 39 testes, 0 falhas;
  - as linhas do gráfico cruzam o zero em 57, 86 e 116;
  - o texto da interpretação é igual ao esperado no modelo;
  - o CSV baixado tem os mesmos valores da tabela.
- **Decisão humana:** aceito.
- **Commits:** `733c672`, `a95a174` (gráfico); `6281aa4`, `fd8e80d` (interpretação); `17b1267`, `a982d22` (CSV).
