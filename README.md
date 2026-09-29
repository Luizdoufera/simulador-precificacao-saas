# simulador-precificacao-saas
Aplicação web para simular preços de um SaaS: calcula receita, tributos, resultado mensal, margem e quantidade mínima de clientes para o equilíbrio, comparando cenários. Projeto acadêmico de Engenharia Econômica – Faculdade SENAI FATESG.

**Tema fictício:** ChaveCerta, SaaS de gestão para oficinas mecânicas de pequeno porte.
**Grupo:** Gubio Garcia dos Santos e Luiz Fernando de Pádua Paixão.
**Dados fictícios – simulação.**

## Tecnologias
- Java 23
- Spring Boot 3.5 (somente `spring-boot-starter-web` e `spring-boot-starter-test`)
- Maven 3.9
- HTML, CSS e JavaScript na tela

## Como executar
Pré-requisitos: JDK 23 e Maven instalados.

```bash
mvn spring-boot:run
```

Depois abra `http://localhost:8080` no navegador. Para parar o servidor, use `Ctrl + C`.

## Como testar
```bash
mvn test
```

## Estrutura
```
src/main/java/br/senai/chavecerta/   código Java
src/main/resources/static/           tela (HTML, CSS, JS)
src/test/java/                       testes JUnit
docs/                                documento do grupo
```

## Documentação
- [contexto_atividade.md](contexto_atividade.md): situação-problema e dados do caso
- [requisitos.md](requisitos.md): requisitos funcionais e não funcionais
- [modelo_calculos.md](modelo_calculos.md): fórmulas e valores de referência
- [registro_ia.md](registro_ia.md): registro das interações com IA
