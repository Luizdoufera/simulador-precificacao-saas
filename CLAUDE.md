# Contexto do projeto para o agente de IA

## Sobre o projeto
Projeto acadêmico de Engenharia Econômica (Faculdade SENAI FATESG, Engenharia de Software).
Atividade: "Desenvolvimento de aplicações de apoio à decisão com agentes de IA".
Opção escolhida: **2 – Simulador de precificação de um SaaS**.

Tema fictício: **ChaveCerta**, SaaS de gestão para oficinas mecânicas de pequeno porte.
Decisão apoiada: qual preço mensal (R$ 40, R$ 50 ou R$ 70) e qual volume de oficinas sustentam o SaaS.
Usuário: Rafael, sócio responsável pela parte comercial.
Grupo: Gubio Garcia dos Santos e Luiz Fernando de Pádua Paixão.

Somos estudantes e precisamos compreender e validar todo o código. Explique as alterações e os arquivos modificados.

## Documentos de referência (leia antes de qualquer tarefa)
- `contexto_atividade.md` – resumo da atividade, situação-problema e dados do caso
- `docs/Atividade_ Aplicações_de_Engenharia_Economica_GL.pdf` – documento do grupo (delimitação e solução calculada)
- `requisitos.md` – requisitos RF01 a RF13, RNF01 a RNF06 e extensões da versão 2
- `modelo_calculos.md` – fórmulas, convenções, casos especiais e valores de referência dos testes (fonte oficial dos cálculos)
- `registro_ia.md` – diário das interações com a IA (registrar **somente quando o usuário pedir**)

## Regras obrigatórias
- Não invente fórmulas, dados de mercado ou alíquotas legais. As fórmulas estão em `modelo_calculos.md`.
- Trabalhe por etapas e aguarde nossa revisão antes de avançar para a etapa seguinte.
- Não afirme que um teste passou sem executá-lo. Informe comandos e resultados reais.
- Spring Boot (apenas `spring-boot-starter-web` e `spring-boot-starter-test`) é o único framework permitido. Não adicione banco de dados, segurança, serviços externos nem funcionalidades fora do escopo da versão 1.
- Manter a versão 1 o mais simples possível (pouco tempo disponível).
- Só acrescentar entradas em `registro_ia.md` quando o usuário pedir explicitamente.

## Tecnologia e estrutura
- Java 23 + Spring Boot 3 + Maven. Execução local: `mvn spring-boot:run` e abrir `http://localhost:8080`.
- Núcleo de cálculo em Java puro (pacote `calculo`), **sem dependência do Spring**: recebe dados e retorna resultados.
- Controlador REST fino (pacote `web`), que apenas recebe a requisição, chama o núcleo e devolve JSON/CSV.
- Tela em HTML, CSS e JavaScript simples, em `src/main/resources/static/`.
- Gráfico com Chart.js carregado por CDN.
- Testes do núcleo com JUnit 5: `mvn test`.

## Convenções de cálculo (resumo – detalhes em modelo_calculos.md)
- Moeda R$; todos os valores são mensais.
- Taxa digitada em % na tela e convertida em fração no cálculo.
- Arredondar só na exibição; comparar valores monetários com tolerância de R$ 0,01.
- Equilíbrio = teto(custoFixo / contribuicaoUnitaria), arredondando o quociente para 6 casas antes do teto.
- Margem só com receita > 0; sem equilíbrio quando contribuição unitária ≤ 0.
- Caso de referência: fixo 3.000, variável 10, taxa 10%, preço 50, 100 clientes → resultado 500, margem 10%, equilíbrio 86.

## Andamento
- Passo 1 (delimitação), Passo 2 (requisitos) e Passo 3 (modelo de cálculos) concluídos.
- Tecnologia alterada de JavaScript puro para Java/Spring Boot (29/09/2026).
- Próximo: Passo 4 – estrutura do projeto Maven, exemplo mínimo executável e README.
