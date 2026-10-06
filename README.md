# simulador-precificacao-saas
Aplicação web para simular preços de um SaaS: calcula receita, tributos, resultado mensal, margem e quantidade mínima de clientes para o equilíbrio, comparando propostas. Projeto acadêmico de Engenharia Econômica – Faculdade SENAI FATESG.

**Dados fictícios – simulação.**

## Objetivo
Apoiar a decisão de **qual preço mensal adotar e qual volume de clientes sustenta um SaaS** (atividade "Desenvolvimento de aplicações de apoio à decisão com agentes de IA", **opção 2 – Simulador de precificação de um SaaS**).

- **Tema fictício:** ChaveCerta, SaaS de gestão para oficinas mecânicas de pequeno porte.
- **Usuário:** Rafael, sócio responsável pela parte comercial.
- **Decisão:** adotar R$ 40, R$ 50 ou R$ 70 por mês e verificar se o número de oficinas previsto cobre os custos.
- **Pergunta respondida:** com o volume esperado, a receita cobre os custos? A partir de quantas oficinas o negócio deixa de dar prejuízo?

## Integrantes
- Gubio Garcia dos Santos
- Luiz Fernando de Pádua Paixão

Docente: Msc. Ujeverson Tavares Sampaio – Engenharia Econômica – Engenharia de Software.

## Tecnologia
| Item | Versão | Uso |
|---|---|---|
| Java (JDK) | 23 | linguagem do servidor e dos cálculos |
| Spring Boot | 3.5.16 | servidor web; somente `spring-boot-starter-web` e `spring-boot-starter-test` |
| Maven | 3.9 | compilação, execução e testes |
| JUnit 5 | (incluído no Spring Boot) | testes automatizados |
| HTML, CSS e JavaScript | – | tela, sem framework |
| Chart.js | 4.4.1 (via CDN) | gráfico |

Não há banco de dados, serviços pagos nem integração de IA no produto. A IA foi usada apenas no desenvolvimento (ver [registro_ia.md](registro_ia.md)).

## Instalação
1. Instale o **JDK 23** e confira com `java -version`.
2. Instale o **Maven 3.9+** e confira com `mvn -v`.
3. Clone o repositório:
   ```bash
   git clone https://github.com/Luizdoufera/simulador-precificacao-saas.git
   cd simulador-precificacao-saas
   ```
Na primeira execução o Maven baixa as dependências da internet, o que leva alguns minutos.

## Execução
```bash
mvn spring-boot:run
```
Aguarde a mensagem `Started ChaveCertaApplication` e abra **http://localhost:8080** no navegador. Para parar, use `Ctrl + C` no terminal.

Se aparecer `Port 8080 was already in use`, outro processo está usando a porta. No PowerShell, libere-a com:
```powershell
Get-NetTCPConnection -LocalPort 8080 -State Listen | % { Stop-Process -Id $_.OwningProcess -Force }
```

## Testes
```bash
mvn test
```
O resultado esperado é `Tests run: 39, Failures: 0, Errors: 0, Skipped: 0` e `BUILD SUCCESS`.

| Classe de teste | Qtd. | O que verifica |
|---|---|---|
| `CalculadoraPrecificacaoTest` | 15 | fórmulas; casos T1, T2, T4–T8 do `modelo_calculos.md`; exemplo A/B/C; identidade de conferência; dados do gráfico |
| `ValidadorTest` | 10 | T3 (campo vazio) e demais validações: não numérico, negativo, taxa, oficinas inteiras |
| `InterpretacaoTest` | 6 | texto da interpretação, caso sem proposta viável, empate e formatação |
| `ExportadorCsvTest` | 4 | conteúdo do CSV, casos especiais e escape de texto |
| `SimulacaoControllerTest` | 4 | API `/api/simular` e `/api/exportar-csv`, com entrada válida e inválida |

Os valores de referência dos testes estão na seção 8 do [modelo_calculos.md](modelo_calculos.md).

**Executado automaticamente:** os 39 testes acima.
**Depende de conferência manual:** a aparência da tela, o desenho do gráfico (que depende do Chart.js no navegador) e a abertura do CSV em planilha.

## Dados de exemplo
O botão **Carregar exemplo** preenche o caso da atividade (fonte: [contexto_atividade.md](contexto_atividade.md)):

| Premissa | Valor |
|---|---|
| Custo fixo mensal | R$ 3.000 |
| Custo variável por oficina | R$ 10 |
| Taxa hipotética de tributos sobre a receita | 10% |
| Horizonte | 1 mês |

| Proposta | Preço mensal | Oficinas estimadas (premissa da equipe) |
|---|---|---|
| A | R$ 40 | 130 |
| B (referência da atividade) | R$ 50 | 100 |
| C | R$ 70 | 80 |

