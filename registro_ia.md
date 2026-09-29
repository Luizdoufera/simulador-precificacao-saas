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
