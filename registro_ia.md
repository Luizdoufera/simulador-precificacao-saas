# Registro de Interações com IA

Diário dos prompts enviados ao agente de IA e do que foi feito com cada resposta.
**Ferramenta:** Claude Code (modelo Claude Opus 5.5), extensão do VS Code.

## Como registrar

Para cada interação relevante, copie o modelo abaixo:

```
### Nº – DD/MM/AAAA – Título curto
**Etapa:** Passo X – ...
**Prompt:** (texto enviado, literal ou resumido)
**Resposta da IA:** (resumo do que a IA fez ou sugeriu)
**Arquivos alterados:** ...
**Validação do grupo:** (o que conferimos, testes rodados, o que foi aceito, corrigido ou rejeitado)
```

---

## Passos 1 a 3 (anteriores a este registro)

> Delimitação, requisitos e modelo de cálculos já estavam concluídos quando este arquivo foi criado.
> **Pendente:** o grupo deve preencher aqui os prompts usados nesses passos, se ainda os tiver.

---

### 01 – 29/09/2026 – Leitura e contextualização do projeto
**Etapa:** Preparação para o Passo 4
**Prompt:** "Poderia fazer uma leitura dos arquivos que estão nesse projeto para você ficar mais contextualizado do mesmo?"
**Resposta da IA:** leu `CLAUDE.md`, `requisitos.md`, `modelo_calculos.md` e o PDF do grupo. Conferiu os valores de referência e apontou divergências: PDF do enunciado e `registro_ia.md` citados mas inexistentes; limite da taxa diferente entre RF09 e o modelo; caso "custo fixo 0 e contribuição ≤ 0" não coberto; texto esperado do RF08 sem as folgas de todas as propostas.
**Arquivos alterados:** nenhum.
**Validação do grupo:** _(preencher)_

### 02 – 29/09/2026 – Contexto, registro da IA e mudança para Java
**Etapa:** Preparação para o Passo 4
**Prompt:** "Coloque o contexto da atividade e o arquivo registro_ia.md na pasta, que vai servir de registro dos prompts de IA. Vamos fazer o projeto em Java, o que sugere para começar? Corrija o que for necessário."
**Resposta da IA:** perguntou qual tipo de projeto Java seria feito. O grupo escolheu **Java web com Spring Boot**. A IA criou `contexto_atividade.md` e este registro, atualizou `CLAUDE.md` para Java/Spring Boot e corrigiu `requisitos.md` e `modelo_calculos.md` (RNF01, RNF02, RNF05, novo RNF06, RF08, RF09, RF10, caso fixo = 0 com contribuição ≤ 0, arredondamento em Java). Sugeriu uma estrutura para o Passo 4, que ainda não foi implementada e aguarda revisão.
**Arquivos alterados:** `CLAUDE.md`, `requisitos.md`, `modelo_calculos.md`; criados `contexto_atividade.md` e `registro_ia.md`.
**Validação do grupo:** _(preencher)_

### 03 – 29/09/2026 – Repositório Git e Passo 4a (esqueleto Spring Boot)
**Etapa:** Passo 4a
**Prompt:** "Podemos prosseguir assim, mas eu precisava de uma certa progressão no projeto do GitHub; colocar tudo de uma vez não é o mais indicado. Aqui está meu repositório: https://github.com/Luizdoufera/simulador-precificacao-saas.git. Vamos pouco a pouco. Estrutura de commit: nome, data do commit e uma descrição breve da implementação."
**Resposta da IA:** ligou a pasta ao repositório remoto, que já tinha um README, e fez commits pequenos no padrão `Nome - DD/MM/AAAA - Descrição`. Commit 1: documentação. Commit 2: esqueleto Spring Boot 3.5.16 com `pom.xml`, classe principal, página inicial provisória, `.gitignore` e README com instruções.
**Verificação executada:** `mvn -q -B package -DskipTests` terminou com código 0. `java -jar target/chavecerta-0.1.0.jar` iniciou o servidor ("Tomcat started on port 8080"), e `http://localhost:8080/` respondeu HTTP 200 com a página inicial.
**Arquivos alterados:** `pom.xml`, `ChaveCertaApplication.java`, `application.properties`, `static/index.html`, `.gitignore`, `README.md`, `registro_ia.md`.
**Validação do grupo:** _(preencher)_