## Exemplo de uso
1. Abra http://localhost:8080 e clique em **Carregar exemplo**.
2. Clique em **Calcular**. A tela mostra:

   | Indicador | A | B | C |
   |---|---|---|---|
   | Resultado mensal | R$ 380,00 | R$ 500,00 | R$ 1.240,00 |
   | Margem | 7,31% | 10,00% | 22,14% |
   | Equilíbrio | 116 oficinas | 86 oficinas | 57 oficinas |
   | Folga | 14 | 14 | 23 |

   > *Interpretação:* Nas premissas consideradas, a proposta C (R$ 70) apresenta o maior resultado mensal, R$ 1.240,00. Folga sobre o equilíbrio: A 14 oficinas; B 14 oficinas; C 23 oficinas. Essa conclusão depende de atingir ao menos 57 oficinas ao preço de R$ 70.

3. **Altere uma premissa e clique em Calcular de novo.** Exemplo: mude as oficinas da proposta C de 80 para 50. O resultado de C passa a ser 53 × 50 − 3.000 = **−R$ 350,00**, porque fica abaixo do equilíbrio de 57, e a interpretação passa a indicar a proposta B.
4. Clique em **Exportar CSV** para baixar `simulacao_chavecerta.csv` com as premissas, os resultados e a interpretação.
5. Para ver as validações, apague o custo fixo e clique em Calcular. Aparece "Informe o custo fixo mensal" e nenhum resultado é exibido.

Digite os números sem separador de milhar (`3000`, e não `3.000`). A vírgula decimal é aceita (`49,90`).

## Indicadores
Todos os valores são **mensais**, em reais. A taxa é digitada em % e convertida em fração no cálculo (10% → 0,10). Fórmulas completas e casos especiais: [modelo_calculos.md](modelo_calculos.md).

| Indicador | Fórmula | Significado |
|---|---|---|
| Receita | preço × oficinas | quanto entra por mês |
| Tributos | receita × taxa | taxa **hipotética** e editável, não uma alíquota legal |
| Custo variável total | custo variável × oficinas | custo que cresce com cada cliente |
| Resultado mensal | receita − custo fixo − custo variável total − tributos | saldo operacional do mês: positivo = sobra, negativo = falta. **Não é lucro contábil.** |
| Margem | 100 × resultado ÷ receita | fração da receita que sobra; calculada só com receita > 0 (senão "não se aplica") |
| Contribuição unitária | preço × (1 − taxa) − custo variável | quanto cada oficina deixa para pagar o custo fixo |
| Equilíbrio | teto(custo fixo ÷ contribuição unitária) | menor número inteiro de oficinas sem prejuízo; não existe se a contribuição for ≤ 0 |
| Folga | oficinas estimadas − equilíbrio | quantas oficinas a proposta pode perder antes do prejuízo; negativa = abaixo do equilíbrio |

**Interpretação:** entre as propostas com resultado ≥ 0, indica a de maior resultado mensal, informa a folga de cada uma e a condição que mudaria a conclusão. Se todas forem negativas, informa que nenhuma cobre os custos nas premissas consideradas.

## Limitações
**Do modelo econômico** (ver [modelo_calculos.md](modelo_calculos.md), seção 4):
- Horizonte de **um mês típico**: não há projeção mês a mês, desconto no tempo nem recuperação de investimento. O equilíbrio é operacional, do período.
- **A quantidade de oficinas em cada preço é uma estimativa da equipe.** A aplicação não calcula a demanda, apenas mostra o efeito da estimativa. É a premissa que mais influencia a decisão.
- Custo variável por oficina constante, sem ganho de escala.
- Não considera inadimplência, cancelamentos (churn) nem custo de aquisição de clientes.
- A taxa de tributos é hipotética, com base didática declarada pela equipe.

**Da aplicação:**
- Exatamente três propostas (A, B e C).
- Não há análise de sensibilidade automática (por exemplo, o preço que zera o resultado), nem comparação de cenários pessimista/base/otimista.
- O gráfico precisa de internet, porque o Chart.js vem de CDN. Sem conexão, a tabela e a interpretação continuam funcionando.
- Números sem separador de milhar.
- Os dados não são salvos: ao recarregar a página, o formulário volta vazio.

## API
| Método | Caminho | Retorno |
|---|---|---|
| POST | `/api/simular` | JSON com resultados, gráfico e interpretação (200) ou erros (400) |
| POST | `/api/exportar-csv` | arquivo `simulacao_chavecerta.csv` (200) ou erros em JSON (400) |

## Estrutura
```
src/main/java/br/senai/chavecerta/
├── ChaveCertaApplication.java   inicia o servidor
├── calculo/                     núcleo em Java puro, sem Spring: fórmulas, validação,
│                                interpretação, gráfico e CSV
└── web/                         controlador REST (recebe a tela e chama o núcleo)
src/main/resources/static/       tela: index.html, app.js (sem cálculos), estilo.css
src/test/java/                   testes JUnit
docs/                            enunciado da atividade e documento do grupo
```

## Documentação
- [contexto_atividade.md](contexto_atividade.md): situação-problema e dados do caso
- [requisitos.md](requisitos.md): requisitos funcionais e não funcionais, com critérios de aceitação
- [modelo_calculos.md](modelo_calculos.md): variáveis, fórmulas, convenções, caso resolvido e valores de referência
- [registro_ia.md](registro_ia.md): registro das interações com IA
- [docs/](docs/): enunciado da atividade e documento de delimitação do grupo
